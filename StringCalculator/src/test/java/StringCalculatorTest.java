import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class StringCalculatorTest {

	@Test
	void 기본_구분자_문자열() {
		//given
		StringCalculatorService sd = new StringCalculatorService();

		//when
		String userString = "113,22:1173";
		String exceptionString = "1:2a";

		//then
		assertThat(sd.isDefaultSeparator(userString)).isTrue();
		assertThat(sd.isDefaultSeparator(exceptionString)).isFalse();
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