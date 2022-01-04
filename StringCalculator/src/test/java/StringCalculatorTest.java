import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class StringCalculatorTest {
	private static StringCalculatorService calService;

	@BeforeAll
	public static void setSd() {
		calService = new StringCalculatorService();
	}

	@Test
	void 공백_입력_계산() {
		//given

		//when
		String defaultString = "";
		String customString = "//<\\n";

		//then
		assertThat(calService.calculateString(defaultString)).isEqualTo(0);
		assertThat(calService.calculateString(customString)).isEqualTo(0);
	}

	@Test
	void 한자리_숫자_계산() {
		//given

		//when
		String customString = "//<\\n1";

		//then
		assertThat(calService.calculateString(customString)).isEqualTo(1);
	}

	@Test
	void 디폴트_구분자_계산() {
		//given

		//when
		String defaultString = "130,23:90";
		String customString = "//<\\n11<23<34";

		//then
		assertThat(calService.calculateString(defaultString)).isEqualTo(243);
		assertThat(calService.calculateString(customString)).isEqualTo(68);
	}

	@Test
	void 커스텀_구분자_계산() {
		//given

		//when
		String customString = "//<\\n11<23<34";

		//then
		assertThat(calService.calculateString(customString)).isEqualTo(68);
	}
}