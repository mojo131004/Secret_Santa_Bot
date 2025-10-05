package org.example.commands.StoryMode;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.example.commands.StoryMode.Saving.StorySaveManager;
import org.example.commands.StoryMode.Saving.StoryScene;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("storymode")) return;

        event.deferReply().queue();

        try {
            String userId = event.getUser().getId();
            StorySaveManager saveManager = new StorySaveManager();
            StoryState state = saveManager.load(userId);

            StoryEngine engine = new StoryEngine();
            StoryScene scene = engine.getScene(state.getCurrentScene());

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("📖 Story Mode")
                    .setDescription(scene.getText());

            List<Button> buttons = new ArrayList<>();
            for (StoryOption option : scene.getOptions()) {
                buttons.add(Button.primary("story_choose:" + option.getNextScene(), option.getLabel()));
            }

            if (!buttons.isEmpty()) {
                buttons.add(Button.danger("story_exit", "🛑 Speichern & Beenden"));

                List<ActionRow> rows = new ArrayList<>();
                for (int i = 0; i < buttons.size(); i += 5) {
                    int end = Math.min(i + 5, buttons.size());
                    rows.add(ActionRow.of(buttons.subList(i, end)));
                }

                event.getHook().sendMessageEmbeds(embed.build())
                        .setComponents(rows)
                        .queue();
            } else {
                event.getHook().sendMessageEmbeds(embed.build())
                        .addActionRow(Button.success("story_restart", "🔁 Neustart"))
                        .queue();
            }

        } catch (Exception e) {
            event.getHook().sendMessage("❌ Fehler beim Laden der Story.")
                    .setEphemeral(true)
                    .queue();
            e.printStackTrace();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String id = event.getComponentId();
        String userId = event.getUser().getId();
        StorySaveManager saveManager = new StorySaveManager();

        if (id.equals("story_exit")) {
            StoryState state = saveManager.load(userId);
            saveManager.save(state);

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("📖 Story beendet")
                    .setDescription("Dein Fortschritt wurde gespeichert. Bis bald!");

            Button disabledExit = Button.danger("story_exit", "🛑 Speichern & Beenden").asDisabled();

            event.editMessageEmbeds(embed.build())
                    .setActionRow(disabledExit)
                    .queue();
            return;
        }

        if (id.equals("story_restart")) {
            // ✅ Datei löschen
            File file = new File("saves/" + userId + ".json");
            if (file.exists()) file.delete();

            // ✅ Neuen Spielstand erzeugen
            StoryState state = new StoryState(userId, "start");
            saveManager.save(state);

            StoryEngine engine = new StoryEngine();
            StoryScene scene = engine.getScene("start");

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("📖 Story Mode")
                    .setDescription(scene.getText());

            List<Button> buttons = new ArrayList<>();
            for (StoryOption option : scene.getOptions()) {
                buttons.add(Button.primary("story_choose:" + option.getNextScene(), option.getLabel()));
            }

            buttons.add(Button.danger("story_exit", "🛑 Speichern & Beenden"));

            event.editMessageEmbeds(embed.build())
                    .setActionRow(buttons.toArray(new Button[0]))
                    .queue();
            return;
        }

        if (!id.startsWith("story_choose:")) return;

        try {
            String nextScene = id.split(":")[1];
            StoryState state = saveManager.load(userId);

            // ✅ Schreien als versteckte Choice speichern
            if (nextScene.equals("scream") && !state.getChoices().contains("scream")) {
                state.getChoices().add("scream");
            }

            // ✅ Schlüssel aufnehmen
            if (nextScene.equals("takeKey")) {
                state.addItem("FirstDoorKey");
            }

            if (nextScene.equals("clickBlackButton") && !state.getChoices().contains("clickBlackButton")) {
                state.getChoices().add("knowsCombination");
            }

            // ✅ Tür öffnen nur mit Schlüssel
            if (nextScene.equals("openDoor") && !state.hasItem("FirstDoorKey")) {
                nextScene = "doorLocked";
            }

            if (nextScene.equals("hide") && state.getChoices().contains("scream")) {
                nextScene = "libDeadTwo";
            }

            if (nextScene.equals("takeBook")) {
                state.addItem("escapeBook");
            }

            if (nextScene.equals("takeMap")) {
                state.addItem("map");
            }

            if (nextScene.equals("stareTwo") && !state.getChoices().contains("stareTwo")) {
                state.getChoices().add("happy");
            }

            if (nextScene.equals("closeDoor") && !state.getChoices().contains("happy")) {
                nextScene = "closeDoorFailed";
            }

            if (nextScene.equals("screamOldMan") && !state.getChoices().contains("screamOldMan") ||
                    nextScene.equals("askOldMan") && !state.getChoices().contains("askOldMan")) {
                state.getChoices().add("scared");
            }

            if (nextScene.equals("openDoor") && state.getChoices().contains("happy") && !state.getChoices().contains("scared")) {
                nextScene = "followHallFurtherHappy";
            }

            if (nextScene.equals("openDoor") && state.getChoices().contains("scared")) {
                nextScene = "followHallFurtherHappyScared";
            }

            if (nextScene.equals("openDoor") && state.getChoices().contains("scream")) {
                nextScene = "followHallFurtherScream";
            }

            if (nextScene.equals("thirdRight") && !state.getChoices().contains("knowsCombination")) {
                nextScene = "cheater";
            }

            state.setCurrentScene(nextScene);
            saveManager.save(state);

            StoryEngine engine = new StoryEngine();
            StoryScene scene = engine.getScene(nextScene);

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("📖 Story Mode")
                    .setDescription(scene.getText());

            List<Button> buttons = new ArrayList<>();
            for (StoryOption option : scene.getOptions()) {
                buttons.add(Button.primary("story_choose:" + option.getNextScene(), option.getLabel()));
            }

            if (!buttons.isEmpty()) {
                buttons.add(Button.danger("story_exit", "🛑 Speichern & Beenden"));

                List<ActionRow> rows = new ArrayList<>();
                for (int i = 0; i < buttons.size(); i += 5) {
                    int end = Math.min(i + 5, buttons.size());
                    rows.add(ActionRow.of(buttons.subList(i, end)));
                }

                event.getHook().sendMessageEmbeds(embed.build())
                        .setComponents(rows)
                        .queue();
            } else {
                event.editMessageEmbeds(embed.build())
                        .setActionRow(Button.success("story_restart", "🔁 Neustart"))
                        .queue();
            }

        } catch (Exception e) {
            event.reply("❌ Fehler beim Wechseln der Szene.")
                    .setEphemeral(true)
                    .queue();
            e.printStackTrace();
        }
    }
}
