package org.example.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.File;
import java.io.FileWriter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StartupMessageExporter extends ListenerAdapter {

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
			887825681231278092L,
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

			dm.sendMessage("📥 **Starte Nachrichten‑Export...**").queue();

			new Thread(() -> {

				try {
					File file = new File("server_export.txt");
					FileWriter writer = new FileWriter(file);

					writer.write("📄 SERVER EXPORT\n");
					writer.write("Server: " + guild.getName() + "\n\n");

					for (GuildMessageChannel channel : guild.getChannels().stream()
							.filter(c -> c instanceof GuildMessageChannel)
							.map(c -> (GuildMessageChannel) c)
							.toList()) {

						if (IGNORE_CHANNELS.contains(channel.getIdLong())) continue;
						if (!channel.canTalk()) continue;

						writer.write("=====================================\n");
						writer.write("# " + channel.getName() + "\n");
						writer.write("=====================================\n\n");

						List<Message> all = loadAllMessages(channel);

						for (Message msg : all) {
							writer.write(
									"[" + msg.getTimeCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "] " +
											msg.getAuthor().getName() + ": " +
											msg.getContentDisplay() + "\n" +
											"Link: " + msg.getJumpUrl() + "\n\n"
							);
						}
					}

					writer.close();

					dm.sendMessage("📤 **Export abgeschlossen!**").addFiles(net.dv8tion.jda.api.utils.FileUpload.fromData(file)).queue();

				} catch (Exception e) {
					e.printStackTrace();
				}

			}).start();
		});
	}

	private List<Message> loadAllMessages(GuildMessageChannel channel) {
		List<Message> all = new ArrayList<>();
		String lastId = null;

		try {
			while (true) {
				List<Message> batch;

				if (lastId == null) {
					batch = channel.getHistory().retrievePast(100).complete();
				} else {
					batch = channel.getHistoryBefore(lastId, 100).complete().getRetrievedHistory();
				}

				if (batch.isEmpty()) break;

				all.addAll(batch);
				lastId = batch.get(batch.size() - 1).getId();

				Thread.sleep(1200); // Anti‑Rate‑Limit
			}
		} catch (Exception ignored) {}

		return all;
	}
}
