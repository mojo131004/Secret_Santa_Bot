package org.example.commands.wavelength;

import java.util.HashMap;
import java.util.Map;

public class WavelengthSessionManager {

	// Channel-ID → Session
	private final Map<String, WavelengthSession> sessions = new HashMap<>();

	/**
	 * Erstellt eine neue Session für einen Channel.
	 * Falls bereits eine Session existiert, wird sie überschrieben.
	 */
	public WavelengthSession createSession(String channelId, WavelengthSession.Mode mode) {
		WavelengthSession session = new WavelengthSession(channelId, mode);
		sessions.put(channelId, session);
		return session;
	}

	/**
	 * Holt die Session für einen Channel.
	 */
	public WavelengthSession getSession(String channelId) {
		return sessions.get(channelId);
	}

	/**
	 * Prüft, ob in diesem Channel ein Spiel läuft.
	 */
	public boolean hasSession(String channelId) {
		return sessions.containsKey(channelId);
	}

	/**
	 * Löscht die Session eines Channels.
	 */
	public void removeSession(String channelId) {
		sessions.remove(channelId);
	}

	/**
	 * Löscht alle Sessions (z. B. beim Neustart).
	 */
	public void clearAll() {
		sessions.clear();
	}
}
