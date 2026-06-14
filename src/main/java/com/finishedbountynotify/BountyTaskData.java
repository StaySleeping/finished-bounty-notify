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
