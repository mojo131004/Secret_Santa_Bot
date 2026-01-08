package org.example.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InitiateSecretSanta extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("wichteln")) {
            event.reply("Netter Versuch aber bis zu Weihnachten ist es noch zu lange")
                    .setEphemeral(true)
                    .queue();
            //event.deferReply(true).queue();

            /*event.getGuild().loadMembers().onSuccess(members -> {
                List<SelectOption> options = new ArrayList<>();
                for (Member m : members) {
                    if (!m.getUser().isBot()) {
                        options.add(SelectOption.of(m.getEffectiveName(), m.getId()));
                    }
                }

                if (options.size() < 2) {
                    event.getHook().editOriginal("❌ Es sind nicht genug Mitglieder verfügbar, um zu wichteln.").queue();
                    return;
                }

                StringSelectMenu menu = StringSelectMenu.create("santa_select")
                        .setPlaceholder("Wähle bis zu 4 Personen")
                        .setMinValues(2)
                        .setMaxValues(Math.min(4, options.size()))
                        .addOptions(options)
                        .build();

                event.getHook().editOriginal("Wähle deine Wichtel aus:").setActionRow(menu).queue();
            });
        }
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("santa_select")) {
            event.deferReply(true).queue();

            List<String> selectedIds = event.getValues();
            List<Member> selectedMembers = new ArrayList<>();
            List<String> failedLookups = new ArrayList<>();

            final int total = selectedIds.size();
            final int[] completed = {0};

            for (String id : selectedIds) {
                event.getGuild().retrieveMemberById(id).queue(
                        member -> {
                            selectedMembers.add(member);
                            completed[0]++;
                            if (completed[0] == total) {
                                processWichteln(event, selectedMembers);
                            }
                        },
                        error -> {
                            failedLookups.add(id);
                            completed[0]++;
                            if (completed[0] == total) {
                                event.getHook().editOriginal("❌ Fehler: Einige Mitglieder konnten nicht geladen werden.").queue();
                            }
                        }
                );
            }
        }
    }

    private void sendWichtelDM(Member giver, Member receiver, List<String> failedDMs) {
        String message = "🎁 Dein Wichtel geht dieses Jahr an **" + receiver.getEffectiveName() +
                "**. Das Budget beträgt 40€. Viel Spaß und frohe Weihnachten! 🎅";

        giver.getUser().openPrivateChannel()
                .flatMap(channel -> channel.sendMessage(message))
                .queue(
                        success -> {},
                        failure -> {
                            System.out.println("Konnte DM nicht senden an: " + giver.getEffectiveName());
                            failedDMs.add(giver.getEffectiveName());
                        }
                );
    }

    private List<Member> generateDerangement(List<Member> members) {
        List<Member> result = new ArrayList<>(members);
        boolean valid;

        do {
            Collections.shuffle(result);
            valid = true;
            for (int i = 0; i < members.size(); i++) {
                if (result.get(i).getId().equals(members.get(i).getId())) {
                    valid = false;
                    break;
                }
            }
        } while (!valid);

        return result;
    }

    private void processWichteln(StringSelectInteractionEvent event, List<Member> selectedMembers) {
        List<String> failedDMs = new ArrayList<>();

        if (selectedMembers.size() < 2) {
            event.getHook().editOriginal("❌ Fehler bei der Auswahl. Stelle sicher, dass alle Mitglieder verfügbar sind.").queue();
            return;
        }

        if (selectedMembers.size() == 2) {
            sendWichtelDM(selectedMembers.get(0), selectedMembers.get(1), failedDMs);
            sendWichtelDM(selectedMembers.get(1), selectedMembers.get(0), failedDMs);
        } else {
            List<Member> shuffled = generateDerangement(selectedMembers);
            for (int i = 0; i < selectedMembers.size(); i++) {
                sendWichtelDM(selectedMembers.get(i), shuffled.get(i), failedDMs);
            }
        }

        String confirmation = "✅ Die Wichtel-Zuweisungen wurden per DM verschickt!";
        if (!failedDMs.isEmpty()) {
            confirmation += "\n⚠️ Folgende Personen konnten keine DM empfangen: " +
                    String.join(", ", failedDMs) +
                    ". Bitte überprüft eure Datenschutzeinstellungen.";
        }

        event.getHook().editOriginal(confirmation).queue();*/
        }
    }
}
