package com.finishedbountynotify;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import org.junit.Test;

public class BountySlotCompletionTest
{
	private static final int TWO_PART_TASK_ID = 99999;
	private static final int TWO_PART_REQUIRED = 2;

	@Test
	public void bothPartsCollectedInOneVarbitUpdateCompletesTask()
	{
		BountySlotState state = slotState(TWO_PART_REQUIRED, 0);

		assertFalse(state.isComplete());
		assertTrue(FinishedBountyNotifyPlugin.applyCountUpdate(state, TWO_PART_REQUIRED, 0));
		assertTrue(state.isComplete());
		assertEquals(TWO_PART_REQUIRED, state.getItemsCollected());
	}

	@Test
	public void bothPartsAtOnceDoesNotRecompleteOnSecondVarbitRead()
	{
		BountySlotState state = slotState(TWO_PART_REQUIRED, 0);
		assertTrue(FinishedBountyNotifyPlugin.applyCountUpdate(state, TWO_PART_REQUIRED, 0));

		assertFalse(FinishedBountyNotifyPlugin.applyCountUpdate(state, TWO_PART_REQUIRED, 0));
		assertTrue(state.isComplete());
	}

	@Test
	public void onePartAtATimeCompletesOnlyAfterFinalPart()
	{
		BountySlotState state = slotState(TWO_PART_REQUIRED, 0);

		assertFalse(FinishedBountyNotifyPlugin.applyCountUpdate(state, TWO_PART_REQUIRED, 1));
		assertFalse(state.isComplete());
		assertEquals(1, state.getItemsCollected());

		assertTrue(FinishedBountyNotifyPlugin.applyCountUpdate(state, TWO_PART_REQUIRED, 0));
		assertTrue(state.isComplete());
		assertEquals(TWO_PART_REQUIRED, state.getItemsCollected());
	}

	@Test
	public void catalogQuantityUsedForMultiPartTask()
	{
		assertEquals(TWO_PART_REQUIRED, BountyTaskCatalog.resolveQuantity(TWO_PART_TASK_ID, TWO_PART_REQUIRED));
	}

	@Test
	public void notificationModeIsPublicForConfigProxy()
	{
		assertTrue(Modifier.isPublic(NotificationMode.class.getModifiers()));
	}

	@Test
	public void configProxyCanReturnNotificationMode()
	{
		FinishedBountyNotifyConfig config = (FinishedBountyNotifyConfig) Proxy.newProxyInstance(
			FinishedBountyNotifyConfig.class.getClassLoader(),
			new Class<?>[] {FinishedBountyNotifyConfig.class},
			(proxy, method, args) ->
			{
				if ("notificationMode".equals(method.getName()))
				{
					return NotificationMode.EACH_TASK;
				}
				if (method.getReturnType() == boolean.class)
				{
					return true;
				}
				return null;
			});

		assertEquals(NotificationMode.EACH_TASK, config.notificationMode());
	}

	private static BountySlotState slotState(int required, int collected)
	{
		BountySlotState state = new BountySlotState();
		BountyTaskData task = new BountyTaskData(
			1,
			TWO_PART_TASK_ID,
			"Test bounty",
			"Test monster",
			1,
			1,
			required);
		state.setTask(task);
		state.setItemsCollected(collected);
		return state;
	}
}
