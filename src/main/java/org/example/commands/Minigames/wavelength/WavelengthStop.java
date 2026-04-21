package org.example.commands.Minigames.wavelength;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;

public class WavelengthStop extends ListenerAdapter {

	private final WavelengthSessionManager sessionManager;
	private final Wavelength2Players gameHandler;

	public WavelengthStop(WavelengthSessionManager sessionManager, Wavelength2Players gameHandler) {
		this.sessionManager = sessionManager;
		this.gameHandler = gameHandler;
	}

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if (!event.getName().equals("wavelengthstop")) return;

		String channelId = event.getChannel().getId();

		if (!sessionManager.hasSession(channelId)) {
			event.reply("❌ In diesem Channel läuft aktuell **kein** Wavelength-Spiel.").setEphemeral(true).queue();
			return;
		}

		WavelengthSession session = sessionManager.getSession(channelId);

		// Join-Listener entfernen
		Object listener = session.getJoinListener();
		if (listener instanceof ListenerAdapter adapter) {
			event.getJDA().removeEventListener(adapter);
		}

		if (!(event.getChannel() instanceof GuildMessageChannel guildChannel)) {
			event.reply("❌ Dieser Befehl funktioniert nur in Server-Textchannels.").setEphemeral(true).queue();
			return;
		}

		gameHandler.endGame(session, guildChannel);

		sessionManager.removeSession(channelId);

		event.reply("🛑 **Wavelength-Spiel wurde beendet und gespeichert!**").queue();
	}
}
