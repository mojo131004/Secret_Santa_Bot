package org.example.commands.MessageCounter;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageTrackerService extends ListenerAdapter {

    private final Map<String, Integer> messageCount = new HashMap<>();

    public Map<String, Integer> getMessageCount() {
        return messageCount;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String userId = event.getAuthor().getId();
        messageCount.merge(userId, 1, Integer::sum);
    }

    public void initializeHistory(JDA jda) {
        System.out.println("📥 Initialisiere Nachrichtenhistorie...");

        for (Guild guild : jda.getGuilds()) {
            for (TextChannel channel : guild.getTextChannels()) {
                try {
                    int totalFetched = 0;
                    Message lastMessage = null;

                    while (totalFetched < 50000) { // z. B. bis zu 5000 Nachrichten
                        List<Message> messages;

                        if (lastMessage == null) {
                            messages = channel.getHistory().retrievePast(100).complete();
                        } else {
                            int remaining = 50000 - totalFetched;
                            int fetchSize = Math.min(remaining, 100);
                            MessageHistory history = channel.getHistoryBefore(lastMessage.getId(), fetchSize).complete();
                            messages = history.getRetrievedHistory();
                        }


                        if (messages.isEmpty()) break;

                        for (Message message : messages) {
                            if (message.getAuthor().isBot()) continue;
                            String userId = message.getAuthor().getId();
                            messageCount.merge(userId, 1, Integer::sum);
                        }

                        lastMessage = messages.get(messages.size() - 1);
                        totalFetched += messages.size();
                    }

                    System.out.println("📄 Gelesen aus #" + channel.getName() + ": " + totalFetched + " Nachrichten");

                } catch (Exception e) {
                    System.out.println("⚠️ Fehler beim Abrufen von Nachrichten aus Kanal " + channel.getName());
                    e.printStackTrace();
                }
            }
        }

        System.out.println("✅ Initialisierung abgeschlossen. Nachrichten gezählt: " + messageCount.size());
    }

}
