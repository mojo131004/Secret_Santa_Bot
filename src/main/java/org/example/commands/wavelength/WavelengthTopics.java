package org.example.commands.wavelength;

import java.util.List;
import java.util.Random;

public class WavelengthTopics {

	private static final List<String> TOPICS = List.of(
			"Scharf vs. Mild",
			"Alt vs. Neu",
			"Gefährlich vs. Harmlos",
			"Teuer vs. Billig",
			"Nerdig vs. Mainstream",
			"Laut vs. Leise",
			"Komplex vs. Einfach",
			"Mutig vs. Vorsichtig",
			"Natürlich vs. Künstlich",
			"Modern vs. Klassisch",
			"Freundlich vs. Unfreundlich",
			"Cool vs. Uncool",
			"Selten vs. Häufig",
			"Schwer vs. Leicht",
			"Wichtig vs. Unwichtig",
			"Unheimlich vs. Beruhigend",
			"Schnell vs. Langsam",
			"Luxuriös vs. Basic",
			"Chaotisch vs. Geordnet",
			"Realistisch vs. Unrealistisch"
	);

	private static final Random random = new Random();

	public static String getRandomTopic() {
		return TOPICS.get(random.nextInt(TOPICS.size()));
	}
}
