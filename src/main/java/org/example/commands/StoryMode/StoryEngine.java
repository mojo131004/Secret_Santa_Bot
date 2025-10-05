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
                                new StoryOption("Zur Tür gehen", "doorThree")
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

            case "doorThree":
                return new StoryScene(
                        "Die Tür ist verschlossen. Du hörst ein Geräusch dahinter.",
                        List.of(
                                new StoryOption("Anklopfen", "knock"),
                                new StoryOption("Schlüssel doch nehmen", "takeKey")
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
                                new StoryOption("Den Raum durchsuchen", "searchRoom"),
                                new StoryOption("Zurückstarren", "stare")
                        )
                );

            case "stare":
                return new StoryScene(
                        "Der alte man start dich nun noch mehr an",
                        List.of(
                                new StoryOption("Mit ihm sprechen", "talkToOldMan"),
                                new StoryOption("Den Raum durchsuchen", "searchRoom"),
                                new StoryOption("Ebenfalls krasser zurückstarren", "stareTwo")
                        )
                );

            case "stareTwo":
                return new StoryScene(
                        "'Ich mag dich' sagt der alte man nun auf einmal und du fühlst dich besser",
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
                                "Du hörst Schritte und merkst wie das Bücherregal langsam umkippt und dich für immer unter sich begräbt. \n\nBad End1",
                        List.of() // ✅ Keine Optionen = Story-Ende
                );

            case "libDeadTwo":
                return new StoryScene(
                        "Als du dich auf den Weg begeben wolltest dich schnell zu verstecken bemerkst du, dass sich die Wände nur dort " +
                                "vibrieren wo du stehst... du überlegst und stellst fest das es mit dem Schrei aus dem anfangsraum zu tun hat." +
                                "Dir bleibt nichts anderes übrig als dein Schicksal hinzunehmen und mit einem Augenblick von den Wänden zerdrückt zu werden. " +
                                "\n\nBad End2",
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
                        "Du hast dir das Buch genommen.",
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

            case "leaveRoom":
                return new StoryScene("Du verlässt den Raum. Der Flur ist leer und still.",
                        List.of(
                                new StoryOption("Dem Flur folgen", "followHall"),
                                new StoryOption("Tür schließen", "closeDoor")
                        )
                );

            case "closeDoor":
                return new StoryScene("Du schließt die Tür hinter und bist nun wieder alleine. Auf einmal hörst du" +
                        " schmerzerfülltes schreien hinter der Tür... die Tür lässt sich nicht mehr öffnen.",
                        List.of(
                                new StoryOption("Dem Flur folgen", "followHall"),
                                new StoryOption("Nach dem alten Mann rufen", "screamOldMann")
                        )
                );

            case "screamOldMann":
                return new StoryScene("Du rufst zur Tür nach dem alten Mann... keine Antwort",
                        List.of(
                                new StoryOption("Dem Flur folgen", "followHall")
                        )
                );

            case "closeDoorFailed":
                return new StoryScene("Du möchtest grade die Tür schließen doch der alte Mann verhindert das " +
                        "'So einfach lasse ich mich nicht fertig machen. Ich will deinen Fortschritt schon sehen.'",
                        List.of(
                                new StoryOption("Dem Flur folgen", "followHall"),
                                new StoryOption("Nachfragen was er meint", "askOldMan")
                        )
                );

            case "askOldMan":
                return new StoryScene("Du fragst den alten Mann war er meint worauf er nur erwidert, 'Sie verfolgen dich... so wie ich'",
                        List.of(
                                new StoryOption("Dem Flur folgen", "followHall")
                        )
                );

            case "followHall":
                return new StoryScene("Der Gang ist lang, viel zu lang. Wände aus grauem Stein, nackt und still." +
                        "Jeder Schritt hallt, als wärst du der Einzige, der je hier gegangen ist.",
                        List.of(
                                new StoryOption("Dem Flur weiter folgen", "followHallFurther")
                        )
                );

            case "followHallFurther":
                return new StoryScene("Du gehst voran. Die Luft ist trocken, staubig – so alt, dass du fast schmecken kannst," +
                        " wie lange hier niemand war. Nur dann findest du eine Aufgabe, entweder du gibst einen Code ein," +
                        " oder du musst eine Challenge schaffen",
                        List.of(
                                new StoryOption("Code eingeben", "enterCode"),
                                new StoryOption("Challenge versuchen", "challenge")
                        )
                );

            case "followHallFurtherHappy":
                return new StoryScene("Etwas in dir bleibt ruhig. Der Flur ist still, doch er bedrängt dich nicht – er beobachtet nur." +
                        "Irgendwo in der Ferne flackert ein warmes Licht. Vielleicht das Ende. Vielleicht nur Hoffnung.",
                        List.of(
                                new StoryOption("Nach dem Licht schauen", "lookAfterLight")
                        )
                );

            case "followHallFurtherHappyScared":
                return new StoryScene("Der Flur wirkt eng und dunkel. Du willst dich nicht umdrehen. " +
                        "Etwas zischt durch die Luft – ein Flüstern, zu nah, um Einbildung zu sein.",
                        List.of(
                                new StoryOption("Rennen", "run"),
                                new StoryOption("Zuhören", "listenToVoices")
                        )
                );

            case "followHallFurtherScream":
                return new StoryScene("Dein Schrei aus dem ersten Raum hallt auf einmal auf dich zurück Als du weitergehst, siehst du an der Wand " +
                        "deine eigene Silhouette – doch sie bewegt sich nicht im selben Takt.",
                        List.of(
                                new StoryOption("Rennen", "run"),
                                new StoryOption("Ignorieren und weitergehen", "followHallFurther")
                        )
                );

            case "followHallFurtherHappyScaredScream":
                return new StoryScene("Du fühlst dich gleichzeitig am Leben und verloren. Jeder Schritt ist ein Zittern, " +
                        "doch du gehst weiter, weil du endlich spürst, dass du existierst.",
                        List.of(
                                new StoryOption("Nochmal schreien", "screamTwice"),
                                new StoryOption("Mit Stolz weiter gehen", "followHallFurther")
                        )
                );

            case "screamTwice":
                return new StoryScene("Du Schreist dir nochmal alle sorgen raus und fühlst dich lebendig.",
                        List.of(
                                new StoryOption("Mit Stolz weiter gehen", "followHallFurther")
                        )
                );

            case "followHallFurtherHappyScream":
                return new StoryScene("Es erscheint ein Lächeln auf deinen Lippen. Die Luft vibriert, als würde der ganze Ort deinen " +
                        "Laut mittragen. Etwas in dir hat begriffen, dass der Schrei aus dem ersten Raum kein Zeichen von Schwäche ist, " +
                        "sondern von Leben. Du drehst dich einmal im Kreis, lachst, atmest – und zum ersten Mal fühlst du dich nicht " +
                        "beobachtet, sondern willkommen.",
                        List.of(
                                new StoryOption("weiter gehen", "followHallFurther")
                        )
                );

            case "enterCode":
                return new StoryScene("Bitte gib die erste Ziffer des Code ein, wenn du diesen denn weißt",
                        List.of(
                                new StoryOption("0", "wrongNumber"),
                                new StoryOption("1", "wrongNumber"),
                                new StoryOption("2", "wrongNumber"),
                                new StoryOption("3", "wrongNumber"),
                                new StoryOption("4", "wrongNumber"),
                                new StoryOption("5", "firstRight"),
                                new StoryOption("6", "wrongNumber"),
                                new StoryOption("7", "wrongNumber"),
                                new StoryOption("8", "wrongNumber"),
                                new StoryOption("9", "wrongNumber")
                        )
                );

            case "firstRight":
                return new StoryScene("Bitte gib die zweite Nummer des Code ein, wenn du diesen denn weißt",
                        List.of(
                                new StoryOption("0", "wrongNumber"),
                                new StoryOption("1", "wrongNumber"),
                                new StoryOption("2", "secondRight"),
                                new StoryOption("3", "wrongNumber"),
                                new StoryOption("4", "wrongNumber"),
                                new StoryOption("5", "wrongNumber"),
                                new StoryOption("6", "wrongNumber"),
                                new StoryOption("7", "wrongNumber"),
                                new StoryOption("8", "wrongNumber"),
                                new StoryOption("9", "wrongNumber")
                        )
                );

            case "secondRight":
                return new StoryScene("Bitte gib die dritte Nummer des Code ein, wenn du diesen denn weißt",
                        List.of(
                                new StoryOption("0", "thirdRight"),
                                new StoryOption("1", "wrongNumber"),
                                new StoryOption("2", "wrongNumber"),
                                new StoryOption("3", "wrongNumber"),
                                new StoryOption("4", "wrongNumber"),
                                new StoryOption("5", "wrongNumber"),
                                new StoryOption("6", "wrongNumber"),
                                new StoryOption("7", "wrongNumber"),
                                new StoryOption("8", "wrongNumber"),
                                new StoryOption("9", "wrongNumber")
                        )
                );

            case "wrongNumber":
                return new StoryScene("Du gibst die Nummer für den Code ein auf einmal wird alles schwarz... du bleibst stehen, bis du " +
                        "plötzlich einen schmerz in deinem Magen spürst... Das Licht geht an und du fühlst was dieser Schmerz ist." +
                        "Es ist ein Speer direkt in deinen Magen geschleudert... Du wirst ohnmächtig. \n\nBad End 3",
                        List.of()
                );

            case "thirdRight":
                return new StoryScene("Du gibst die letzte Zahl richtig ein und gehst in den Raum rein und stellst fest das in diesem Raum " +
                        "eine weitere Tür auf dich wartet diesmal ist es aber anders, du fühlst dich förmlich zu ihr hingezogen. Was nun?",
                        List.of(
                                new StoryOption("versuchen zu wiederstehen", "resistance"),
                                new StoryOption("durch gehen", "endOne")
                        )
                );


            case "cheater":
                return new StoryScene("Du gibst die letzte Zahl richtig ein und gehst in den Raum rein und stellst fest das in diesem Raum " +
                        "der alte Mann bereits auf dich Wartet 'Ich mag keine Schummler... woher weißt du die Nummer...' Bevor du bemerken kannst " +
                        "was gerade passiert wird dir schwarz vor Augen. Bad End 4",
                        List.of()
                );

            case "endOne":
                return new StoryScene("Die Tür öffnet sich, und kalte Luft strömt herein. Du lächelst – du hast es geschafft. " +
                        "Doch als du hinaustrittst, siehst du den langen Gang erneut vor dir. Nur diesmal steht am anderen Ende eine " +
                        "Gestalt. Sie sieht aus wie du. Sie schreit um Hilfe in einem Regal.\n\n Good End 1",
                        List.of()
                );

            case "resistance":
                return new StoryScene("Du versuchst zu wiederstehen, bis du merkst der alte Mann ist zurück welcher zu dir sagt" +
                        "'Du bist anscheinend noch nicht bereit einer von den anderen zu werden' noch bevor du den alten Mann befragen kannst," +
                        "verlässt er den Raum welcher sich nun schnell mit Gas füllt und dich Ohnmächtig auf dem Boden zurücklässt. \n\nBad End 5",
                        List.of()
                );



            default:
                return new StoryScene("❌ Ungültige Szene-ID: `" + sceneId + "`", List.of());
        }
    }
}
