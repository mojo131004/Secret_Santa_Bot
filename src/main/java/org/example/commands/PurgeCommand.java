package org.example.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class PurgeCommand extends ListenerAdapter {

	// Die erlaubte User-ID
	private static final String ALLOWED_USER_ID = "486543345355587584";

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if (!event.getName().equals("purge")) return;

		User user = event.getUser();

		// ✅ Check: nur die erlaubte Person darf den Command nutzen
		if (!user.getId().equals(ALLOWED_USER_ID)) {
			event.reply("❌ Du hast keine Berechtigung, diesen Command auszuführen.")
					.setEphemeral(true)
					.queue();
			return;
		}

		int amount = event.getOption("amount").getAsInt();

		if (amount < 1 || amount > 100) {
			event.reply("❌ Bitte gib eine Zahl zwischen 1 und 100 an.")
					.setEphemeral(true)
					.queue();
			return;
		}

		TextChannel channel = event.getChannel().asTextChannel();

		channel.getHistory().retrievePast(amount).queue(messages -> {
			channel.deleteMessages(messages).queue(
					success -> event.reply("🧹 " + amount + " Nachrichten gelöscht.").setEphemeral(true).queue(),
					error -> event.reply("❌ Fehler beim Löschen der Nachrichten.").setEphemeral(true).queue()
			);
		});
	}
}
