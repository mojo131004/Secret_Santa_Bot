package org.example.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Random;

public class GayCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String name = event.getName();

        if (!name.equals("gayrate") && !name.equals("kultiviertehurensohn") && !name.equals("gemeinimeini")) return;

        User target = event.getOption("user") != null
                ? event.getOption("user").getAsUser()
                : event.getUser();

        int percentage = new Random().nextInt(100) + 1;

        if (name.equals("gayrate")) {
            event.reply(target.getAsMention() + " ist zu " + percentage + "% GAY 🌈")
                    .queue();
        } else if (name.equals("kultiviertehurensohn")) {
            event.reply(target.getAsMention() + " ist zu " + percentage + "% ein kultivierter Hurensohn 😌")
                    .queue();
        }else {
            event.reply(target.getAsMention() + " ist zu " + percentage + "% ein Gemeini meini 😌")
                    .queue();
        }
    }
}
