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

	// @Test
	// void 기본_구분자_분리() {
	// 	//given
	// 	StringCalculatorService sd = new StringCalculatorService();
	//
	// 	//when
	// 	String userString = "1,2,3";
	//
	// 	//then
	// 	assertThat(sd.calculateString(userString)).isEqualTo(6);
	// }

}