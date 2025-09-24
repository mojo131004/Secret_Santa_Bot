package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.example.commands.*;

public class MyBot extends ListenerAdapter {

    public static void main(String[] args) throws Exception {
        String token = "MTQxNzU3NzY4NDc3ODg3Njk4OA.GNQaEK.LW9HIOA4RdLSUvtBd1ZFV9M7KdMI-f5MQK3xJQ";

        JDA jda = JDABuilder.createDefault(token,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(
                        new InitiateSecretSanta(),
                        new PingCommand(),
                        new CountdownCommand(),
                        new InviteCommand(),
                        new QuoteCommand()
                )
                .build()
                .awaitReady();

        jda.updateCommands().addCommands(
                Commands.slash("wichteln", "Wähle bis zu 4 Personen für den Wichtel"),
                Commands.slash("ping", "Antwortet mit der Latenz"),
                Commands.slash("countdown", "Zeigt, wie viele Tage bis Weihnachten übrig sind"),
                Commands.slash("invite", "Zeigt den Invite-Link für den Bot"),
                Commands.slash("quote", "Holt ein zufälliges Zitat aus dem Quotes-Channel")
        ).queue();
    }
}