package org.example;

import org.example.commands.StoryMode.Saving.StorySaveManager;
import org.example.commands.StoryMode.StoryState;

public class StoryTest {
    public static void main(String[] args) {
        StorySaveManager manager = new StorySaveManager();
        String userId = "123456789012345678";

        StoryState state = manager.load(userId);
        state.setCurrentScene("keyroom");
        state.getFlags().put("hasKey", true);
        state.getInventory().add("Schlüssel");
        state.getChoices().add("openedDoor");

        manager.save(state);
        System.out.println("✅ Spielstand gespeichert!");
    }
}

