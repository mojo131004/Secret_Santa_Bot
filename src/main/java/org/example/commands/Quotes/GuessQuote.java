package org.example.commands.Quotes;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuessQuote extends ListenerAdapter {

	// Fester Quote-Channel
	private static final long QUOTE_CHANNEL_ID = 855887631645016115L;

	// Liste aller möglichen Sprecher
	private static final List<String> KEYWORDS = List.of("Mo", "Lea", "Hannah", "Flo");

	// Alle Quotes
	private final List<QuoteEntry> cachedQuotes = new ArrayList<>();

	// Aktive Spiele (Message-ID → QuoteEntry)
	private final Map<String, QuoteEntry> activeSessions = new HashMap<>();


	// -------------------------------
	// QuoteEntry Klasse
	// -------------------------------
	public static class QuoteEntry {
		private final Message message;
		private final Set<String> speakers;

		public QuoteEntry(Message message, Set<String> speakers) {
			this.message = message;
			this.speakers = speakers;
		}

		public Message getMessage() {
			return message;
		}

		public Set<String> getSpeakers() {
			return speakers;
		}
	}


	// -------------------------------
	// Quotes beim Start laden
	// -------------------------------
	public void loadQuotes(JDA jda) {
		TextChannel channel = jda.getTextChannelById(QUOTE_CHANNEL_ID);
		if (channel == null) {
			System.out.println("❌ Quote-Channel nicht gefunden!");
			return;
		}

		System.out.println("📥 Lade Quotes aus Channel: " + channel.getName());

		channel.getIterableHistory().forEach(msg -> cacheQuote(msg));

		System.out.println("✅ Quotes geladen: " + cachedQuotes.size());
	}


	// -------------------------------
	// Sprecher-Erkennung
	// -------------------------------
	public Set<String> extractSpeakers(String content) {
		Set<String> speakers = new HashSet<>();

		String[] lines = content.split("\n");
		Pattern p = Pattern.compile("^([A-Za-zÄÖÜäöüß]+):\\s*");

		// 1. "Name:" Format erkennen
		for (String line : lines) {
			Matcher m = p.matcher(line);
			if (m.find()) {
				String name = m.group(1);
				if (KEYWORDS.contains(name)) {
					speakers.add(name);
				}
			}
		}

		// 2. Falls keine "Name:"-Zeilen → KEYWORDS im Text suchen
		if (speakers.isEmpty()) {
			Pattern p2 = Pattern.compile("\\b(" + String.join("|", KEYWORDS) + ")\\b");
			Matcher m2 = p2.matcher(content);
			while (m2.find()) {
				speakers.add(m2.group(1));
			}
		}

		return speakers;
	}


	// -------------------------------
	// Quote cachen
	// -------------------------------
	public void cacheQuote(Message msg) {
		String content = msg.getContentDisplay();
		Set<String> speakers = extractSpeakers(content);

		if (!speakers.isEmpty()) {
			cachedQuotes.add(new QuoteEntry(msg, speakers));
		}
	}
	// -------------------------------
	// Sprecher im Text anonymisieren
	// -------------------------------
	private String replaceSpeakerNames(String content) {
		String[] lines = content.split("\n");
		StringBuilder cleaned = new StringBuilder();
		Pattern p = Pattern.compile("^(\\w+):\\s*");
		for (String line : lines) { Matcher m = p.matcher(line);
		if (m.find()) {
			// Ersetzt "Flo:" oder "Lea:" durch "Name:"
			line = line.replaceFirst("^(\\w+):\\s*", "Name: ");
		}
		cleaned.append(line).append("\n"); } return cleaned.toString().trim();
	}


	// -------------------------------
	// Slash Command Handler
	// -------------------------------
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if (!event.getName().equals("guessquote")) return;

		if (cachedQuotes.isEmpty()) {
			event.reply("Es sind keine Quotes verfügbar.").setEphemeral(true).queue();
			return;
		}

		Random r = new Random();
		QuoteEntry entry = cachedQuotes.get(r.nextInt(cachedQuotes.size()));
		String quoteText = replaceSpeakerNames(entry.getMessage().getContentDisplay());
		Set<String> speakers = entry.getSpeakers();

		// Antwortmöglichkeiten generieren
		Set<String> options = new HashSet<>(speakers);

		while (options.size() < 4) {
			String randomName = KEYWORDS.get(r.nextInt(KEYWORDS.size()));
			options.add(randomName);
		}

		List<String> shuffled = new ArrayList<>(options);
		Collections.shuffle(shuffled);

		// Buttons bauen
		List<Button> buttons = shuffled.stream()
				.map(name -> Button.primary("guessquote_" + name, name))
				.toList();

		// Embed
		EmbedBuilder eb = new EmbedBuilder()
				.setTitle("Guess the Quote")
				.setDescription("Wer hat diesen Quote gesagt?\n\n```" + quoteText + "```")
				.setColor(Color.ORANGE);

		// WICHTIG: richtige Message-ID speichern
		event.replyEmbeds(eb.build())
				.addActionRow(buttons)
				.queue(hook -> hook.retrieveOriginal().queue(original ->
						activeSessions.put(original.getId(), entry)
				));
	}


	// -------------------------------
	// Button Handler
	// -------------------------------
	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		if (!event.getComponentId().startsWith("guessquote_")) return;

		String clicked = event.getComponentId().replace("guessquote_", "");

		QuoteEntry entry = activeSessions.get(event.getMessageId());
		if (entry == null) {
			event.reply("Dieses Spiel ist abgelaufen.").setEphemeral(true).queue();
			return;
		}

		Set<String> speakers = entry.getSpeakers();

		String quoteLink =
				"https://discord.com/channels/" +
						entry.getMessage().getGuild().getId() + "/" +
						entry.getMessage().getChannel().getId() + "/" +
						entry.getMessage().getId();

		if (speakers.contains(clicked)) {
			event.reply("✅ **Richtig!**\nSprecher: **" + String.join(", ", speakers) + "**" +
					"\n\n[🔗 Zum Original-Quote](" + quoteLink + ")").queue();
		} else {
			event.reply("❌ **Falsch!**\nRichtige Sprecher: **" + String.join(", ", speakers) + "**" +
					"\n\n[🔗 Zum Original-Quote](" + quoteLink + ")").queue();
		}


		activeSessions.remove(event.getMessageId());
	}


	// -------------------------------
	// Command Registration
	// -------------------------------
	public static void registerCommand(JDA jda) {
		jda.updateCommands().addCommands(
				Commands.slash("guessquote", "Errate, wer den Quote gesagt hat")
		).queue();
	}


	// -------------------------------
	// Auto-Load Quotes on Ready
	// -------------------------------
	@Override
	public void onReady(ReadyEvent event) {
		loadQuotes(event.getJDA());
	}
}
