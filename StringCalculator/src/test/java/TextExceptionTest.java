import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TextExceptionTest {

	private static StringCalculatorService sd;

	@BeforeAll
	public static void setSd() {
		sd = new StringCalculatorService();
	}

	@Test
	void 숫자_이외의_입력() {
		String exceptionString = "1:2a";
		assertThatThrownBy(() -> {sd.calculateString(exceptionString);})
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	void 커스텀_구분자_불일() {
		String exceptionString = "//<\\n1:2:3`";
		assertThatThrownBy(() -> {sd.calculateString(exceptionString);})
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	void 음수() {
		String userString = "1:23:-90";
		assertThatThrownBy(() -> {sd.calculateString(userString);})
			.isInstanceOf(RuntimeException.class);
	}
}
