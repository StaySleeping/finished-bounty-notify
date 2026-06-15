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

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("finishedbountynotify")
public interface FinishedBountyNotifyConfig extends Config
{
	@ConfigItem(
		keyName = "notifyOnComplete",
		name = "Notify on completion",
		description = "Send a notification when bounty task collection requirements are met"
	)
	default boolean notifyOnComplete()
	{
		return true;
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
		return NotificationMode.EACH_TASK;
	}

	@ConfigItem(
		keyName = "includeTaskName",
		name = "Include task name",
		description = "Include the bounty task name in the notification message"
	)
	default boolean includeTaskName()
	{
		return true;
	}
}
