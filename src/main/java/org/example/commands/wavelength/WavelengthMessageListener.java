package org.example.commands.wavelength;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class WavelengthMessageListener extends ListenerAdapter {

	private final WavelengthSessionManager sessionManager;

	public WavelengthMessageListener(WavelengthSessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		// Bot-Nachrichten ignorieren
		if (event.getAuthor().isBot()) return;

		String channelId = event.getChannel().getId();

		// Prüfen, ob in diesem Channel ein Spiel läuft
		if (!sessionManager.hasSession(channelId)) return;

		WavelengthSession session = sessionManager.getSession(channelId);

		// Wenn das Spiel noch nicht gestartet ist → ignorieren
		if (!session.isGameStarted()) return;

		// Wenn wir nicht auf einen Guess warten → ignorieren
		if (!session.isWaitingForGuess()) return;

		String userId = event.getAuthor().getId();

		// Prüfen, ob der richtige Spieler guessed
		if (!userId.equals(session.getCurrentGuesser())) {
			event.getChannel().sendMessage("❌ Nur <@" + session.getCurrentGuesser() + "> darf jetzt raten.").queue();
			return;
		}

		// Prüfen, ob die Nachricht eine Zahl ist
		String content = event.getMessage().getContentRaw().trim();

		int guess;
		try {
			guess = Integer.parseInt(content);
		} catch (NumberFormatException e) {
			return; // keine Zahl → ignorieren
		}

		// Zahl muss zwischen 1 und 10 sein
		if (guess < 1 || guess > 10) {
			event.getChannel().sendMessage("❌ Bitte gib eine Zahl zwischen **1 und 10** ein.").queue();
			return;
		}

		// Guess akzeptiert → Runde beenden
		session.setWaitingForGuess(false);

		int secret = session.getSecretNumber();
		int distance = Math.abs(secret - guess);

		int points = 0;
		if (distance == 0) points = 2;
		else if (distance == 1) points = 1;

		// Punkte vergeben
		if (session.getMode() == WavelengthSession.Mode.FOUR_PLAYERS_TEAMS) {
			String team = session.getTeams().get(session.getCurrentGuesser());
			session.addTeamPoints(team, points);
		} else {
			session.addPoints(userId, points);
		}

		// Ergebnis anzeigen
		event.getChannel().sendMessage(
				"🎯 **Ergebnis!**\n" +
						"Geheime Zahl: **" + secret + "**\n" +
						"Guess von <@" + userId + ">: **" + guess + "**\n" +
						"➡️ Punkte: **" + points + "**"
		).queue();

		// Nächste Runde vorbereiten
		session.nextTurn();

		// Neue Runde starten
		startNextRound(event.getChannel().getId(), event);
	}

	private void startNextRound(String channelId, MessageReceivedEvent event) {
		WavelengthSession session = sessionManager.getSession(channelId);

		// Neue geheime Zahl
		int secret = 1 + (int)(Math.random() * 10);
		session.setSecretNumber(secret);

		// Neues Thema
		String topic = WavelengthTopics.getRandomTopic();
		session.setTopic(topic);

		// DM an den Beschreiber
		event.getJDA().retrieveUserById(session.getCurrentDescriber()).queue(user -> {
			user.openPrivateChannel().queue(dm -> {
				dm.sendMessage("🔐 Deine geheime Zahl ist: **" + secret + "**").queue();
			});
		});

		// Channel-Info
		event.getChannel().sendMessage(
				"🧠 **Neue Runde!**\n" +
						"Beschreiber: <@" + session.getCurrentDescriber() + ">\n" +
						"Guesser: <@" + session.getCurrentGuesser() + ">\n" +
						"Thema: **" + session.getTopic() + "**\n\n" +
						"👉 <@" + session.getCurrentDescriber() + "> beschreibt jetzt etwas.\n" +
						"👉 <@" + session.getCurrentGuesser() + "> rät danach eine Zahl zwischen **1 und 10**."
		).queue();

		session.setWaitingForGuess(true);
	}
}
