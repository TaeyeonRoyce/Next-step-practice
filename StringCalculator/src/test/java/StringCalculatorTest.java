import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class StringCalculatorTest {
	private static StringCalculatorService sd;

	@BeforeAll
	public static void setSd() {
		sd = new StringCalculatorService();
	}

	@Test
	void 기본_구분자_문자열() {
		//given

		//when
		String userString = "113,22:1173";
		String exceptionString = "1:2a";

		//then
		assertThat(sd.isDefaultSeparator(userString)).isTrue();
		assertThat(sd.isDefaultSeparator(exceptionString)).isFalse();
	}

	@Test
	void 커스텀_구분자_문자열() {
		//given

		//when
		String userString = "//<\\n1<2<3";
		String exceptionString = "//`\\n1a`2`3";

		//then
		assertThat(sd.isCustomSeparator(userString)).isTrue();
		assertThat(sd.isCustomSeparator(exceptionString)).isFalse();
	}

	@Test
	void 커스텀_구분자() {
		//given

		//when
		String exceptionString = "//<\\n1:2:3`";

		//then
		assertThat(sd.isCustomSeparator(exceptionString)).isFalse();
	}

	@Test
	void 숫자_추출() {
		//given

		//when
		String userString = "113,22:1173";

		//then
		assertThat(sd.separateString(userString)).contains("113","22","1173");
	}

	@Test
	void 커스텀_문자열_숫자_추출() {
		//given

		//when
		String userString = "//<\\n11<23<34";

		//then
		assertThat(sd.separateString(userString)).contains("11","23","34");
	}

	@Test
	void 음수_예외_처리() {
		//given

		//when
		String userString = "1:23:-90";

		//then
		assertThatThrownBy(() -> {sd.calculateString(userString);})
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	void 계산() {
		//given

		//when
		String defaultString = "130,23:90";
		String customString = "//<\\n11<23<34";

		//then
		assertThat(sd.calculateString(defaultString)).isEqualTo(243);
		assertThat(sd.calculateString(customString)).isEqualTo(68);
	}

	@Test
	void 공백_입력_처리() {
		//given

		//when
		String defaultString = "";
		String customString = "//<\\n";

		//then
		assertThat(sd.calculateString(defaultString)).isEqualTo(0);
		assertThat(sd.calculateString(customString)).isEqualTo(0);
	}
}