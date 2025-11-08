package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.example.commands.*;
import org.example.commands.MessageCounter.ActivityHeatmapCommand;
import org.example.commands.StoryMode.PlayCommand;
import org.example.commands.StoryMode.Saving.StoryResetCommand;
import org.example.commands.roasts.RoastCommand;
import org.example.commands.roasts.RoastService;

public class MyBot extends ListenerAdapter {

    public static void main(String[] args) throws Exception {
        String token = "MTQxNzU3NzY4NDc3ODg3Njk4OA.GNQaEK.LW9HIOA4RdLSUvtBd1ZFV9M7KdMI-f5MQK3xJQ";

        //MessageTrackerService tracker = new MessageTrackerService();

        RoastService roastService = new RoastService();

        JDA jda = JDABuilder.createDefault(token,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_VOICE_STATES)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(
                        new PingCommand(),
                        new CountdownCommand(),
                        new InviteCommand(),
                        new QuoteCommand(),
                        new DiceCommand(),
                        new CoinFlipCommand(),
                        new GuessNumberCommand(),
                        //new TopTalkersCommand(tracker),
                        new RouletteCommand(),
                        new RoastCommand(roastService),
                        new ActivityHeatmapCommand(),
                        new PlayCommand(),
                        new StoryResetCommand(),
                        new GayCommand()

                )
                .build()
                .awaitReady();

        //tracker.initializeHistory(jda);

        jda.updateCommands().addCommands(
                Commands.slash("ping", "Antwortet mit der Latenz"),
                Commands.slash("countdown", "Zeigt, wie viele Tage bis Weihnachten übrig sind"),
                Commands.slash("invite", "Zeigt den Invite-Link für den Bot"),
                Commands.slash("quote", "Holt ein zufälliges Zitat aus dem Quotes-Channel"),
                Commands.slash("würfel", "Wirft einen würfel mit einer selbstbestimmten seitenzahl")
                         .addOption(OptionType.INTEGER, "sides", "Würfel Seiten", true),
                Commands.slash("coinflip", "Wirf eine Münze für schwierige entscheidungen"),
                Commands.slash("guess", "Rate eine Zahl zwischen 1 und 100"),
                Commands.slash("roulette", "Jemand zufälliges im VC wird für 10 sekunden stummgeschaltet"),
                Commands.slash("roastsomeone", "Roaste einen User wie eine (vegane) Wurst aufm Grill")
                        .addOption(OptionType.USER, "user", "Wen willst du roasten?", true),
                //Commands.slash("toptalkers", "wer hat die meisten nachrichten geschrieben?"),
                Commands.slash("emojiwars", "Welcher emoji wird hier am meisten benutzt?"),
                Commands.slash("activityheatmap","Um wie viel Uhr ist der Server am aktivsten?"),
                Commands.slash("storymode", "Starte dein Abenteuer"),
                Commands.slash("storyreset", "Setzt deinen Story-Spielstand zurück"),
                Commands.slash("gayrate", "Wie Gay ist ein Nutzer?")
                        .addOption(OptionType.USER, "user", "Wen willst du testen?", false),
                Commands.slash("kultiviertehurensohn", "Wie sehr bist du ein kultivierter Hurensohn?")
                        .addOption(OptionType.USER, "user", "Wen willst du testen?", false),
                Commands.slash("gemeinimeini", "Wie sehr bist du ein Gemeini meini?")
                        .addOption(OptionType.USER, "user", "Wen willst du testen?", false)

        ).queue();
    }
}