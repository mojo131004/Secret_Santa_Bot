package org.example.commands.StoryMode.Saving;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.File;

public class StoryResetCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("storyreset")) return;

        String userId = event.getUser().getId();
        File file = new File("saves/" + userId + ".json");

        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                event.reply("🗑️ Deine Story wurde erfolgreich zurückgesetzt.").setEphemeral(true).queue();
            } else {
                event.reply("❌ Fehler beim Löschen der Datei.").setEphemeral(true).queue();
            }
        } else {
            event.reply("⚠️ Du hast noch keinen gespeicherten Spielstand.").setEphemeral(true).queue();
        }
    }
}
