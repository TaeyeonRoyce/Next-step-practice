import java.util.regex.Pattern;

public class StringCalculatorService {

	private static final String COMMA = ",";
	private static final String COLON = ":";

	// public int calculateString(String userString) {
	//
	// }

	public boolean isDefaultSeparator(String userString) {
		String defaultRegex = "(\\d*[,:])*\\d*";
		return Pattern.matches(defaultRegex, userString);
	}
}
