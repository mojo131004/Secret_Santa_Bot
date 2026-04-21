package org.example.commands.Minigames.wordchain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WordChainManager {

    private final Map<Long, WordChainGame> games = new ConcurrentHashMap<>();

    public WordChainGame getOrCreate(long channelId) {
        return games.computeIfAbsent(channelId, WordChainGame::new);
    }

    public WordChainGame get(long channelId) {
        return games.get(channelId);
    }

    public void remove(long channelId) {
        games.remove(channelId);
    }

    public boolean hasGame(long channelId) {
        return games.containsKey(channelId);
    }
}
