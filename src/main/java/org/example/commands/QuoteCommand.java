package org.example.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuoteCommand extends ListenerAdapter {

    // Channel-ID anpassen
    private static final long QUOTES_CHANNEL_ID = 855887631645016115L;

    // Deine Keywords
    private static final List<String> KEYWORDS = List.of("Mo", "Lea", "Hannah", "Flo", "Ty");

    // Cache für gefundene Zitate
    private final List<Message> cachedQuotes = new ArrayList<>();
    private final Random random = new Random();

    /**
     * Wird beim Bot-Start ausgeführt, sobald die Guild bereit ist.
     * Lädt automatisch die Zitate in den Cache.
     */
    @Override
    public void onGuildReady(GuildReadyEvent event) {
        TextChannel channel = event.getGuild().getTextChannelById(QUOTES_CHANNEL_ID);
        if (channel != null) {
            loadQuotes(channel);
        }
    }

    /**
     * Slash-Command für /quote
     */
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("quote")) return;

        if (cachedQuotes.isEmpty()) {
            event.reply("⏳ Zitate werden noch geladen... bitte versuch es gleich nochmal.").setEphemeral(true).queue();
            return;
        }

        Message randomMsg = cachedQuotes.get(random.nextInt(cachedQuotes.size()));
        String quoteText = randomMsg.getContentDisplay();
        String quoteLink = randomMsg.getJumpUrl();

        event.reply("💬 **Zufälliges Zitat:**\n\n" +
                quoteText + "\n\n" +
                "🔗 [Zur Originalnachricht](" + quoteLink + ")").queue();
    }

    /**
     * Lädt alle Nachrichten aus dem Channel und filtert nach KEYWORDS.
     * Ergebnis wird in cachedQuotes gespeichert.
     */
    private void loadQuotes(TextChannel channel) {
        channel.getIterableHistory().cache(false).forEachAsync(msg -> {
            String content = msg.getContentDisplay();
            if (KEYWORDS.stream().anyMatch(content::contains)) {
                cachedQuotes.add(msg);
            }
            return true;
        }).thenRun(() -> {
            System.out.println("✅ Quotes geladen: " + cachedQuotes.size() + " Nachrichten im Cache.");
        });
    }
}
