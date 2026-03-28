package org.example.commands.Quotes;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuessQuote extends ListenerAdapter {

	private static final long QUOTE_CHANNEL_ID = 855887631645016115L;
	private static final List<String> KEYWORDS = List.of("Mo", "Lea", "Hannah", "Flo", "Ty");

	private final List<QuoteEntry> cachedQuotes = new ArrayList<>();
	private final Map<String, QuoteEntry> activeSessions = new HashMap<>();

	public static class QuoteEntry {
		private final Message message;
		private final Set<String> speakers;

		public QuoteEntry(Message message, Set<String> speakers) {
			this.message = message;
			this.speakers = speakers;
		}

		public Message getMessage() { return message; }
		public Set<String> getSpeakers() { return speakers; }
	}

	@Override
	public void onGuildReady(GuildReadyEvent event) {

		TextChannel channel = event.getGuild().getTextChannelById(QUOTE_CHANNEL_ID);
		if (channel == null) return;

		System.out.println("📥 Lade Quotes aus Channel: " + channel.getName());

		QuoteCache.load(channel, quotes -> {
			cachedQuotes.clear();
			quotes.forEach(this::cacheQuote);

			System.out.println("✅ GuessQuote: " + cachedQuotes.size() + " Quotes geladen.");
		});
	}

	public void cacheQuote(Message msg) {
		String content = msg.getContentDisplay();
		Set<String> speakers = extractSpeakers(content);
		if (!speakers.isEmpty()) cachedQuotes.add(new QuoteEntry(msg, speakers));
	}

	public Set<String> extractSpeakers(String content) {
		Set<String> speakers = new HashSet<>();
		String[] lines = content.split("\n");
		Pattern p = Pattern.compile("^([A-Za-zÄÖÜäöüß]+):\\s*");

		for (String line : lines) {
			Matcher m = p.matcher(line);
			if (m.find() && KEYWORDS.contains(m.group(1))) speakers.add(m.group(1));
		}

		if (speakers.isEmpty()) {
			Pattern p2 = Pattern.compile("\\b(" + String.join("|", KEYWORDS) + ")\\b");
			Matcher m2 = p2.matcher(content);
			while (m2.find()) speakers.add(m2.group(1));
		}

		return speakers;
	}

	private String replaceSpeakerNames(String content) {
		String[] lines = content.split("\n");
		StringBuilder cleaned = new StringBuilder();

		for (String line : lines) {
			line = line.replaceFirst("^([A-Za-zÄÖÜäöüß]+):\\s*", "Name: ");
			cleaned.append(line).append("\n");
		}
		return cleaned.toString().trim();
	}

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if (!event.getName().equals("guessquote")) return;

		if (cachedQuotes.isEmpty()) {
			event.reply("⏳ Quotes werden noch geladen...").setEphemeral(true).queue();
			return;
		}

		Random r = new Random();
		QuoteEntry entry = cachedQuotes.get(r.nextInt(cachedQuotes.size()));
		String quoteText = replaceSpeakerNames(entry.getMessage().getContentDisplay());
		Set<String> speakers = entry.getSpeakers();

		Set<String> options = new HashSet<>(speakers);
		while (options.size() < 4) options.add(KEYWORDS.get(r.nextInt(KEYWORDS.size())));

		List<String> shuffled = new ArrayList<>(options);
		Collections.shuffle(shuffled);

		List<Button> buttons = shuffled.stream()
				.map(name -> Button.primary("guessquote_" + name, name))
				.toList();

		EmbedBuilder eb = new EmbedBuilder()
				.setTitle("Guess the Quote")
				.setDescription("Wer hat diesen Quote gesagt?\n\n```" + quoteText + "```")
				.setColor(Color.ORANGE);

		event.replyEmbeds(eb.build())
				.addActionRow(buttons)
				.queue(hook -> hook.retrieveOriginal().queue(original ->
						activeSessions.put(original.getId(), entry)
				));
	}

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
		String quoteLink = entry.getMessage().getJumpUrl();

		if (speakers.contains(clicked)) {
			event.reply("✅ **Richtig!**\nSprecher: **" + String.join(", ", speakers) +
					"**\n\n[🔗 Zum Original-Quote](" + quoteLink + ")").queue();
		} else {
			event.reply("❌ **Falsch!**\nRichtige Sprecher: **" + String.join(", ", speakers) +
					"**\n\n[🔗 Zum Original-Quote](" + quoteLink + ")").queue();
		}

		activeSessions.remove(event.getMessageId());
	}

	public static void registerCommand(JDA jda) {
		jda.updateCommands().addCommands(
				Commands.slash("guessquote", "Errate, wer den Quote gesagt hat")
		).queue();
	}
}
