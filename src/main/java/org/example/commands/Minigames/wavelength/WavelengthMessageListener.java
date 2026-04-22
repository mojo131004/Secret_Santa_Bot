package org.example.commands.Minigames.wavelength;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class WavelengthMessageListener extends ListenerAdapter {

	private final WavelengthSessionManager sessionManager;

	public WavelengthMessageListener(WavelengthSessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {

		if (event.getAuthor().isBot()) return;

		String channelId = event.getChannel().getId();

		if (!sessionManager.hasSession(channelId)) return;

		WavelengthSession session = sessionManager.getSession(channelId);

		if (!session.isGameStarted()) return;

		String userId = event.getAuthor().getId();
		String content = event.getMessage().getContentRaw().trim();

		// ❗ Nur Guess-Phase existiert noch
		if (!session.isWaitingForGuess()) return;

		if (!userId.equals(session.getCurrentGuesser())) {
			event.getChannel().sendMessage("❌ Nur <@" + session.getCurrentGuesser() + "> darf raten.").queue();
			return;
		}

		// ✅ Nur Zahlen erlauben
		if (!content.matches("\\d+")) {
			event.getChannel().sendMessage("❌ Bitte gib **nur eine Zahl** ein.").queue();
			return;
		}

		int guess = Integer.parseInt(content);

		if (guess < 1 || guess > 10) {
			event.getChannel().sendMessage("❌ Zahl muss zwischen **1 und 10** sein.").queue();
			return;
		}

		session.setWaitingForGuess(false);

		int secret = session.getSecretNumber();
		int distance = Math.abs(secret - guess);

		int points = (distance == 0 ? 2 : distance == 1 ? 1 : 0);

		session.addPoints(userId, points);
		session.incrementRounds();

		event.getChannel().sendMessage(
				"🎯 **Ergebnis!**\n" +
						"Geheime Zahl: **" + secret + "**\n" +
						"Guess von <@" + userId + ">: **" + guess + "**\n" +
						"➡️ Punkte: **" + points + "**"
		).queue();

		startNextRound(event);
	}

	private void startNextRound(MessageReceivedEvent event) {
		String channelId = event.getChannel().getId();
		WavelengthSession session = sessionManager.getSession(channelId);

		int secret = 1 + (int)(Math.random() * 10);
		session.setSecretNumber(secret);

		String topic = WavelengthTopics.getRandomTopic();
		session.setTopic(topic);

		// ✅ DIREKT GUESS-PHASE
		session.setWaitingForGuess(true);

		event.getJDA().retrieveUserById(session.getCurrentDescriber()).queue(user ->
				user.openPrivateChannel().queue(dm ->
						dm.sendMessage("🔐 Deine geheime Zahl ist: **" + secret + "**").queue()
				)
		);

		event.getChannel().sendMessage(
				"🧠 **Neue Runde!**\n" +
						"Beschreiber: <@" + session.getCurrentDescriber() + ">\n" +
						"Guesser: <@" + session.getCurrentGuesser() + ">\n" +
						"Thema: **" + topic + "**\n\n" +
						"🎙️ Hinweis erfolgt im Voice!\n" +
						"👉 <@" + session.getCurrentGuesser() + "> gib jetzt eine Zahl ein!"
		).queue();
	}
}