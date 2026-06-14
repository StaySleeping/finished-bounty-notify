package com.finishedbountynotify;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BountyTaskCatalogTest
{
	@Test
	public void resolvesQuantityPriority()
	{
		assertEquals(7, BountyTaskCatalog.resolveQuantity(99999, 7));
		assertEquals(5, BountyTaskCatalog.resolveQuantity(465, 0));
	}
}
