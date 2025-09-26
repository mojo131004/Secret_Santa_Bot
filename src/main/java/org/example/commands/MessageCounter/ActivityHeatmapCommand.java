package org.example.commands.MessageCounter;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Map;
import java.util.TreeMap;

public class ActivityHeatmapCommand extends ListenerAdapter {

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if (!event.getName().equals("activityheatmap")) return;

		Map<Integer, Integer> activity = new TreeMap<>(ActivityTracker.hourlyActivity);

		if (activity.isEmpty()) {
			event.reply("📭 Noch keine Aktivität erfasst!").queue();
			return;
		}

		StringBuilder reply = new StringBuilder("📊 **Activity Heatmap**:\n");
		for (int hour = 0; hour < 24; hour++) {
			int count = activity.getOrDefault(hour, 0);
			String bar = "▓".repeat(Math.min(count / 2, 20)); // max 20 Balken
			reply.append(String.format("**%02d:00** → %s (%d)\n", hour, bar, count));
		}

		event.reply(reply.toString()).queue();
	}
}
