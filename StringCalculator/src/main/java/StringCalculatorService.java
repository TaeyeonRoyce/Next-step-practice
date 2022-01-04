import java.util.regex.Pattern;

public class StringCalculatorService {

	private static final int CUSTOM_SEPARATOR_INDEX = 2;
	private static final int NUMBER_START_INDEX = 5;

	public int calculateString(String userString) throws RuntimeException {
		String[] numbers = separateString(userString);
		return addNumbers(numbers);
	}



	public boolean isDefaultSeparator(String userString) {
		String defaultRegex = "(\\d*[,:])*\\d*";
		return Pattern.matches(defaultRegex, userString);
	}

	public boolean isCustomSeparator(String userString) {
		String customRegex = "\\/\\/(.)\\\\n([\\d]+.)*[\\d]+";
		return Pattern.matches(customRegex, userString);
	}

	public String[] separateString(String userString) {
		if (isCustomSeparator(userString)) {
			return separateByCustom(userString);
		} else if (isDefaultSeparator(userString)) {
			return separateByDefault(userString);
		}
		throw new RuntimeException();
	}

	private String[] separateByCustom(String userString) {
		String customSeparator = userString.split("")[CUSTOM_SEPARATOR_INDEX];
		String[] numberString = userString.substring(NUMBER_START_INDEX)
			.split(customSeparator);
		for (String s : numberString) {
			try {
				Integer.parseInt(s);
			} catch (NumberFormatException e) {
				throw new RuntimeException();
			}
		}
		return numberString;
	}

	private String[] separateByDefault(String userString) {
		return userString.split(",|:");
	}

	private int addNumbers(String[] strings) {
		int sum = 0;
		for (String string : strings) {
			sum += checkEachNumber(string);
		}
		return sum;
	}
	private int checkEachNumber(String string) {
		int num = Integer.parseInt(string);
		if (num < 0) {
			throw new RuntimeException();
		}
		return num;
	}

}
