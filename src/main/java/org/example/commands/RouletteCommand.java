package org.example.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class RouletteCommand extends ListenerAdapter {

	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if (!event.getName().equals("roulette")) return;

		Member member = event.getMember();
		if (member == null || !member.hasPermission(Permission.ADMINISTRATOR)) {
			event.reply("🚫 Du brauchst Administrator-Rechte, um Roulette zu starten!").setEphemeral(true).queue();
			return;
		}

		Guild guild = event.getGuild();

		if (member == null || member.getVoiceState() == null || !member.getVoiceState().inAudioChannel()) {
			event.reply("❌ Du musst in einem Voice-Channel sein!").setEphemeral(true).queue();
			return;
		}

		VoiceChannel channel = (VoiceChannel) member.getVoiceState().getChannel();
		List<Member> members = channel.getMembers();

		if (members.size() < 2) {
			event.reply("❌ Es müssen mindestens zwei Leute im Voice-Channel sein!").setEphemeral(true).queue();
			return;
		}

		Member unlucky = members.get(new Random().nextInt(members.size()));
		event.reply("🎲 Roulette läuft… und **" + unlucky.getEffectiveName() + "** wurde getroffen!").queue();

		guild.mute(unlucky, true).queue();

		scheduler.schedule(() -> {
			guild.mute(unlucky, false).queue();
		}, 10, TimeUnit.SECONDS);
	}
}
