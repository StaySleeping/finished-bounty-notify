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

final class BountyTaskData
{
	private final int dbrow;
	private final int id;
	private final String taskName;
	private final String monsterName;
	private final int npcId;
	private final int itemId;
	private final int dbQuantity;

	BountyTaskData(int dbrow, int id, String taskName, String monsterName, int npcId, int itemId, int dbQuantity)
	{
		this.dbrow = dbrow;
		this.id = id;
		this.taskName = taskName;
		this.monsterName = monsterName;
		this.npcId = npcId;
		this.itemId = itemId;
		this.dbQuantity = dbQuantity;
	}

	int getDbrow()
	{
		return dbrow;
	}

	int getId()
	{
		return id;
	}

	String getTaskName()
	{
		return taskName;
	}

	String getMonsterName()
	{
		return monsterName;
	}

	int getNpcId()
	{
		return npcId;
	}

	int getItemId()
	{
		return itemId;
	}

	int getItemQuantity()
	{
		return BountyTaskCatalog.resolveQuantity(id, dbQuantity);
	}

	String getMonsterGroupKey()
	{
		if (npcId > 0)
		{
			return "npc:" + npcId;
		}
		if (monsterName != null && !monsterName.isEmpty())
		{
			return "monster:" + monsterName.toLowerCase();
		}
		return "task:" + id;
	}
}
