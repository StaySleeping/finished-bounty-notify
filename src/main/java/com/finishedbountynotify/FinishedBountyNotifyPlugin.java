package com.finishedbountynotify;

import com.google.inject.Provides;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Finished Bounty Notify",
	description = "Notifies you when a sailing bounty task has all parts collected",
	tags = {"sailing", "bounty", "port", "tasks", "notification"}
)
public class FinishedBountyNotifyPlugin extends Plugin
{
	private static final int TASK_SLOT_COUNT = 5;

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private FinishedBountyNotifyConfig config;

	@Inject
	private Notifier notifier;

	@Inject
	private BountyTaskWidgetReader widgetReader;

	private final BountySlotState[] slots = new BountySlotState[TASK_SLOT_COUNT];
	private final Map<String, Boolean> monsterGroupNotified = new HashMap<>();
	private boolean suppressNotifications;

	@Override
	protected void startUp()
	{
		for (int i = 0; i < slots.length; i++)
		{
			slots[i] = new BountySlotState();
		}
	}

	@Override
	protected void shutDown()
	{
		monsterGroupNotified.clear();
		for (int i = 0; i < slots.length; i++)
		{
			if (slots[i] != null)
			{
				slots[i].clear();
			}
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		suppressNotifications = true;
		try
		{
			BountyTaskCatalog.loadFromClient(client);
			widgetReader.scanOpenInterfaces();
			readAllSlotsFromClient();
		}
		catch (Exception e)
		{
			log.warn("Failed to load bounty task data on login", e);
		}
		finally
		{
			suppressNotifications = false;
		}
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded event)
	{
		int groupId = event.getGroupId();
		if (groupId != InterfaceID.PORT_TASK_BOARD
			&& groupId != InterfaceID.SAILING_LOG
			&& groupId != InterfaceID.PORT_TASK_INFO)
		{
			return;
		}

		clientThread.invokeLater(() ->
		{
			widgetReader.scanOpenInterfaces();
			refreshSlotCompletion();
		});
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		if (!PortTaskTrigger.contains(event.getVarbitId()))
		{
			return;
		}

		PortTaskTrigger trigger = PortTaskTrigger.fromId(event.getVarbitId());
		handleTrigger(trigger, event.getValue());
	}

	private void readAllSlotsFromClient()
	{
		int[] varps = client.getVarps();
		if (varps == null)
		{
			return;
		}

		for (PortTaskTrigger trigger : PortTaskTrigger.values())
		{
			int value = client.getVarbitValue(varps, trigger.getId());
			handleTrigger(trigger, value);
		}
	}

	private void handleTrigger(PortTaskTrigger trigger, int value)
	{
		int slot = trigger.getSlot();
		if (slot < 0 || slot >= slots.length)
		{
			return;
		}

		switch (trigger.getType())
		{
			case ID:
				handleTaskIdChanged(slot, value);
				break;
			case COUNT:
				handleCountChanged(slot, value);
				break;
		}
	}

	private void handleTaskIdChanged(int slot, int taskId)
	{
		BountySlotState state = slots[slot];
		String previousGroup = state.getTask() != null ? state.getTask().getMonsterGroupKey() : null;

		if (taskId == 0)
		{
			state.clear();
			resetMonsterGroup(previousGroup);
			return;
		}

		BountyTaskData task = BountyTaskCatalog.fromId(taskId);
		if (task == null)
		{
			state.clear();
			resetMonsterGroup(previousGroup);
			return;
		}

		if (state.getTask() == null || state.getTask().getId() != taskId)
		{
			state.setTask(task);
			resetMonsterGroup(previousGroup);
			resetMonsterGroup(task.getMonsterGroupKey());
		}
	}

	private void handleCountChanged(int slot, int remaining)
	{
		BountySlotState state = slots[slot];
		if (state.getTask() == null)
		{
			PortTaskTrigger idTrigger = PortTaskTrigger.idTriggerForSlot(slot);
			if (idTrigger != null)
			{
				handleTaskIdChanged(slot, client.getVarbitValue(idTrigger.getId()));
			}
		}

		BountyTaskData task = state.getTask();
		if (task == null)
		{
			return;
		}

		int required = task.getItemQuantity();
		int collected = itemsCollected(required, remaining);
		boolean wasComplete = state.isComplete();
		state.setItemsCollected(collected);

		if (!wasComplete && state.isComplete())
		{
			maybeNotify(state, task);
		}
	}

	private void refreshSlotCompletion()
	{
		for (int slot = 0; slot < slots.length; slot++)
		{
			BountySlotState state = slots[slot];
			if (state.getTask() == null)
			{
				continue;
			}

			PortTaskTrigger countTrigger = PortTaskTrigger.countTriggerForSlot(slot);
			if (countTrigger == null)
			{
				continue;
			}

			boolean wasComplete = state.isComplete();
			handleCountChanged(slot, client.getVarbitValue(countTrigger.getId()));
			if (!suppressNotifications && wasComplete && !state.isComplete())
			{
				resetMonsterGroup(state.getTask().getMonsterGroupKey());
			}
		}
	}

	static int itemsCollected(int required, int remaining)
	{
		return Math.max(0, Math.min(required, required - remaining));
	}

	private void maybeNotify(BountySlotState state, BountyTaskData task)
	{
		if (suppressNotifications || !config.notifyOnComplete())
		{
			return;
		}

		if (config.notificationMode() == NotificationMode.EACH_TASK)
		{
			if (state.isNotified())
			{
				return;
			}

			sendNotification(task);
			state.setNotified(true);
			return;
		}

		String groupKey = task.getMonsterGroupKey();
		if (Boolean.TRUE.equals(monsterGroupNotified.get(groupKey)))
		{
			return;
		}

		if (!BountyNotifyLogic.isMonsterGroupComplete(groupKey, slots))
		{
			return;
		}

		sendNotification(task);
		monsterGroupNotified.put(groupKey, true);
	}

	private void sendNotification(BountyTaskData task)
	{
		String message;
		if (config.includeTaskName())
		{
			if (config.notificationMode() == NotificationMode.ALL_MONSTER_TASKS
				&& task.getMonsterName() != null && !task.getMonsterName().isEmpty())
			{
				message = "All " + task.getMonsterName() + " bounty tasks complete";
			}
			else
			{
				message = "Bounty task complete: " + task.getTaskName();
			}
		}
		else
		{
			message = config.notificationMode() == NotificationMode.ALL_MONSTER_TASKS
				? "All bounty tasks for a monster are complete"
				: "A sailing bounty task is complete";
		}

		notifier.notify(message);
	}

	private void resetMonsterGroup(String groupKey)
	{
		if (groupKey != null)
		{
			monsterGroupNotified.remove(groupKey);
		}
	}

	@Provides
	FinishedBountyNotifyConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(FinishedBountyNotifyConfig.class);
	}
}
