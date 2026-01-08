package org.example.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CountdownCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("countdown")) {
            long daysLeft = ChronoUnit.DAYS.between(
                    LocalDate.now(),
                    LocalDate.of(LocalDate.now().getYear(), 12, 24)
            );

            if (daysLeft < 0) {
                daysLeft = ChronoUnit.DAYS.between(
                        LocalDate.now(),
                        LocalDate.of(LocalDate.now().getYear() + 1, 12, 24)
                );
            }

            event.reply("🎄 Noch **" + daysLeft + "** Tage bis Weihnachten!").setEphemeral(true).queue();
        }
    }
}
