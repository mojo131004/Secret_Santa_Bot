package org.example.commands.roasts;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RoastCommand extends ListenerAdapter {
    private final RoastService roastService;

    public RoastCommand(RoastService roastService) {
        this.roastService = roastService;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("roastsomeone")) return;

        event.deferReply().queue();

        User target = event.getOption("user").getAsUser();
        String roast = roastService.getRandomRoast();

        event.getHook().sendMessage("🔥 " + target.getAsMention() + ", " + roast).queue();
    }
}
