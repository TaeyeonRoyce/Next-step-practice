import java.util.Scanner;

public class StringCalculator {
	public static void main(String[] args) {
		StringCalculatorService calService = new StringCalculatorService();
		Scanner inputs = new Scanner(System.in);
		String userString = inputs.nextLine();
		inputs.close();
		System.out.println(calService.calculateString(userString));
	}
}
