import java.util.InputMismatchException;
import java.util.regex.Pattern;

public class StringCalculatorService {

	private static final int CUSTOM_SEPARATOR_INDEX = 2;
	private static final int NUMBER_START_INDEX = 5;

	// public int calculateString(String userString) {
	//
	// }

	public boolean isDefaultSeparator(String userString) {
		String defaultRegex = "(\\d*[,:])*\\d*";
		return Pattern.matches(defaultRegex, userString);
	}

	public boolean isCustomSeparator(String userString) {
		String customRegex = "\\/\\/(.)\\\\n([\\d]+.)*[\\d]+";
		if (!Pattern.matches(customRegex, userString)) {
			return false;
		}
		return isValidString(userString);
	}

	private boolean isValidString(String userString) {
		String customSeparator = userString.split("")[CUSTOM_SEPARATOR_INDEX];
		String[] numberString = userString.substring(NUMBER_START_INDEX)
			.split(customSeparator);
		for (String s : numberString) {
			try {
				Integer.parseInt(s);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return true;
	}
}
