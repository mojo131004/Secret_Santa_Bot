package org.example.commands.StoryMode;

import org.example.commands.StoryMode.Saving.StoryScene;
import java.util.List;

public class StoryEngine {

    public StoryScene getScene(String sceneId) {
        switch (sceneId) {

            case "start":
                return new StoryScene(
                        "Du wachst in einem dunklen Raum auf. Was tust du?",
                        List.of(
                                new StoryOption("Lichtschalter suchen", "light"),
                                new StoryOption("Tür öffnen", "door"),
                                new StoryOption("Schreien", "scream")
                        )
                );

            case "scream":
                return new StoryScene(
                        "Du schreist ohne zu wissen was das tun soll und fühlst dich jetzt dumm.",
                        List.of(
                                new StoryOption("Lichtschalter suchen", "light"),
                                new StoryOption("Tür öffnen", "door")
                        )
                );

            case "light":
                return new StoryScene(
                        "Du findest den Schalter. Das Licht geht an. Ein Schlüssel liegt auf dem Tisch.",
                        List.of(
                                new StoryOption("Schlüssel nehmen", "takeKey"),
                                new StoryOption("Ignorieren", "ignore")
                        )
                );

            case "ignore":
                return new StoryScene(
                        "Du ignorierst den schlüssel und überlegst was du als nächstes machst.",
                        List.of(
                                new StoryOption("Schlüssel doch nehmen", "takeKey"),
                                new StoryOption("Weiter umschauen", "lookaround"),
                                new StoryOption("Zur Tür gehen", "door")
                        )
                );

            case "lookaround":
                return new StoryScene(
                        "Du schaust dich um und findest etwas ungewöhnliches was du nicht direkt zuordnen kannst.",
                        List.of(
                                new StoryOption("Schlüssel doch nehmen", "takeKey"),
                                new StoryOption("genauer nachschauen", "lookFurther"),
                                new StoryOption("Zur Tür gehen", "door")
                        )
                );

            case "lookFurther":
                return new StoryScene(
                        "Du schaust dir diese ungewöhnlichkeit genauer an und stellst fest das es ein kleiner schwarzer knopf ist. Willst du diesen drücken?",
                        List.of(
                                new StoryOption("Ja", "clickBlackbutton"),
                                new StoryOption("Nein", "noClickBlackbutton")

                        )
                );

            case "clickBlackbutton":
                return new StoryScene(
                        "Du drückst den Knopf und hörst eine Stimme welche dir 3 Zahlen nennt: 5 2 0. Was nun?",
                        List.of(
                                new StoryOption("Schlüssel doch nehmen", "takeKey"),
                                new StoryOption("Zur Tür gehen", "door")
                        )
                );

            case "noClickBlackbutton":
                return new StoryScene(
                        "Du drückst den Knopf nicht. Was nun?",
                        List.of(
                                new StoryOption("Schlüssel doch nehmen", "takeKey"),
                                new StoryOption("Zur Tür gehen", "door")
                        )
                );


            case "takeKey":
                return new StoryScene(
                        "Du nimmst den Schlüssel und steckst ihn ein.",
                        List.of(
                                new StoryOption("Zur Tür gehen", "doorTwo"),
                                new StoryOption("Warten", "wait")
                        )
                );

            case "wait":
                return new StoryScene(
                        "Du wartest eine Weile. Es passiert nichts.",
                        List.of(
                                new StoryOption("Zur Tür gehen", "door")
                        )
                );

            case "door":
                return new StoryScene(
                        "Die Tür ist verschlossen. Du hörst ein Geräusch dahinter.",
                        List.of(
                                new StoryOption("Anklopfen", "knock"),
                                new StoryOption("Zurückgehen", "start"),
                                new StoryOption("Mit Schlüssel öffnen", "openDoor")
                        )
                );

            case "doorTwo":
                return new StoryScene(
                        "Die Tür ist verschlossen. Du hörst ein Geräusch dahinter.",
                        List.of(
                                new StoryOption("Anklopfen", "knock"),
                                new StoryOption("Mit Schlüssel öffnen", "openDoor")
                        )
                );

            case "doorLocked":
                return new StoryScene(
                        "Du versuchst die Tür zu öffnen, aber sie ist verschlossen. Du brauchst einen Schlüssel.",
                        List.of(
                                new StoryOption("Zurückgehen", "start"),
                                new StoryOption("Lichtschalter suchen", "light")
                        )
                );

            case "knock":
                return new StoryScene(
                        "Du klopfst an der Tür, und wartest darauf das sich was tut",
                        List.of(
                                new StoryOption("Weiter warten", "confused"),
                                new StoryOption("Doch licht anmachen", "light")
                        )
                );


            case "confused":
                return new StoryScene(
                        "Nach etwas warten strahlt ein grelles licht durch die Tür da sich diese nun doch öffnet.",
                        List.of(
                                new StoryOption("Reinschauen", "keyroom")
                        )
                );

            case "openDoor":
                return new StoryScene(
                        "Du steckst den Schlüssel ins Schloss. Die Tür öffnet sich langsam...",
                        List.of(
                                new StoryOption("Eintreten", "keyroom"),
                                new StoryOption("Doch lieber warten", "wait")
                        )
                );

            case "keyroom":
                return new StoryScene(
                        "Du betrittst einen kleinen Raum. Ein alter Mann sitzt dort und starrt dich an.",
                        List.of(
                                new StoryOption("Mit ihm sprechen", "talkToOldMan"),
                                new StoryOption("Den Raum durchsuchen", "searchRoom")
                        )
                );

            case "talkToOldMan":
                return new StoryScene(
                        "Der Mann sagt: 'Du bist nicht der Erste, der hier landet...'.",
                        List.of(
                                new StoryOption("Fragen, was er meint", "askMeaning"),
                                new StoryOption("Ignorieren und gehen", "leaveRoom")
                        )
                );

                case "askMeaning":
                return new StoryScene(
                        "Er flüstert: 'Sie beobachten uns... durch die Wände.'",
                        List.of(
                                new StoryOption("Wände untersuchen", "wallCheck"),
                                new StoryOption("Den Raum durchsuchen", "searchRoom")
                        )
                );

            case "searchRoom":
                return new StoryScene(
                        "Du findest eine alte Karte und ein seltsames Gerät mit einem roten Knopf.",
                        List.of(
                                new StoryOption("Knopf drücken", "pressButton"),
                                new StoryOption("Karte lesen", "readMap")
                        )
                );

            case "wallCheck":
                return new StoryScene(
                        "Du überprüfst die Wände und bemerkst komisch zeichnungen an ihnen.'",
                        List.of(
                                new StoryOption("Frag den alten Mann nach ihnen", "askSymbols"),
                                new StoryOption("Den Raum durchsuchen", "searchRoom")
                        )
                );

            case "askSymbols":
                return new StoryScene(
                        "Du befragst den alten Mann, welcher dir nur als Antwort gibt 'Sie... sie sind überall.'",
                        List.of(
                                new StoryOption("Weiter fragen", "askSymbolsAgain"),
                                new StoryOption("Den Raum durchsuchen", "searchRoom")
                        )
                );

            case "askSymbolsAgain":
                return new StoryScene(
                        "Du befragst den alten Mann erneut, welcher dir diesmal sagt 'Du.. du bist der nächste.'",
                        List.of(
                                new StoryOption("Den Raum durchsuchen", "searchRoom")
                        )
                );

            case "leaveRoom":
                return new StoryScene("Hier endet die Story erstmal",
                        //"Du verlässt den Raum. Der Flur ist leer und still.",
                        List.of(
                               // new StoryOption("Dem Flur folgen", "followHall"),
                                //new StoryOption("Zurück zum Start", "start")
                        )
                );

            case "pressButton":
                return new StoryScene(
                        "Ein lauter Ton ertönt. Die Wände beginnen zu vibrieren.",
                        List.of(
                                new StoryOption("Verstecken", "hide"),
                                new StoryOption("Bleiben und beobachten", "observe")
                        )
                );

            case "hide":
                return new StoryScene(
                        "Du versuchst dich in einem Abschnitt eines Bücherregals zu verstecken wo keine Bücher drin sind.",
                        List.of(
                                new StoryOption("Warten", "waitFromHide"),
                                new StoryOption("Um Hilfe schreien", "libDead")
                        )
                );

            case "libDead":
                return new StoryScene(
                        "Das Schreien hat, was auch immer die Wände zum Vibrieren gebracht hat aufmerksam auf dich gemacht. " +
                                "Du hörst Schritte und merkst wie das Bücherregal langsam umkippt und dich für immer unter sich begräbt.",
                        List.of() // ✅ Keine Optionen = Story-Ende
                );

            case "libDeadTwo":
                return new StoryScene(
                        "Als du dich auf den Weg begeben wolltest dich schnell zu verstecken bemerkst du, dass sich die Wände nur dort " +
                                "vibrieren wo du stehst... du überlegst und stellst fest das es mit dem Schrei aus dem anfangsraum zu tun hat." +
                                "Dir bleibt nichts anderes übrig als dein Schicksal hinzunehmen und mit einem Augenblick von den Wänden zerdrückt zu werden.",
                        List.of() // ✅ Keine Optionen = Story-Ende
                );

            case "observe":
                return new StoryScene(
                        "Der alte Mann bemerkt das du stehen bleibst und dich mit den Konsequenzen deiner Aktionen leben willst." +
                                "Er ist sehr beindruckt von dir und hilft dir irgendwie die Wände abzuwehren. " +
                                "Du bedankst dich und hast nur noch die Karte vor dir",
                        List.of(
                                new StoryOption("Raum trotz anwesender Karte verlassen", "leaveRoom"),
                                new StoryOption("Zurück zur Karte und diese lesen", "readMap")
                        )
                );



            case "waitFromHide":
                return new StoryScene(
                        "Du wartest und wartest im Regal bis das vibrieren der Wände aufhört und begibst dich aus deinem Versteck",
                        List.of(
                                new StoryOption("Raum schnell verlassen", "leaveRoom"),
                                new StoryOption("Zurück zur Karte und diese lesen", "readMap")
                        )
                );

            case "readMap":
                return new StoryScene(
                        "Die Karte zeigt einen Buch im Bücherregal mit Tipps für einen Geheimausgang.",
                        List.of(
                                new StoryOption("Nur zum Regal gehen", "bookshelf"),
                                new StoryOption("Karte nur einstecken", "takeMap")
                        )
                );

            case "bookshelf":
                return new StoryScene(
                        "Du gehst zum Bücherregal und stellst fest das dort tatsächlich an der beschriebenen Stelle ein " +
                                "Buch ist welches dir mehr hinweise zu einem Geheimausgang gibt.",
                        List.of(
                                new StoryOption("Buch nehmen", "takeBook"),
                                new StoryOption("Raum verlassen", "leaveRoom")
                        )
                );

            case "takeBook":
                return new StoryScene(
                        "Du hast dir die Karte genommen.",
                        List.of(
                                new StoryOption("Raum verlassen", "leaveRoom")
                        )
                );

            case "takeMap":
                return new StoryScene(
                        "Du hast dir das Buch genommen.",
                        List.of(
                                new StoryOption("Raum verlassen", "leaveRoom")
                        )
                );

            default:
                return new StoryScene("❌ Ungültige Szene-ID: `" + sceneId + "`", List.of());
        }
    }
}
