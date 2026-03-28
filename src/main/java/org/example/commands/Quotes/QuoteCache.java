package org.example.commands.Quotes;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class QuoteCache {

	public static final List<Message> QUOTES = new ArrayList<>();
	private static boolean loading = false;
	private static boolean loaded = false;

	public static void load(TextChannel channel, Consumer<List<Message>> onFinish) {
		if (loaded) {
			onFinish.accept(QUOTES);
			return;
		}
		if (loading) return;

		loading = true;
		QUOTES.clear();

		loadBatch(channel, null, onFinish);
	}

	private static void loadBatch(TextChannel channel, String lastId, Consumer<List<Message>> onFinish) {

		if (lastId == null) {
			channel.getHistory().retrievePast(100).queue(msgs -> {

				if (msgs.isEmpty()) {
					loaded = true;
					onFinish.accept(QUOTES);
					return;
				}

				QUOTES.addAll(msgs);

				String newLastId = msgs.get(msgs.size() - 1).getId();
				loadBatch(channel, newLastId, onFinish);
			});
			return;
		}

		channel.getHistoryBefore(lastId, 100).queue(history -> {
			var msgs = history.getRetrievedHistory();

			if (msgs.isEmpty()) {
				loaded = true;
				onFinish.accept(QUOTES);
				return;
			}

			QUOTES.addAll(msgs);

			String newLastId = msgs.get(msgs.size() - 1).getId();
			loadBatch(channel, newLastId, onFinish);
		});
	}
}
