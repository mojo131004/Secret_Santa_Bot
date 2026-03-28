package org.example.commands.Quotes;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
import java.util.Random;

public class QuoteCommand extends ListenerAdapter {

    private final Random random = new Random();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("quote")) return;

        List<Message> quotes = QuoteCache.QUOTES;

        if (quotes.isEmpty()) {
            event.reply("⏳ Zitate werden noch geladen...").setEphemeral(true).queue();
            return;
        }

        Message msg = quotes.get(random.nextInt(quotes.size()));

        event.reply("💬 **Zufälliges Zitat:**\n\n" +
                msg.getContentDisplay() + "\n\n" +
                "🔗 [Zur Originalnachricht](" + msg.getJumpUrl() + ")").queue();
    }
}
