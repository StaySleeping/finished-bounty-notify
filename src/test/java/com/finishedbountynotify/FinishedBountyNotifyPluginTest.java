package com.finishedbountynotify;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class FinishedBountyNotifyPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(FinishedBountyNotifyPlugin.class);
		RuneLite.main(args);
	}
}
