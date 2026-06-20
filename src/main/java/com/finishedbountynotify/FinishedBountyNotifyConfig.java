package com.finishedbountynotify;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Notification;

@ConfigGroup("finishedbountynotify")
public interface FinishedBountyNotifyConfig extends Config
{
	@ConfigItem(
		keyName = "notifyOnComplete",
		name = "Notify on completion",
		description = "Send a notification when bounty task collection requirements are met"
	)
	default Notification notifyOnComplete()
	{
		return Notification.ON;
	}

	@ConfigItem(
		keyName = "notificationMode",
		name = "Notification mode",
		description = "Each task: notify when any single bounty task is finished. "
			+ "All tasks for monster: wait until every accepted bounty for that monster is finished "
			+ "(e.g. two great white shark tasks notify once when both are done)"
	)
	default NotificationMode notificationMode()
	{
		return NotificationMode.ALL_MONSTER_TASKS;
	}

}
