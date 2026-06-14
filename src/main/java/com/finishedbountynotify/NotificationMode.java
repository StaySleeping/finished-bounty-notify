package com.finishedbountynotify;

enum NotificationMode
{
	EACH_TASK("Each task"),
	ALL_MONSTER_TASKS("All tasks for monster");

	private final String label;

	NotificationMode(String label)
	{
		this.label = label;
	}

	@Override
	public String toString()
	{
		return label;
	}
}
