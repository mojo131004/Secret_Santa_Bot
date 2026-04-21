package org.example.commands.Minigames.wavelength;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScoreEntry {

    private final String player1;
    private final String player2;
    private final int points;
    private final int rounds;
    private final double pointsPerRound;
    private final String date;

    public ScoreEntry(String player1, String player2, int points, int rounds) {
        this.player1 = player1;
        this.player2 = player2;
        this.points = points;
        this.rounds = rounds;
        this.pointsPerRound = rounds == 0 ? 0 : (double) points / rounds;

        this.date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    public String getPlayer1() { return player1; }
    public String getPlayer2() { return player2; }
    public int getPoints() { return points; }
    public int getRounds() { return rounds; }
    public double getPointsPerRound() { return pointsPerRound; }
    public String getDate() { return date; }

    @Override
    public String toString() {
        return player1 + ";" + player2 + ";" + points + ";" + rounds + ";" +
                String.format("%.2f", pointsPerRound) + ";" + date;
    }

    public static ScoreEntry fromString(String line) {
        String[] p = line.split(";");
        return new ScoreEntry(
                p[0], p[1],
                Integer.parseInt(p[2]),
                Integer.parseInt(p[3])
        );
    }
}
