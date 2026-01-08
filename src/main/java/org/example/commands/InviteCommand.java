package org.example.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class InviteCommand extends ListenerAdapter {

    private static final String INVITE_LINK =
            "https://discord.com/oauth2/authorize?client_id=1417577684778876988&scope=bot&permissions=8";

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("invite")) {
            event.reply("🔗 Lade mich auf deinen Server ein: " + INVITE_LINK)
                    .setEphemeral(true)
                    .queue();
        }
    }
}
