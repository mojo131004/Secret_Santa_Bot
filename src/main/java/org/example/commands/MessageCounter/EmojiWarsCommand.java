package org.example.commands.MessageCounter;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;
import java.util.stream.Collectors;

public class EmojiWarsCommand extends ListenerAdapter {

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if (!event.getName().equals("emojiwars")) return;

		Map<String, Integer> counts = EmojiTracker.emojiCount;

		if (counts.isEmpty()) {
			event.reply("😶 Noch keine Emojis gezählt!").queue();
			return;
		}

		List<Map.Entry<String, Integer>> topEmojis = counts.entrySet().stream()
				.sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
				.limit(5)
				.collect(Collectors.toList());

		StringBuilder reply = new StringBuilder("😂 **EmojiWars – Top 5**:\n");
		for (int i = 0; i < topEmojis.size(); i++) {
			String emoji = topEmojis.get(i).getKey();
			int count = topEmojis.get(i).getValue();

			reply.append("**").append(i + 1).append(".** ").append(emoji)
					.append(" – ").append(count).append("x\n");
		}

		event.reply(reply.toString()).queue();
	}
}
