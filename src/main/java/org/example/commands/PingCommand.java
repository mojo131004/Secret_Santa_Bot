package org.example.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PingCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("ping")) {
            long time = System.currentTimeMillis();
            event.reply("🏓 Pong!")
                    .setEphemeral(true)
                    .flatMap(v -> event.getHook().editOriginalFormat("🏓 Pong! (%d ms)", System.currentTimeMillis() - time))
                    .queue();
        }
    }
}
