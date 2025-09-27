package org.example.commands.MessageCounter;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ActivityTracker extends ListenerAdapter {

	public static final Map<Integer, Integer> hourlyActivity = new HashMap<>();

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;

		int hour = LocalDateTime.now().getHour(); // 0–23
		hourlyActivity.merge(hour, 1, Integer::sum);
	}
}
