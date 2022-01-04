import java.util.regex.Pattern;

public class StringCalculatorService {

	private static final int CUSTOM_SEPARATOR_INDEX = 2;
	private static final int NUMBER_START_INDEX = 5;

	//계산 결과 반환
	public int calculateString(String userString) throws RuntimeException {
		if (isUserStringEmpty(userString)) {
			return 0;
		}
		String[] numbers = separateString(userString);
		return addNumbers(numbers);
	}

	//문자열의 공백 체크
	private boolean isUserStringEmpty(String userString) {
		if (userString.isEmpty()
			|| Pattern.matches("\\/\\/.\\\\n", userString)) {
			return true;
		}
		return false;
	}

	//문자열로부터 숫자 추출
	private String[] separateString(String userString) {
		if (isCustomSeparator(userString)) {
			return separateByCustom(userString);
		}

		if (isDefaultSeparator(userString)) {
			return separateByDefault(userString);
		}
		throw new RuntimeException();
	}

	private boolean isDefaultSeparator(String userString) {
		String defaultRegex = "(\\d*[,:])*\\d*";
		return Pattern.matches(defaultRegex, userString);
	}

	private boolean isCustomSeparator(String userString) {
		String customRegex = "\\/\\/(.)\\\\n([\\d]+\\1)*[\\d]+";
		return Pattern.matches(customRegex, userString);
	}

	private String[] separateByCustom(String userString) {
		String customSeparator = userString.split("")[CUSTOM_SEPARATOR_INDEX];
		String[] numberString = userString.substring(NUMBER_START_INDEX)
			.split(customSeparator);
		return numberString;
	}

	private String[] separateByDefault(String userString) {
		return userString.split(",|:");
	}

	private int addNumbers(String[] strings) {
		int sum = 0;
		for (String string : strings) {
			sum += toInt(string);
		}
		return sum;
	}

	private int toInt(String string) {
		int num = Integer.parseInt(string);
		isPositive(num);
		return num;
	}

	private void isPositive(int num) {
		if (num < 0) {
			throw new RuntimeException();
		}
	}
}
