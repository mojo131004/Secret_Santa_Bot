package org.example.commands.StoryMode;

public class StoryOption {
    private final String label;
    private final String nextScene;

    public StoryOption(String label, String nextScene) {
        this.label = label;
        this.nextScene = nextScene;
    }

    public String getLabel() {
        return label;
    }

    public String getNextScene() {
        return nextScene;
    }
}

