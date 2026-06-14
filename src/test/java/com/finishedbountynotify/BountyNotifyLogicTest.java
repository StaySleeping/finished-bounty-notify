package com.finishedbountynotify;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BountyNotifyLogicTest
{
	@Test
	public void monsterGroupIncompleteUntilAllSlotsDone()
	{
		BountySlotState[] slots = new BountySlotState[2];
		slots[0] = slot("npc:1", 5, 5);
		slots[1] = slot("npc:1", 5, 2);

		assertFalse(BountyNotifyLogic.isMonsterGroupComplete("npc:1", slots));

		slots[1].setItemsCollected(5);
		assertTrue(BountyNotifyLogic.isMonsterGroupComplete("npc:1", slots));
	}

	@Test
	public void monsterGroupIgnoresOtherMonsters()
	{
		BountySlotState[] slots = new BountySlotState[2];
		slots[0] = slot("npc:1", 5, 5);
		slots[1] = slot("npc:2", 3, 1);

		assertTrue(BountyNotifyLogic.isMonsterGroupComplete("npc:1", slots));
		assertFalse(BountyNotifyLogic.isMonsterGroupComplete("npc:2", slots));
	}

	private static BountySlotState slot(String groupKey, int required, int collected)
	{
		BountySlotState state = new BountySlotState();
		String monster = groupKey.startsWith("npc:") ? "Monster" : groupKey;
		int npcId = groupKey.startsWith("npc:") ? Integer.parseInt(groupKey.substring(4)) : 0;
		BountyTaskData task = new BountyTaskData(1, 100, monster + " bounty", monster, npcId, 1, required);
		state.setTask(task);
		state.setItemsCollected(collected);
		return state;
	}
}
