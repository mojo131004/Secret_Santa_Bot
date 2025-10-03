package org.example.commands.StoryMode.Saving;

import org.example.commands.StoryMode.StoryOption;

import java.util.List;

public class StoryScene {
    private final String text;
    private final List<StoryOption> options;

    public StoryScene(String text, List<StoryOption> options) {
        this.text = text;
        this.options = options;
    }

    public String getText() {
        return text;
    }

    public List<StoryOption> getOptions() {
        return options;
    }
}
