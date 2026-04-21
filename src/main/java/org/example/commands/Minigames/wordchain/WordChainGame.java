package org.example.commands.Minigames.wordchain;

import net.dv8tion.jda.api.entities.Member;

import java.util.*;
import java.util.concurrent.*;

public class WordChainGame {

    private final long channelId;
    private final Map<Long, Integer> lives = new HashMap<>();
    private final List<Long> players = new ArrayList<>();
    private final Set<String> usedWords = new HashSet<>();

    private String currentWord = null;
    private char requiredStartChar = 0;
    private int currentPlayerIndex = 0;
    private boolean started = false;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> currentTimer;

    public WordChainGame(long channelId) {
        this.channelId = channelId;
    }

    public long getChannelId() {
        return channelId;
    }

    public boolean isStarted() {
        return started;
    }

    public List<Long> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public int getLives(long playerId) {
        return lives.getOrDefault(playerId, 0);
    }

    public Long getCurrentPlayerId() {
        if (players.isEmpty()) return null;
        return players.get(currentPlayerIndex);
    }

    public char getRequiredStartChar() {
        return requiredStartChar;
    }

    public boolean addPlayer(Member member) {
        long id = member.getIdLong();
        if (players.contains(id)) return false;
        players.add(id);
        lives.put(id, 3);
        return true;
    }

    public boolean canStart() {
        return players.size() >= 2;
    }

    public void startGame() {
        started = true;
        currentPlayerIndex = 0;
        usedWords.clear();
        currentWord = null;
        requiredStartChar = 0;
    }

    public WordPlayResult playFirstWord(long playerId, String word) {
        if (!started) return WordPlayResult.NOT_STARTED;
        if (!Objects.equals(getCurrentPlayerId(), playerId)) return WordPlayResult.NOT_YOUR_TURN;

        word = normalize(word);
        if (word.length() < 2) return WordPlayResult.INVALID_WORD;

        currentWord = word;
        usedWords.add(word);
        requiredStartChar = word.charAt(word.length() - 1);

        advanceTurn();
        return WordPlayResult.OK;
    }

    public WordPlayResult playNextWord(long playerId, String word) {
        if (!started) return WordPlayResult.NOT_STARTED;
        if (currentWord == null) return WordPlayResult.NO_FIRST_WORD;
        if (!Objects.equals(getCurrentPlayerId(), playerId)) return WordPlayResult.NOT_YOUR_TURN;

        word = normalize(word);
        if (word.length() < 2) return WordPlayResult.INVALID_WORD;
        if (usedWords.contains(word)) return WordPlayResult.ALREADY_USED;
        if (word.charAt(0) != requiredStartChar) return WordPlayResult.WRONG_START_CHAR;

        currentWord = word;
        usedWords.add(word);
        requiredStartChar = word.charAt(word.length() - 1);

        advanceTurn();
        return WordPlayResult.OK;
    }

    private void advanceTurn() {
        if (players.isEmpty()) return;

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public void startTurnTimer(Runnable onTimeout) {
        if (currentTimer != null && !currentTimer.isDone()) {
            currentTimer.cancel(true);
        }

        currentTimer = scheduler.schedule(onTimeout, 15, TimeUnit.SECONDS);
    }

    public void loseLife(long playerId) {
        lives.put(playerId, lives.get(playerId) - 1);
    }

    public boolean isDead(long playerId) {
        return lives.get(playerId) <= 0;
    }

    public void removePlayer(long playerId) {
        players.remove(playerId);
        lives.remove(playerId);
        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0;
        }
    }

    private String normalize(String word) {
        return word.trim().toLowerCase(Locale.ROOT);
    }

    public enum WordPlayResult {
        OK,
        NOT_STARTED,
        NOT_YOUR_TURN,
        INVALID_WORD,
        ALREADY_USED,
        WRONG_START_CHAR,
        NO_FIRST_WORD
    }
}
