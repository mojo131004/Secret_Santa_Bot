package org.example.commands.wavelength;

import java.util.*;

public class WavelengthSession {

	public enum Mode {
		TWO_PLAYERS,
		THREE_PLAYERS,
		FOUR_PLAYERS,
		FOUR_PLAYERS_TEAMS
	}

	private final String channelId;
	private final Mode mode;

	private final List<String> players = new ArrayList<>();
	private final Map<String, Integer> scores = new HashMap<>();

	private final Map<String, String> teams = new HashMap<>(); // playerId -> teamName

	private int currentDescriberIndex = 0;
	private int currentGuesserIndex = 1;

	private int secretNumber;
	private String topic;

	private boolean waitingForGuess = false;
	private boolean gameStarted = false;

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

	public Map<String, Integer> getScores() {
		return scores;
	}

	public Map<String, String> getTeams() {
		return teams;
	}

	public int getCurrentDescriberIndex() {
		return currentDescriberIndex;
	}

	public int getCurrentGuesserIndex() {
		return currentGuesserIndex;
	}

	public String getCurrentDescriber() {
		return players.get(currentDescriberIndex);
	}

	public String getCurrentGuesser() {
		return players.get(currentGuesserIndex);
	}

	public int getSecretNumber() {
		return secretNumber;
	}

	public void setSecretNumber(int secretNumber) {
		this.secretNumber = secretNumber;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public boolean isWaitingForGuess() {
		return waitingForGuess;
	}

	public void setWaitingForGuess(boolean waitingForGuess) {
		this.waitingForGuess = waitingForGuess;
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}

	public void addPlayer(String userId) {
		players.add(userId);
		scores.put(userId, 0);
	}

	public void addTeamMember(String userId, String teamName) {
		teams.put(userId, teamName);
		scores.putIfAbsent(teamName, 0);
	}

	public void addPoints(String userId, int points) {
		scores.put(userId, scores.getOrDefault(userId, 0) + points);
	}

	public void addTeamPoints(String teamName, int points) {
		scores.put(teamName, scores.getOrDefault(teamName, 0) + points);
	}

	public void nextTurn() {
		currentDescriberIndex = (currentDescriberIndex + 1) % players.size();
		currentGuesserIndex = (currentDescriberIndex + 1) % players.size();
	}
}
