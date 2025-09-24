package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.example.commands.InitiateSecretSanta;

public class MyBot extends ListenerAdapter {

    public static void main(String[] args) throws Exception {
        String token = "MTQxNzU3NzY4NDc3ODg3Njk4OA.GNQaEK.LW9HIOA4RdLSUvtBd1ZFV9M7KdMI-f5MQK3xJQ";
        String guildId = "807515353798017024";


        JDA jda = JDABuilder.createDefault(token,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new InitiateSecretSanta())  // <-- Listener-Klasse
                .build()
                .awaitReady();

        Guild guild = jda.getGuildById(guildId);
        if (guild != null) {
            guild.updateCommands().addCommands(
                    Commands.slash("Wichteln", "Wähle bis zu 4 Personen für den Wichtel")
            ).queue();
        }
    }
}