package org.example.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
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

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("quote")) return;

        TextChannel channel = event.getGuild().getTextChannelById(QUOTES_CHANNEL_ID);
        if (channel == null) {
            event.reply("❌ Quotes-Channel nicht gefunden!").setEphemeral(true).queue();
            return;
        }

        // Falls Cache noch leer ist → laden
        if (cachedQuotes.isEmpty()) {
            event.reply("⏳ Lade Zitate... bitte nochmal versuchen in ein paar Sekunden.").setEphemeral(true).queue();
            loadQuotes(channel);
            return;
        }

        // Zufälliges Zitat aus Cache
        Message randomMsg = cachedQuotes.get(random.nextInt(cachedQuotes.size()));
        event.reply("💬 Zufälliges Zitat:\n\n" + randomMsg.getContentDisplay()).queue();
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
            return true; // true = weiter durchgehen
        }).thenRun(() -> {
            System.out.println("✅ Quotes geladen: " + cachedQuotes.size() + " Nachrichten im Cache.");
        });
    }
}
