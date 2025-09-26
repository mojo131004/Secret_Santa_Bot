package org.example.commands.MessageCounter;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;
import java.util.stream.Collectors;

public class TopTalkersCommand extends ListenerAdapter {

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if (!event.getName().equals("toptalkers")) return;

		Map<String, Integer> counts = MessageTracker.messageCount;

		if (counts.isEmpty()) {
			event.reply("📭 Noch keine Nachrichten gezählt!").queue();
			return;
		}

		List<Map.Entry<String, Integer>> topUsers = counts.entrySet().stream()
				.sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
				.limit(5)
				.collect(Collectors.toList());

		StringBuilder reply = new StringBuilder("🏆 **Top Chatter**:\n");
		for (int i = 0; i < topUsers.size(); i++) {
			String userId = topUsers.get(i).getKey();
			int count = topUsers.get(i).getValue();
			Member member = event.getGuild().getMemberById(userId);
			String name = member != null ? member.getEffectiveName() : "Unbekannt";

			reply.append("**").append(i + 1).append(". ").append(name)
					.append("** – ").append(count).append(" Nachrichten\n");
		}

		event.reply(reply.toString()).queue();
	}
}
