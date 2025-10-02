package org.example.commands.roasts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoastService {
    private final List<String> roasts = new ArrayList<>();

    public RoastService() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("roasts.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                roasts.add(line);
            }
        } catch (IOException | NullPointerException e) {
            System.out.println("⚠️ Fehler beim Laden der Roasts: " + e.getMessage());
        }
    }

        public String getRandomRoast() {
        if (roasts.isEmpty()) return "Du bist so langweilig, selbst dein Fehlercode ist 404.";
        return roasts.get(new Random().nextInt(roasts.size()));
    }
}
