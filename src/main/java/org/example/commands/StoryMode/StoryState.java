package org.example.commands.StoryMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoryState {
    private String userId;
    private String currentScene;
    private Map<String, Boolean> flags = new HashMap<>();
    private List<String> inventory = new ArrayList<>();
    private List<String> choices = new ArrayList<>();

    public StoryState() {}

    public StoryState(String userId, String currentScene) {
        this.userId = userId;
        this.currentScene = currentScene;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(String currentScene) {
        this.currentScene = currentScene;
    }

    public Map<String, Boolean> getFlags() {
        return flags;
    }

    public void setFlags(Map<String, Boolean> flags) {
        this.flags = flags;
    }

    public List<String> getInventory() {
        return inventory;
    }

    public void setInventory(List<String> inventory) {
        this.inventory = inventory;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    // ✅ Inventar-Methoden
    public void addItem(String item) {
        if (!inventory.contains(item)) {
            inventory.add(item);
        }
    }

    public boolean hasItem(String item) {
        return inventory.contains(item);
    }
}
