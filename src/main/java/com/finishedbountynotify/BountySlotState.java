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
		return task != null && task.getItemQuantity() > 0 && itemsCollected >= task.getItemQuantity();
	}
}
