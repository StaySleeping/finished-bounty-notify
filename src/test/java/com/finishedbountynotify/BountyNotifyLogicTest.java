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
