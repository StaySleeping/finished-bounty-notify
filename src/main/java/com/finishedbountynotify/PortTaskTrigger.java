/*
 * Copyright (c) 2026, StaySleeping <https://github.com/StaySleeping>
 * Copyright (c) 2025, nucleon <https://github.com/nucleon>
 * Copyright (c) 2025, Cooper Morris <https://github.com/coopermor>
 * All rights reserved.
 *
 * Portions of this file are derived from port-tasks (https://github.com/nucleon/port-tasks),
 * used under the BSD 2-Clause License.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.finishedbountynotify;

import java.util.HashMap;
import java.util.Map;
import net.runelite.api.gameval.VarbitID;

enum PortTaskTrigger
{
	TASK_SLOT_0_ID(VarbitID.PORT_TASK_SLOT_0_ID, TaskType.ID, 0),
	TASK_SLOT_0_COUNT(14662, TaskType.COUNT, 0),
	TASK_SLOT_1_ID(VarbitID.PORT_TASK_SLOT_1_ID, TaskType.ID, 1),
	TASK_SLOT_1_COUNT(14663, TaskType.COUNT, 1),
	TASK_SLOT_2_ID(VarbitID.PORT_TASK_SLOT_2_ID, TaskType.ID, 2),
	TASK_SLOT_2_COUNT(14819, TaskType.COUNT, 2),
	TASK_SLOT_3_ID(VarbitID.PORT_TASK_SLOT_3_ID, TaskType.ID, 3),
	TASK_SLOT_3_COUNT(15370, TaskType.COUNT, 3),
	TASK_SLOT_4_ID(VarbitID.PORT_TASK_SLOT_4_ID, TaskType.ID, 4),
	TASK_SLOT_4_COUNT(15397, TaskType.COUNT, 4),
	;

	enum TaskType
	{
		ID,
		COUNT
	}

	private static final Map<Integer, PortTaskTrigger> LOOKUP = new HashMap<>();

	static
	{
		for (PortTaskTrigger trigger : values())
		{
			LOOKUP.put(trigger.id, trigger);
		}
	}

	private final int id;
	private final TaskType type;
	private final int slot;

	PortTaskTrigger(int id, TaskType type, int slot)
	{
		this.id = id;
		this.type = type;
		this.slot = slot;
	}

	int getId()
	{
		return id;
	}

	TaskType getType()
	{
		return type;
	}

	int getSlot()
	{
		return slot;
	}

	static PortTaskTrigger fromId(int id)
	{
		return LOOKUP.get(id);
	}

	static boolean contains(int id)
	{
		return LOOKUP.containsKey(id);
	}

	static PortTaskTrigger idTriggerForSlot(int slot)
	{
		for (PortTaskTrigger trigger : values())
		{
			if (trigger.type == TaskType.ID && trigger.slot == slot)
			{
				return trigger;
			}
		}
		return null;
	}

	static PortTaskTrigger countTriggerForSlot(int slot)
	{
		for (PortTaskTrigger trigger : values())
		{
			if (trigger.type == TaskType.COUNT && trigger.slot == slot)
			{
				return trigger;
			}
		}
		return null;
	}
}
