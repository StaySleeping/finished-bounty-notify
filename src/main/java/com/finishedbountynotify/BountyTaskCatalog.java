/*
 * Copyright (c) 2026, StaySleeping
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.finishedbountynotify;

import java.util.HashMap;
import java.util.Map;
import net.runelite.api.Client;
import net.runelite.api.gameval.DBTableID;

final class BountyTaskCatalog
{
	private static final Map<Integer, BountyTaskData> BY_ID = new HashMap<>();
	private static final Map<Integer, BountyTaskData> BY_DBROW = new HashMap<>();
	private static final Map<Integer, Integer> WIDGET_QUANTITY_OVERRIDES = new HashMap<>();

	private BountyTaskCatalog()
	{
	}

	static void loadFromClient(Client client)
	{
		BY_ID.clear();
		BY_DBROW.clear();

		for (int rowId : client.getDBTableRows(DBTableID.PortTask.ID))
		{
			BountyTaskData data = fromRow(client, rowId);
			if (data != null)
			{
				BY_ID.put(data.getId(), data);
				BY_DBROW.put(data.getDbrow(), data);
			}
		}
	}

	static BountyTaskData fromDbrow(int dbrow)
	{
		return BY_DBROW.get(dbrow);
	}

	static void setWidgetQuantityOverride(int taskId, int quantity)
	{
		if (quantity > 0)
		{
			WIDGET_QUANTITY_OVERRIDES.put(taskId, quantity);
		}
	}

	static int resolveQuantity(int taskId, int dbQuantity)
	{
		Integer widgetQuantity = WIDGET_QUANTITY_OVERRIDES.get(taskId);
		if (widgetQuantity != null && widgetQuantity > 0)
		{
			return widgetQuantity;
		}
		if (dbQuantity > 0)
		{
			return dbQuantity;
		}
		return WikiBountyFallback.getQuantity(taskId);
	}

	private static BountyTaskData fromRow(Client client, int dbrow)
	{
		Integer id = getIntField(client, dbrow, DBTableID.PortTask.COL_TASK_ID, 0);
		if (id == null)
		{
			return null;
		}

		Integer cargoPort = getIntField(client, dbrow, DBTableID.PortTask.COL_CARGO_PORT, 0);
		Integer endingPort = getIntField(client, dbrow, DBTableID.PortTask.COL_ENDING_PORT, 0);
		if (cargoPort == null || endingPort == null || !cargoPort.equals(endingPort))
		{
			return null;
		}

		String taskName = (String) client.getDBTableField(dbrow, DBTableID.PortTask.COL_NAME, 0)[0];
		Integer itemQuantity = getIntField(client, dbrow, DBTableID.PortTask.COL_BOUNTY_OBJECT_AMOUNT, 0);
		Integer itemId = getIntField(client, dbrow, DBTableID.PortTask.COL_BOUNTY_OBJECT, 0);
		Integer npcId = getIntField(client, dbrow, DBTableID.PortTask.COL_BOUNTY_TARGET_ALIVE, 0);
		if (itemId == null || npcId == null)
		{
			return null;
		}

		int quantity = itemQuantity != null ? itemQuantity : 0;
		String monsterName = WikiBountyFallback.getMonsterName(id);
		return new BountyTaskData(dbrow, id, taskName, monsterName, npcId, itemId, quantity);
	}

	static BountyTaskData fromId(int id)
	{
		BountyTaskData data = BY_ID.get(id);
		if (data != null)
		{
			return data;
		}

		int wikiQuantity = WikiBountyFallback.getQuantity(id);
		if (wikiQuantity <= 0)
		{
			return null;
		}

		String monsterName = WikiBountyFallback.getMonsterName(id);
		String taskName = monsterName != null ? monsterName + " bounty" : "Bounty task";
		return new BountyTaskData(-1, id, taskName, monsterName, 0, 0, wikiQuantity);
	}

	private static Integer getIntField(Client client, int rowId, int col, int tupleIndex)
	{
		Object[] field = client.getDBTableField(rowId, col, tupleIndex);
		if (field == null || field.length == 0)
		{
			return null;
		}
		return (int) field[0];
	}
}
