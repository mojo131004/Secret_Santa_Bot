package org.example.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Random;

public class CoinFlipCommand extends ListenerAdapter {
	Random rand = new Random(); // funktioniert problemlos

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if (event.getName().equals("coinflip")) {
			event.reply("Ausgang des Münzwurfs wird ermittelt 🪙")
					.setEphemeral(false)
					.queue();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			int randomNum = rand.nextInt(2);
			String result = (randomNum == 0) ? "Das Ergebnis ist Kopf 🪙🗣️" : "Das Ergebnis ist Zahl 🪙🔢";
			event.getHook().sendMessage(result).setEphemeral(false).queue();
		}
	}
}
