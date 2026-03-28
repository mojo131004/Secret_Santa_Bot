package org.example.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class StartupChannelAnalyzer extends ListenerAdapter {

	private static final long OWNER_ID = 486543345355587584L;
	private static final long SERVER_ID = 855818659948003349L;

	private static final Set<Long> IGNORE_CHANNELS = Set.of(
			1197596900615008287L,
			1477688444712124487L,
			855818660757241878L,
			855887631645016115L,
			1281405433508270142L,
			855818723754246154L,
			874382201217753108L,
			1291152376748572682L,
			1288991322396430456L,
			1162432393664266251L,
			1063579935542878329L,
			1162494981219549194L,
			1364700884378058883L,
			1422218331053625405L,
			855818764175540244L,
			855818743744299038L,
			1149020360478564352L,
			1204850992735195166L,
			1142623054464294942L,
			1216489354269032458L,
			855818660757241879L,
			1371935447424434227L,
			1288987793569218611L,
			1004449173757759658L,
			1292186020737191987L,
			1288990211753377912L,
			873603768674041906L,
			1209538841657221150L,
			874837746622025739L,
			902328561187307591L
	);

	@Override
	public void onReady(ReadyEvent event) {

		var jda = event.getJDA();
		var guild = jda.getGuildById(SERVER_ID);
		if (guild == null) return;

		User owner = jda.getUserById(OWNER_ID);
		if (owner == null) return;

		owner.openPrivateChannel().queue(dm -> {

			dm.sendMessage("📊 **Channel‑Analyse gestartet**").queue();

			StringBuilder chunk = new StringBuilder();

			for (GuildChannel channel : guild.getChannels()) {

				boolean ignored = IGNORE_CHANNELS.contains(channel.getIdLong());

				boolean canView = guild.getSelfMember().hasPermission(channel, Permission.VIEW_CHANNEL);
				boolean canHistory = guild.getSelfMember().hasPermission(channel, Permission.MESSAGE_HISTORY);
				boolean canWrite = guild.getSelfMember().hasPermission(channel, Permission.MESSAGE_SEND);

				// "Read Messages" = VIEW_CHANNEL + MESSAGE_HISTORY
				boolean canRead = canView && canHistory;

				// Snowflake → Creation Timestamp
				long discordEpoch = 1420070400000L;
				long timestamp = (channel.getIdLong() >> 22) + discordEpoch;

				String created = Instant.ofEpochMilli(timestamp)
						.atZone(ZoneId.systemDefault())
						.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));

				String type = channel.getType().name();

				String line =
						"----------------------------------------\n" +
								"**#" + channel.getName() + "**  \n" +
								"• Typ: **" + type + "**\n" +
								"• ID: `" + channel.getId() + "`\n" +
								"• Erstellt am: **" + created + "**\n" +
								"• Ignoriert: " + (ignored ? "🟥 JA" : "🟩 NEIN") + "\n" +
								"• View Channel: " + (canView ? "🟩" : "🟥") + "\n" +
								"• Read Messages: " + (canRead ? "🟩" : "🟥") + "\n" +
								"• Read History: " + (canHistory ? "🟩" : "🟥") + "\n" +
								"• Write Messages: " + (canWrite ? "🟩" : "🟥") + "\n\n";

				if (chunk.length() + line.length() > 1800) {
					dm.sendMessage(chunk.toString()).queue();
					chunk = new StringBuilder();
				}

				chunk.append(line);
			}

			if (!chunk.isEmpty()) {
				dm.sendMessage(chunk.toString()).queue();
			}

			dm.sendMessage("✅ **Analyse abgeschlossen**").queue();
		});
	}
}
