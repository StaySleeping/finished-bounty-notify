package com.finishedbountynotify;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BountyTaskWidgetReaderTest
{
	@Test
	public void parsesFractionAsRequiredQuantity()
	{
		assertEquals(25, BountyTaskWidgetReader.parseRequiredQuantity("Collected 10/25"));
	}

	@Test
	public void parsesCollectPhrase()
	{
		assertEquals(5, BountyTaskWidgetReader.parseRequiredQuantity("Bring 5 great white shark jaw"));
	}
}
