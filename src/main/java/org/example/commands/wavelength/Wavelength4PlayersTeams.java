package org.example.commands.wavelength;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashSet;
import java.util.Set;

public class Wavelength4PlayersTeams extends ListenerAdapter {

	private final WavelengthSessionManager sessionManager;

	public Wavelength4PlayersTeams(WavelengthSessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if (!event.getName().equals("wavelength4playersteams")) return;

		String channelId = event.getChannel().getId();

		// Prüfen, ob bereits ein Spiel läuft
		if (sessionManager.hasSession(channelId)) {
			event.reply("❌ In diesem Channel läuft bereits ein Wavelength-Spiel!").setEphemeral(true).queue();
			return;
		}

		// Neue Session erstellen
		WavelengthSession session = sessionManager.createSession(
				channelId,
				WavelengthSession.Mode.FOUR_PLAYERS_TEAMS
		);

		event.reply(
				"🎮 **Wavelength – 4 Spieler (2v2 Teams)**\n\n" +
						"Es werden **4 Spieler** benötigt.\n" +
						"Schreibt **`join`** in den Chat, um mitzuspielen.\n\n" +
						"⏳ Das Spiel startet automatisch, sobald 4 Spieler beigetreten sind."
		).queue();

		collectPlayers(event, session);
	}

	private void collectPlayers(SlashCommandInteractionEvent event, WavelengthSession session) {
		String channelId = session.getChannelId();
		var channel = event.getChannel();

		Set<String> joinedPlayers = new HashSet<>();

		channel.sendMessage("👉 Wer möchte mitspielen? Schreibe **`join`**!").queue();

		// Listener für Join-Nachrichten
		channel.getJDA().addEventListener(new ListenerAdapter() {
			@Override
			public void onMessageReceived(net.dv8tion.jda.api.events.message.MessageReceivedEvent e) {
				if (!e.getChannel().getId().equals(channelId)) return;
				if (e.getAuthor().isBot()) return;

				String content = e.getMessage().getContentRaw().trim().toLowerCase();

				if (!content.equals("join")) return;

				String userId = e.getAuthor().getId();

				if (joinedPlayers.contains(userId)) {
					e.getChannel().sendMessage("❌ Du bist bereits beigetreten!").queue();
					return;
				}

				joinedPlayers.add(userId);
				session.addPlayer(userId);

				e.getChannel().sendMessage("✅ <@" + userId + "> ist beigetreten!").queue();

				// Wenn 4 Spieler da sind → Spiel starten
				if (joinedPlayers.size() == 4) {
					channel.getJDA().removeEventListener(this);
					assignTeams(session, e);
					startGame(session, e);
				}
			}
		});
	}

	private void assignTeams(WavelengthSession session, net.dv8tion.jda.api.events.message.MessageReceivedEvent event) {
		var players = session.getPlayers();

		// Team A: Spieler 1 + Spieler 3
		session.addTeamMember(players.get(0), "Team A");
		session.addTeamMember(players.get(2), "Team A");

		// Team B: Spieler 2 + Spieler 4
		session.addTeamMember(players.get(1), "Team B");
		session.addTeamMember(players.get(3), "Team B");

		event.getChannel().sendMessage(
				"🔵 **Team A:** <@" + players.get(0) + ">, <@" + players.get(2) + ">\n" +
						"🔴 **Team B:** <@" + players.get(1) + ">, <@" + players.get(3) + ">"
		).queue();
	}

	private void startGame(WavelengthSession session, net.dv8tion.jda.api.events.message.MessageReceivedEvent event) {
		session.setGameStarted(true);

		var channel = event.getChannel();

		channel.sendMessage(
				"🎉 **Das Spiel beginnt!**\n\n" +
						"Teams wurden automatisch zugewiesen.\n" +
						"Die erste Runde startet jetzt!"
		).queue();

		startFirstRound(session, event);
	}

	private void startFirstRound(WavelengthSession session, net.dv8tion.jda.api.events.message.MessageReceivedEvent event) {
		int secret = 1 + (int)(Math.random() * 10);
		session.setSecretNumber(secret);

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
				"🧠 **Runde 1**\n" +
						"Beschreiber: <@" + session.getCurrentDescriber() + ">\n" +
						"Guesser: <@" + session.getCurrentGuesser() + ">\n" +
						"Thema: **" + session.getTopic() + "**\n\n" +
						"👉 <@" + session.getCurrentDescriber() + "> beschreibt jetzt etwas.\n" +
						"👉 <@" + session.getCurrentGuesser() + "> rät danach eine Zahl zwischen **1 und 10**."
		).queue();

		session.setWaitingForGuess(true);
	}
}
