package org.example.commands.wavelength;

import java.util.List;
import java.util.Random;

public class WavelengthTopics {

	private static final List<String> TOPICS = List.of(
			"Mild vs. Scharf",
			"Neu vs. Alt",
			"Harmlos vs. Gefährlich",
			"Billig vs. Teuer",
			"Mainstream vs. Nerdig",
			"Leise vs. Laut",
			"Einfach vs. Komplex",
			"Vorsichtig vs. Mutig",
			"Künstlich vs. Natürlich",
			"Klassisch vs. Modern",
			"Unfreundlich vs. Freundlich",
			"Uncool vs. Cool",
			"Häufig vs. Selten",
			"Leicht vs. Schwer",
			"Unwichtig vs. Wichtig",
			"Beruhigend vs. Unheimlich",
			"Langsam vs. Schnell",
			"Basic vs. Luxuriös",
			"Geordnet vs. Chaotisch",
			"Unrealistisch vs. Realistisch"
	);

	private static final Random random = new Random();

	public static String getRandomTopic() {
		return TOPICS.get(random.nextInt(TOPICS.size()));
	}
}
