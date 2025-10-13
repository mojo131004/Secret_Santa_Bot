package org.example.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GuessNumberCommand extends ListenerAdapter {
	private final Map<String, GuessSession> sessions = new HashMap<>();

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if (event.getName().equals("guess")) {
			String userId = event.getUser().getId();

			int secret = new Random().nextInt(100) + 1;
			sessions.put(userId, new GuessSession(secret));

			event.reply("🎯 Ich habe eine Zahl zwischen 1 und 100 gewählt. Du hast 5 Versuche. Gib deine Zahl einfach in den Chat ein!")
					.queue();
		}
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;

		String userId = event.getAuthor().getId();
		String content = event.getMessage().getContentRaw().trim();

		if (!sessions.containsKey(userId)) return;
		if (!content.matches("\\d+")) return;

		int guess = Integer.parseInt(content);
		GuessSession session = sessions.get(userId);
		session.attempts++;

		if (guess == session.secretNumber) {
			sessions.remove(userId);
			event.getChannel().sendMessage("🎉 Richtig geraten, " + event.getAuthor().getName() + "! Die Zahl war " + guess + ".").queue();
		} else if (session.attempts >= 5) {
			sessions.remove(userId);
			event.getChannel().sendMessage("😢 Leider verloren, " + event.getAuthor().getName() + ". Die richtige Zahl war " + session.secretNumber + ".").queue();
		} else if (guess >= 101) {
            event.getChannel().sendMessage( "Diese nummer ist über 100").queue();
            session.attempts--;
        } else {
			String hint = (guess < session.secretNumber) ? "🔼 Zu niedrig!" : "🔽 Zu hoch!";
			int remaining = 5 - session.attempts;
			event.getChannel().sendMessage(hint + " Du hast noch " + remaining + " Versuch(e), " + event.getAuthor().getName() + ".").queue();
		}
	}

	private static class GuessSession {
		int secretNumber;
		int attempts;

		public GuessSession(int secretNumber) {
			this.secretNumber = secretNumber;
			this.attempts = 0;
		}
	}
}
