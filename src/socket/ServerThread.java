package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
	// 계산한 값 담는 변수
	private int result = 0;
	// 메세지를 실질적으로 하는 소켓
	private Socket socket;
	// 메세지를 받는 것
	private BufferedReader reader = null;
	// 메세지를 쓰는 것
	private PrintWriter writer = null;
	// 보내도 되는 지 체크
	static boolean check = false;

	// 연결한 클라이언트 socket을 받아옴
	ServerThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			// Input한 내용을 읽기 위한 BufferedReader생성
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// Output을 하기 위한 PrintWriter생성
			writer = new PrintWriter(socket.getOutputStream());
			// Input한 내용을 담을 String타입 변수
			String line = "";
			// Input한 내용 한줄을 읽고 String 변수에 담는다, 담은 내용이 null이 아닐경우 반복문 실행
			while ((line = reader.readLine()) != null) {
				System.out.println("클라이언트로 부터 온 메세지:" + line);
				// ADD num1 num2 형식으로 메세지를 받기 때문에 " "로 split한다.
				String cal[] = line.split(" ");
				// many argument인지 아닌지 판단하기 위해 3개이상인 경우는 계산을 하지 않는다
				if (cal.length > 3) {
					check = false;
					writer.println("Error message : too many arguments");
					writer.flush();
				} else {
					Cal calculator = new Cal();
					// ADD MUL MINUS DIV 가 담기는 배열 0번지
					String str = cal[0];
					// 첫번째 숫자 (String으로 들어오기때문에 int타입으로 변환)
					int firstNum = Integer.parseInt(cal[1]);
					// 두번째 숫자 (String으로 들어오기때문에 int타입으로 변환)
					int secondNum = Integer.parseInt(cal[2]);
					// ADD인 경우 덧셈
					if ("ADD".equals(str)) {
						result = calculator.add(firstNum, secondNum);
						// MINUS인 경우 뺄셈
					} else if ("MINUS".equals(str)) {
						result = calculator.minus(firstNum, secondNum);
						// DIV인 경우 나눗셈
					} else if ("DIV".equals(str)) {
						// 나눗셈의 경우 두번째 값이 0이 아닌경우에만 계산
						if (secondNum != 0) {
							result = calculator.divide(firstNum, secondNum);
						} else {
							check = false;
							writer.println("Error message : divided by zero");
							writer.flush();
						}
						// MUL인 경우 곱셈
					} else if ("MUL".equals(str)) {
						result = calculator.multi(firstNum, secondNum);
					}
					// 그외 나머지 값인 경우
					else {
						check = false;
						writer.println("Error message : [[ ADD:더하기 MINUS:빼기 DIV:나누기 MUL:곱하기 bye:종료 ]] 중 하나를 입력 해주십시요");
						writer.flush();
					}
				}
				if (check) {
					// 클라이언트 측으로 계산한 값을 보낸다
					writer.println("Answer : " + result);
					// writer의 스트림 내용을 비운다
					writer.flush();
				}
			}
		} catch (IOException e) {
			// 에러 내용 출력
			System.out.println(e.getMessage());
		} finally {
			try {
				// 소켓 종료
				socket.close();
			} catch (IOException e) {
				System.out.println("오류가 발생 하였습니다");
			}
		}
	}

}
