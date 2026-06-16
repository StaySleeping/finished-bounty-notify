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
