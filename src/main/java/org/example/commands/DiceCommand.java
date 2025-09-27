package org.example.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Random;

public class DiceCommand extends ListenerAdapter {
    Random rand = new Random(); // funktioniert problemlos

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("würfel")) {
            event.reply("Der Würfel wird gewürfelt.")
                    .setEphemeral(false)
                    .queue();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            int beschraenkung = event.getOption("sides").getAsInt();

            int randomNum = rand.nextInt(beschraenkung);
            String result = "Das Ergebnis ist "+ randomNum;
            event.getHook().sendMessage(result).setEphemeral(false).queue();
        }
    }
}
