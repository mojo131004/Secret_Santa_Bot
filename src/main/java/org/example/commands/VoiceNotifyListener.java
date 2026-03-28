package org.example.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class VoiceNotifyListener extends ListenerAdapter {

	private static final long OWNER_ID = 486543345355587584L;
	private static final long SERVER_ID = 855818659948003349L;

	// Beim Start: Wer sitzt im VC?
	@Override
	public void onReady(ReadyEvent event) {
		var jda = event.getJDA();
		var guild = jda.getGuildById(SERVER_ID);
		if (guild == null) return;

		var owner = jda.getUserById(OWNER_ID);
		if (owner == null) return;

		StringBuilder msg = new StringBuilder("🔊 **Bot gestartet!**\n\nAktuelle Voice-Channels:\n\n");

		for (VoiceChannel vc : guild.getVoiceChannels()) {
			if (vc.getMembers().isEmpty()) continue;

			msg.append("**")
					.append(vc.getName())
					.append("**:\n");

			for (Member m : vc.getMembers()) {
				msg.append("• ").append(m.getEffectiveName()).append("\n");
			}
			msg.append("\n");
		}

		owner.openPrivateChannel().queue(dm -> dm.sendMessage(msg.toString()).queue());
	}

	// Wenn jemand joint / moved / left
	@Override
	public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
		var jda = event.getJDA();
		var owner = jda.getUserById(OWNER_ID);
		if (owner == null) return;

		Member member = event.getMember();

		// JOIN
		if (event.getChannelJoined() != null && event.getChannelLeft() == null) {
			String msg = "🟢 **" + member.getEffectiveName() + "** ist dem VC **" +
					event.getChannelJoined().getName() + "** beigetreten.";

			owner.openPrivateChannel().queue(dm -> dm.sendMessage(msg).queue());
		}

		// MOVE
		if (event.getChannelJoined() != null && event.getChannelLeft() != null) {
			String msg = "🔄 **" + member.getEffectiveName() + "** ist von **" +
					event.getChannelLeft().getName() + "** nach **" +
					event.getChannelJoined().getName() + "** gewechselt.";

			owner.openPrivateChannel().queue(dm -> dm.sendMessage(msg).queue());
		}

		// LEAVE
		if (event.getChannelLeft() != null && event.getChannelJoined() == null) {
			String msg = "🔴 **" + member.getEffectiveName() + "** hat den VC **" +
					event.getChannelLeft().getName() + "** verlassen.";

			owner.openPrivateChannel().queue(dm -> dm.sendMessage(msg).queue());
		}
	}
}
