package org.example.commands.StoryMode.Saving;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.commands.StoryMode.StoryState;

import java.io.File;
import java.io.IOException;

public class StorySaveManager {
    private final ObjectMapper mapper = new ObjectMapper();
    private final String saveFolder = "saves/";

    public StoryState load(String userId) {
        File file = new File(saveFolder + userId + ".json");
        if (!file.exists()) return new StoryState(userId, "start");

        try {
            return mapper.readValue(file, StoryState.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new StoryState(userId, "start");
        }
    }

    public void save(StoryState state) {
        try {
            File folder = new File(saveFolder);
            if (!folder.exists()) folder.mkdirs();

            mapper.writeValue(new File(saveFolder + state.getUserId() + ".json"), state);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
