package codingchallenge;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Constants {

	// Port the server runs on
	public static final int PORT_NO = 8080;

	public static final Map<String, String> CONTEXT_ENDPOINT = createMap();

	private static Map<String, String> createMap() {
		Map<String, String> mapping = new HashMap<String, String>();
		mapping.put("/wiki", "http://en.wikipedia.org/w/api.php?");
		/* Insert more APIs here */
		// result.put("/googlebooks", "");
		return Collections.unmodifiableMap(mapping);
	}
}
