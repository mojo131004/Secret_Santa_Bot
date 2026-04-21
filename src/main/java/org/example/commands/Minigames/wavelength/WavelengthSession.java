package org.example.commands.Minigames.wavelength;

import java.util.*;

public class WavelengthSession {

	public enum Mode {
		TWO_PLAYERS
	}

	private final String channelId;
	private final Mode mode;

	private final List<String> players = new ArrayList<>();
	private final Map<String, Integer> scores = new HashMap<>();

	private boolean gameStarted = false;
	private int secretNumber;
	private String topic;

	private int roundsPlayed = 0;

	// Listener für Join-Phase
	private Object joinListener;

	// Phasensteuerung
	private boolean waitingForHint = false;
	private boolean waitingForGuess = false;

	public WavelengthSession(String channelId, Mode mode) {
		this.channelId = channelId;
		this.mode = mode;
	}

	public String getChannelId() {
		return channelId;
	}

	public Mode getMode() {
		return mode;
	}

	public List<String> getPlayers() {
		return players;
	}

	public void addPlayer(String userId) {
		players.add(userId);
		scores.put(userId, 0);
	}

	public Map<String, Integer> getScores() {
		return scores;
	}

	public void addPoints(String userId, int points) {
		scores.put(userId, scores.getOrDefault(userId, 0) + points);
	}

	public int getRoundsPlayed() {
		return roundsPlayed;
	}

	public void incrementRounds() {
		roundsPlayed++;
	}

	public void setSecretNumber(int number) {
		this.secretNumber = number;
	}

	public int getSecretNumber() {
		return secretNumber;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTopic() {
		return topic;
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public void setGameStarted(boolean started) {
		this.gameStarted = started;
	}

	public String getCurrentDescriber() {
		return players.get(0);
	}

	public String getCurrentGuesser() {
		return players.get(1);
	}

	public void setJoinListener(Object listener) {
		this.joinListener = listener;
	}

	public Object getJoinListener() {
		return joinListener;
	}

	// Hinweisphase
	public boolean isWaitingForHint() {
		return waitingForHint;
	}

	public void setWaitingForHint(boolean waitingForHint) {
		this.waitingForHint = waitingForHint;
	}

	// Guessphase
	public boolean isWaitingForGuess() {
		return waitingForGuess;
	}

	public void setWaitingForGuess(boolean waitingForGuess) {
		this.waitingForGuess = waitingForGuess;
	}
}
