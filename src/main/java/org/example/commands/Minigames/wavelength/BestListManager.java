package org.example.commands.Minigames.wavelength;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BestListManager {

    private static final String FILE_PATH =
            "src/main/java/org/example/commands/Minigames/wavelength/bestlist.txt";

    public static List<ScoreEntry> load() {
        List<ScoreEntry> list = new ArrayList<>();

        try {
            if (!Files.exists(Paths.get(FILE_PATH))) return list;

            for (String line : Files.readAllLines(Paths.get(FILE_PATH))) {
                if (!line.trim().isEmpty()) {
                    list.add(ScoreEntry.fromString(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void save(List<ScoreEntry> list) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (ScoreEntry entry : list) {
                writer.write(entry.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addEntry(ScoreEntry entry) {
        List<ScoreEntry> list = load();
        list.add(entry);

        // 🔥 Sortierung bleibt nach Punkte pro Runde
        list.sort((a, b) -> Double.compare(b.getPointsPerRound(), a.getPointsPerRound()));

        // Optional: Top 5 behalten
        if (list.size() > 5) {
            list = list.subList(0, 5);
        }

        save(list);
    }

    // 🔥 NEU: Bestlist formatieren mit Punkten & Runden
    public static String formatBestList() {
        List<ScoreEntry> list = load();

        if (list.isEmpty()) {
            return "📭 **Noch keine Einträge in der Bestlist!**";
        }

        StringBuilder sb = new StringBuilder("🏆 **Bestlist – Top 5**\n\n");

        int rank = 1;
        for (ScoreEntry entry : list) {
            sb.append(rank++)
                    .append(". <@")
                    .append(entry.getPlayer1())
                    .append("> & <@")
                    .append(entry.getPlayer2())
                    .append(">\n")
                    .append("   ➤ Punkte: **")
                    .append(entry.getPoints())
                    .append("**, Runden: **")
                    .append(entry.getRounds())
                    .append("**, Ø **")
                    .append(String.format("%.2f", entry.getPointsPerRound()))
                    .append("** Punkte/Runde\n")
                    .append("   📅 ")
                    .append(entry.getDate())
                    .append("\n\n");
        }

        return sb.toString();
    }
}
