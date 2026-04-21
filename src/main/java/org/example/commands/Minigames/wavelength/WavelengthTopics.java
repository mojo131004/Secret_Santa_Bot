package org.example.commands.Minigames.wavelength;

import java.util.List;
import java.util.Random;

public class WavelengthTopics {

	private static final List<String> TOPICS = List.of(
			"Leicht zu Schwer",
			"Unwichtig zu Wichtig",
			"Gay zu Straight",
			"Gay zu Straight",
			"Bare minimum zu Princes treatment",
			"Bare minimum zu Princes treatment",
			"Bare minimum zu Princes treatment",
			"Bare minimum zu Princes treatment",
			"Würdest du drink anvertrauen zu Würdest du nicht drink anvertrauen",
			"Würdest du drink anvertrauen zu Würdest du nicht drink anvertrauen",
			"Cheating zu nicht Cheating",
			"Cheating zu nicht Cheating",
			"Green Flag zu Red Flag",
			"Green Flag zu Red Flag",
			"Green Flag zu Red Flag",
			"Politisch Links zu Politisch Rechts",
			"Gay zu Lesbian",
			"Sie/Er ist eine 10 aber...",
			"Sie/Er ist eine 10 aber...",
			"Sie/Er ist eine 10 aber...",
			"Sie/Er ist eine 10 aber...",
			"Sie/Er ist eine 10 aber...",
			"Sie/Er ist eine 10 aber...",
			"Sie/Er ist eine 10 aber..."
	);

	private static final Random random = new Random();

	public static String getRandomTopic() {
		return TOPICS.get(random.nextInt(TOPICS.size()));
	}
}
