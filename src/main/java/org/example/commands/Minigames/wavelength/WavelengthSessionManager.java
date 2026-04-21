package org.example.commands.Minigames.wavelength;

import java.util.HashMap;
import java.util.Map;

public class WavelengthSessionManager {

	private final Map<String, WavelengthSession> sessions = new HashMap<>();

	public boolean hasSession(String channelId) {
		return sessions.containsKey(channelId);
	}

	public WavelengthSession createSession(String channelId, WavelengthSession.Mode mode) {
		WavelengthSession session = new WavelengthSession(channelId, mode);
		sessions.put(channelId, session);
		return session;
	}

	public WavelengthSession getSession(String channelId) {
		return sessions.get(channelId);
	}

	public void removeSession(String channelId) {
		sessions.remove(channelId);
	}
}
