package org.example.commands.wavelength;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class WavelengthStop extends ListenerAdapter {

	private final WavelengthSessionManager sessionManager;

	public WavelengthStop(WavelengthSessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if (!event.getName().equals("wavelengthstop")) return;

		String channelId = event.getChannel().getId();

		// Prüfen, ob überhaupt eine Session existiert
		if (!sessionManager.hasSession(channelId)) {
			event.reply("❌ In diesem Channel läuft aktuell **kein** Wavelength-Spiel.").setEphemeral(true).queue();
			return;
		}

		// Session löschen
		sessionManager.removeSession(channelId);

		event.reply("🛑 **Wavelength-Spiel wurde abgebrochen!**\nAlle Daten wurden gelöscht.").queue();
	}
}
