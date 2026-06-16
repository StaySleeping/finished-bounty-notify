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
