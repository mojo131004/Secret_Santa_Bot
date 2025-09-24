package org.example.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuoteCommand extends ListenerAdapter {

    private static final long QUOTES_CHANNEL_ID = 855887631645016115L;
    private static final List<String> KEYWORDS = List.of("Mo:", "Lea:", "Hannah", "Flo:");
    private final Random random = new Random();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("quote")) return;

        TextChannel channel = event.getGuild().getTextChannelById(QUOTES_CHANNEL_ID);
        if (channel == null) {
            event.reply("❌ Quotes-Channel nicht gefunden!").setEphemeral(true).queue();
            return;
        }

        // Neue Methode in JDA v5
        channel.getIterableHistory().takeAsync(100).thenAccept(messages -> {
            List<Message> filtered = new ArrayList<>();

            for (Message msg : messages) {
                String content = msg.getContentDisplay();
                if (KEYWORDS.stream().anyMatch(content::contains)) {
                    filtered.add(msg);
                }
            }

            if (filtered.isEmpty()) {
                event.reply("❌ Keine passenden Zitate gefunden!").setEphemeral(true).queue();
            } else {
                Message randomMsg = filtered.get(random.nextInt(filtered.size()));
                event.reply("💬 Zufälliges Zitat:\n\n" + randomMsg.getContentDisplay())
                        .setEphemeral(false)
                        .queue();
            }
        });
    }
}
