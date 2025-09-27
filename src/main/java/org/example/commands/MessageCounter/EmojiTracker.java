package org.example.commands.MessageCounter;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiTracker extends ListenerAdapter {

	public static final Map<String, Integer> emojiCount = new HashMap<>();
	private static final Pattern emojiPattern = Pattern.compile("[\\p{So}\\p{Cn}\\p{Sk}\\p{Sc}\\p{Sm}\\p{Zs}\\p{L}\\p{N}]{1,2}");

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;

		String content = event.getMessage().getContentDisplay();
		Matcher matcher = emojiPattern.matcher(content);

		while (matcher.find()) {
			String emoji = matcher.group();
			if (emoji.codePoints().count() > 1 || emoji.matches("[^\\w\\s]")) {
				emojiCount.merge(emoji, 1, Integer::sum);
			}
		}
	}
}
