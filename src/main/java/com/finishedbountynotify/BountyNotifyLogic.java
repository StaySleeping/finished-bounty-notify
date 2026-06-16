package com.finishedbountynotify;

final class BountyNotifyLogic
{
	private BountyNotifyLogic()
	{
	}

	static boolean isMonsterGroupComplete(String groupKey, BountySlotState[] slots)
	{
		boolean hasTask = false;
		for (BountySlotState slot : slots)
		{
			BountyTaskData task = slot.getTask();
			if (task == null || !groupKey.equals(task.getMonsterGroupKey()))
			{
				continue;
			}

			hasTask = true;
			if (!slot.isComplete())
			{
				return false;
			}
		}
		return hasTask;
	}
}
