package com.finishedbountynotify;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ItemsCollectedTest
{
	@Test
	public void computesCollectedFromRemaining()
	{
		assertEquals(0, FinishedBountyNotifyPlugin.itemsCollected(3, 3));
		assertEquals(1, FinishedBountyNotifyPlugin.itemsCollected(3, 2));
		assertEquals(3, FinishedBountyNotifyPlugin.itemsCollected(3, 0));
		assertEquals(3, FinishedBountyNotifyPlugin.itemsCollected(3, -1));
	}

	@Test
	public void collectsBothPartsWhenRemainingDropsToZeroInOneUpdate()
	{
		assertEquals(0, FinishedBountyNotifyPlugin.itemsCollected(2, 2));
		assertEquals(1, FinishedBountyNotifyPlugin.itemsCollected(2, 1));
		assertEquals(2, FinishedBountyNotifyPlugin.itemsCollected(2, 0));
	}
}
