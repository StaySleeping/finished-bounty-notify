package com.finishedbountynotify;

final class BountySlotState
{
	private BountyTaskData task;
	private int itemsCollected;
	private boolean notified;

	void clear()
	{
		task = null;
		itemsCollected = 0;
		notified = false;
	}

	void setTask(BountyTaskData task)
	{
		this.task = task;
		this.itemsCollected = 0;
		this.notified = false;
	}

	BountyTaskData getTask()
	{
		return task;
	}

	int getItemsCollected()
	{
		return itemsCollected;
	}

	void setItemsCollected(int itemsCollected)
	{
		this.itemsCollected = itemsCollected;
	}

	boolean isNotified()
	{
		return notified;
	}

	void setNotified(boolean notified)
	{
		this.notified = notified;
	}

	boolean isComplete()
	{
		return task != null && itemsCollected >= task.getItemQuantity();
	}
}
