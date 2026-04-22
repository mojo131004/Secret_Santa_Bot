package org.example.commands.Minigames.wavelength;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class WavelengthBestList extends ListenerAdapter {

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if (!event.getName().equals("wavelengthbestlist")) return;

		String bestlist = BestListManager.formatBestList();

		event.reply(bestlist).queue();
	}
}
