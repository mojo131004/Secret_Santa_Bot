package org.example.commands;

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.*;
import net.dv8tion.jda.api.entities.*;
import org.example.commands.audioplayer.AudioPlayerSendHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand extends ListenerAdapter {

    private final AudioPlayerManager playerManager;
    private final AudioPlayer player;

    public PlayCommand() {
        playerManager = new DefaultAudioPlayerManager();

        // YouTube Source Manager mit Timeout-Konfiguration
        YoutubeAudioSourceManager youtube = new YoutubeAudioSourceManager(true);
        youtube.getHttpInterfaceManager().setRequestConfigCallback(config ->
                config.setSocketTimeout(10000) // 10 Sekunden
                        .setConnectTimeout(10000)
                        .setConnectionRequestTimeout(10000)
        );

        playerManager.registerSourceManager(youtube);
        player = playerManager.createPlayer();
    }


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("play_youtube")) return;

        event.deferReply().queue(); // ← WICHTIG!

        String url = event.getOption("url").getAsString();
        Member member = event.getMember();

        if (member == null || member.getVoiceState() == null || !member.getVoiceState().inAudioChannel()) {
            event.getHook().editOriginal("Du musst in einem Voice-Channel sein!").queue();
            return;
        }

        Guild guild = event.getGuild();
        AudioManager audioManager = guild.getAudioManager();
        audioManager.openAudioConnection(member.getVoiceState().getChannel());
        audioManager.setSendingHandler(new AudioPlayerSendHandler(player));

        playerManager.loadItemOrdered(player, url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                player.playTrack(track);
                event.getHook().editOriginal("🎶 Spiele jetzt: **" + track.getInfo().title + "**").queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack() != null
                        ? playlist.getSelectedTrack()
                        : playlist.getTracks().get(0);
                player.playTrack(firstTrack);
                event.getHook().editOriginal("🎶 Spiele jetzt: **" + firstTrack.getInfo().title + "**").queue();
            }

            @Override
            public void noMatches() {
                event.getHook().editOriginal("❌ Kein Video gefunden!").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                event.getHook().editOriginal("⚠️ Fehler beim Laden: " + exception.getMessage()).queue();
            }
        });
    }


    public static net.dv8tion.jda.api.interactions.commands.build.CommandData getCommandData() {
        return Commands.slash("play", "Spielt ein YouTube-Video im Voice-Channel")
                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.STRING, "url", "YouTube-URL", true);
    }
}
