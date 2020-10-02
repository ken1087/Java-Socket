package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	// 계산한 값 담는 변수
	int result = 0;
	// 스트림 연결 소켓
	ServerSocket serverSocket = null;
	// 메세지를 실질적으로 하는 소켓
	Socket socket = null;
	// 메세지를 받는 것
	BufferedReader reader = null;
	// 메세지를 쓰는 것
	PrintWriter writer = null;
	// 보내도 되는 지 체크
	boolean check = false;

	public SocketServer() {
		try {
			// 서버소켓 생성
			serverSocket = new ServerSocket(10000);
			System.out.println("클라이언트 요청 대기중");

			while (true) {
				// 클라이언트로 부터 접속 대기
				// 대기중 => 스트림 연결 요청을 받기 위해
				socket = serverSocket.accept();
				// 클라이언트와 연결 성공 후
				System.out.println("요청이 성공됨");
				// 여러 클라이언트를 받기 위해 쓰레드 생성
				ServerThread st = new ServerThread(socket);
				st.start();
			}
		} catch (IOException e) {
			// 에러 내용 출력
			System.out.println(e.getMessage());
		} finally {
			// 소켓통신 종료
			try {
				socket.close();
				serverSocket.close();
			} catch (IOException e) {
				System.out.println("오류가 발생 했습니다.");
			}
		}
	}

	public static void main(String[] args) {
		// Server측 Socket실행
		new SocketServer();
	}

}
