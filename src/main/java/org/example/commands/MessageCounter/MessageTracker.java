package org.example.commands.MessageCounter;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class MessageTracker extends ListenerAdapter {

	public static final Map<String, Integer> messageCount = new HashMap<>();

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;

		String userId = event.getAuthor().getId();
		messageCount.merge(userId, 1, Integer::sum);
	}
}
