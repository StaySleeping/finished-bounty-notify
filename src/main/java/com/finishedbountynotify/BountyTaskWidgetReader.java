package com.finishedbountynotify;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;

@Singleton
final class BountyTaskWidgetReader
{
	private static final Pattern FRACTION = Pattern.compile("(\\d+)\\s*/\\s*(\\d+)");
	private static final Pattern COLLECT = Pattern.compile("(?i)(?:collect|bring|gather|kill)\\s+(?:\\w+\\s+){0,6}(\\d+)");

	@Inject
	private Client client;

	void scanOpenInterfaces()
	{
		scanWidgetTree(client.getWidget(InterfaceID.PortTaskBoard.CONTAINER));
		scanWidgetTree(client.getWidget(InterfaceID.SailingLog.TASKS_CONTENT));
		scanWidgetTree(client.getWidget(InterfaceID.SailingLog.LIST));
		scanWidgetTree(client.getWidget(InterfaceID.PortTaskInfo.TEXT));
	}

	private void scanWidgetTree(Widget widget)
	{
		if (widget == null || widget.isHidden())
		{
			return;
		}

		applyWidget(widget);

		Widget[] children = widget.getChildren();
		if (children != null)
		{
			for (Widget child : children)
			{
				scanWidgetTree(child);
			}
		}

		Widget[] dynamicChildren = widget.getDynamicChildren();
		if (dynamicChildren != null)
		{
			for (Widget child : dynamicChildren)
			{
				scanWidgetTree(child);
			}
		}
	}

	private void applyWidget(Widget widget)
	{
		Integer dbrow = getDbrowFromWidget(widget);
		BountyTaskData task = dbrow != null ? BountyTaskCatalog.fromDbrow(dbrow) : null;

		String text = widget.getText();
		if (text != null && !text.isEmpty())
		{
			int quantity = parseRequiredQuantity(text);
			if (quantity > 0)
			{
				if (task != null)
				{
					BountyTaskCatalog.setWidgetQuantityOverride(task.getId(), quantity);
				}
			}
		}

		if (task != null)
		{
			String name = widget.getName();
			if (name != null && !name.isEmpty())
			{
				int quantity = parseRequiredQuantity(name);
				if (quantity > 0)
				{
					BountyTaskCatalog.setWidgetQuantityOverride(task.getId(), quantity);
				}
			}
		}
	}

	static Integer getDbrowFromWidget(Widget widget)
	{
		if (widget == null)
		{
			return null;
		}

		Object[] ops = widget.getOnOpListener();
		if (ops == null || ops.length < 4)
		{
			return null;
		}

		Object value = ops[3];
		return value instanceof Integer ? (Integer) value : null;
	}

	static int parseRequiredQuantity(String text)
	{
		Matcher fraction = FRACTION.matcher(text);
		if (fraction.find())
		{
			int left = Integer.parseInt(fraction.group(1));
			int right = Integer.parseInt(fraction.group(2));
			if (right > 0 && left <= right)
			{
				return right;
			}
		}

		Matcher collect = COLLECT.matcher(text);
		if (collect.find())
		{
			return Integer.parseInt(collect.group(1));
		}

		return 0;
	}
}
