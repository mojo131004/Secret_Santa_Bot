package org.example.commands.Minigames.wordchain;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class WordChainCommand extends ListenerAdapter {

    private final WordChainManager manager = new WordChainManager();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String msg = event.getMessage().getContentRaw();
        long channelId = event.getChannel().getIdLong();

        if (!msg.startsWith("!word")) return;

        String[] parts = msg.split("\\s+", 3);
        if (parts.length < 2) {
            event.getChannel().sendMessage("Benutzung: !word start|join|begin|play|stop").queue();
            return;
        }

        String sub = parts[1].toLowerCase();

        switch (sub) {
            case "start" -> start(event, channelId);
            case "join" -> join(event, channelId);
            case "begin" -> begin(event, channelId, parts);
            case "play" -> play(event, channelId, parts);
            case "stop" -> stop(event, channelId);
        }
    }

    private void start(MessageReceivedEvent e, long channelId) {
        WordChainGame game = manager.getOrCreate(channelId);
        if (game.isStarted()) {
            e.getChannel().sendMessage("Hier läuft bereits ein Spiel.").queue();
            return;
        }

        game.addPlayer(e.getMember());
        e.getChannel().sendMessage("WordChain gestartet! Nutzt `!word join` zum Mitspielen.").queue();
    }

    private void join(MessageReceivedEvent e, long channelId) {
        WordChainGame game = manager.get(channelId);
        if (game == null) {
            e.getChannel().sendMessage("Kein aktives Spiel. Starte eins mit `!word start`.").queue();
            return;
        }

        if (!game.addPlayer(e.getMember())) {
            e.getChannel().sendMessage("Du bist bereits im Spiel.").queue();
            return;
        }

        e.getChannel().sendMessage(e.getAuthor().getAsMention() + " ist beigetreten!").queue();
    }

    private void begin(MessageReceivedEvent e, long channelId, String[] parts) {
        WordChainGame game = manager.get(channelId);
        if (game == null) {
            e.getChannel().sendMessage("Kein Spiel aktiv.").queue();
            return;
        }

        if (!game.canStart()) {
            e.getChannel().sendMessage("Mindestens 2 Spieler nötig.").queue();
            return;
        }

        if (!game.isStarted()) {
            game.startGame();
        }

        if (parts.length < 3) {
            e.getChannel().sendMessage("Benutzung: !word begin <wort>").queue();
            return;
        }

        String word = parts[2];
        var result = game.playFirstWord(e.getAuthor().getIdLong(), word);

        if (result != WordChainGame.WordPlayResult.OK) {
            e.getChannel().sendMessage("Ungültiges erstes Wort.").queue();
            return;
        }

        startTimer(e, game);

        e.getChannel().sendMessage("Erstes Wort: **" + word + "**\n" +
                "Nächster Spieler: <@" + game.getCurrentPlayerId() + ">\n" +
                "Startbuchstabe: `" + game.getRequiredStartChar() + "`").queue();
    }

    private void play(MessageReceivedEvent e, long channelId, String[] parts) {
        WordChainGame game = manager.get(channelId);
        if (game == null) {
            e.getChannel().sendMessage("Kein Spiel aktiv.").queue();
            return;
        }

        if (parts.length < 3) {
            e.getChannel().sendMessage("Benutzung: !word play <wort>").queue();
            return;
        }

        String word = parts[2];
        var result = game.playNextWord(e.getAuthor().getIdLong(), word);

        switch (result) {
            case OK -> {
                startTimer(e, game);
                e.getChannel().sendMessage("**" + word + "** akzeptiert!\n" +
                        "Nächster Spieler: <@" + game.getCurrentPlayerId() + ">\n" +
                        "Startbuchstabe: `" + game.getRequiredStartChar() + "`").queue();
            }
            case WRONG_START_CHAR -> e.getChannel().sendMessage("Falscher Anfangsbuchstabe!").queue();
            case ALREADY_USED -> e.getChannel().sendMessage("Dieses Wort wurde bereits benutzt!").queue();
            case NOT_YOUR_TURN -> e.getChannel().sendMessage("Du bist nicht dran!").queue();
            default -> e.getChannel().sendMessage("Ungültiges Wort.").queue();
        }
    }

    private void startTimer(MessageReceivedEvent e, WordChainGame game) {
        long playerId = game.getCurrentPlayerId();

        game.startTurnTimer(() -> {
            int lives = game.getLives(playerId) - 1;
            game.loseLife(playerId);

            e.getChannel().sendMessage("<@" + playerId + "> hat zu lange gebraucht! ❤️ -" + 1 +
                    " (verbleibend: " + lives + ")").queue();

            if (game.isDead(playerId)) {
                game.removePlayer(playerId);
                e.getChannel().sendMessage("<@" + playerId + "> ist ausgeschieden!").queue();
            }

            if (game.getPlayers().size() == 1) {
                long winner = game.getPlayers().get(0);
                e.getChannel().sendMessage("🏆 <@" + winner + "> hat gewonnen!").queue();
                manager.remove(game.getChannelId());
            }
        });
    }

    private void stop(MessageReceivedEvent e, long channelId) {
        if (!manager.hasGame(channelId)) {
            e.getChannel().sendMessage("Kein Spiel aktiv.").queue();
            return;
        }

        manager.remove(channelId);
        e.getChannel().sendMessage("Spiel beendet.").queue();
    }
}
