package socket;

public class Cal {
	// 덧셈 메소드
	public int add(int firstNum, int secondNum) {
		int result = firstNum + secondNum;
		ServerThread.check = true;
		return result;
	}

	// 뺄셈 메소드
	public int minus(int firstNum, int secondNum) {
		int result = firstNum - secondNum;
		ServerThread.check = true;
		return result;
	}

	// 나눗셈 메소드
	public int divide(int firstNum, int secondNum) {
		int result = firstNum / secondNum;
		ServerThread.check = true;
		return result;
	}

	// 곱셈 메소드
	public int multi(int firstNum, int secondNum) {
		int result = firstNum * secondNum;
		ServerThread.check = true;
		return result;
	}
}
