package org.example.commands.Minigames.wavelength;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashSet;
import java.util.Set;

public class Wavelength2Players extends ListenerAdapter {

	private final WavelengthSessionManager sessionManager;

	public Wavelength2Players(WavelengthSessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if (!event.getName().equals("wavelength2players")) return;

		String channelId = event.getChannel().getId();

		if (sessionManager.hasSession(channelId)) {
			event.reply("❌ In diesem Channel läuft bereits ein Wavelength-Spiel!").setEphemeral(true).queue();
			return;
		}

		WavelengthSession session = sessionManager.createSession(
				channelId,
				WavelengthSession.Mode.TWO_PLAYERS
		);

		event.reply("🎮 **Wavelength – 2 Spieler**\n\nSchreibt **`join`**, um mitzuspielen.").queue();

		collectPlayers(event, session);
	}

	private void collectPlayers(SlashCommandInteractionEvent event, WavelengthSession session) {
		String channelId = session.getChannelId();
		var channel = event.getChannel();

		Set<String> joinedPlayers = new HashSet<>();

		channel.sendMessage("👉 Wer möchte mitspielen? Schreibe **`join`**!").queue();

		ListenerAdapter joinListener = new ListenerAdapter() {
			@Override
			public void onMessageReceived(MessageReceivedEvent e) {
				if (!e.getChannel().getId().equals(channelId)) return;
				if (e.getAuthor().isBot()) return;

				if (!e.getMessage().getContentRaw().equalsIgnoreCase("join")) return;

				String userId = e.getAuthor().getId();

				if (joinedPlayers.contains(userId)) {
					e.getChannel().sendMessage("❌ Du bist bereits beigetreten!").queue();
					return;
				}

				joinedPlayers.add(userId);
				session.addPlayer(userId);

				e.getChannel().sendMessage("✅ <@" + userId + "> ist beigetreten!").queue();

				if (joinedPlayers.size() == 2) {
					// 🔥 WICHTIG: Listener korrekt entfernen
					event.getJDA().removeEventListener(this);

					startGame(session, e);
				}
			}
		};

		session.setJoinListener(joinListener);

		// 🔥 WICHTIG: Listener korrekt registrieren
		event.getJDA().addEventListener(joinListener);
	}

	private void startGame(WavelengthSession session, MessageReceivedEvent event) {
		session.setGameStarted(true);

		var channel = event.getChannel();

		channel.sendMessage(
				"🎉 **Das Spiel beginnt!**\n" +
						"1️⃣ <@" + session.getPlayers().get(0) + ">\n" +
						"2️⃣ <@" + session.getPlayers().get(1) + ">"
		).queue();

		startRound(session, event);
	}

	private void startRound(WavelengthSession session, MessageReceivedEvent event) {
		int secret = 1 + (int) (Math.random() * 10);
		session.setSecretNumber(secret);

		String topic = WavelengthTopics.getRandomTopic();
		session.setTopic(topic);

		session.setWaitingForHint(true);
		session.setWaitingForGuess(false);

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
						"👉 <@" + session.getCurrentDescriber() + "> gib jetzt deinen Hinweis ein!"
		).queue();
	}

	public void endGame(WavelengthSession session, GuildMessageChannel channel) {

		if (session.getPlayers().size() < 2) {
			channel.sendMessage("❌ Das Spiel kann nicht beendet werden – es sind nicht genug Spieler beigetreten.").queue();
			return;
		}

		int points = session.getScores().values().stream().mapToInt(i -> i).sum();
		int rounds = session.getRoundsPlayed();

		String p1 = session.getPlayers().get(0);
		String p2 = session.getPlayers().get(1);

		ScoreEntry entry = new ScoreEntry(p1, p2, points, rounds);

		BestListManager.addEntry(entry);

		channel.sendMessage(
				"🏁 **Spiel beendet!**\n" +
						"Punkte: **" + points + "**\n" +
						"Runden: **" + rounds + "**\n" +
						"Ø: **" + String.format("%.2f", entry.getPointsPerRound()) + "** Punkte pro Runde\n" +
						"📄 Ergebnis gespeichert."
		).queue();

		showBestlist(channel);
	}

	private void showBestlist(GuildMessageChannel channel) {
		channel.sendMessage(BestListManager.formatBestList()).queue();
	}
}
