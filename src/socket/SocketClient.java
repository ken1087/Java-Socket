package socket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {

	// 메세지를 실질적으로 하는 소켓
	Socket socket = null;
	// 메세지를 받는 것
	BufferedReader reader = null;
	// 메세지를 쓰는 것
	PrintWriter writer = null;
	// 파일 경로
	String filePath = "C:\\Users\\kangin\\Desktop\\";
	// file명
	String fileName = "serverinfo.dat";

	public SocketClient() {
		try {
			// file 객체 생성한 후 경로 지정
			File file = new File(filePath + fileName);
			// 파일 내용 취득한 후 ip와 port번호를 배열에 담는다
			String arr[] = getFile(file);
			// ip주소
			String ipAddress = arr[0];
			// port번호
			int portNumber = Integer.parseInt(arr[1]);
			// Server 소켓 연결 (IP, PORT)
			socket = new Socket(ipAddress, portNumber);
			// Input한 내용을 읽기 위한 BufferedReader생성
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// Output을 하기 위한 PrintWriter생성
			writer = new PrintWriter(socket.getOutputStream());
			// while문이 돌아가는 도중에도 메세지를 받기 위한 쓰레드 객체 생성
			Thread readerThread = new Thread(new ReaderThread());
			// 쓰레드 시작
			readerThread.start();
			// 사용자로 부터 입력받기.
			Scanner sc = new Scanner(System.in);
			// 메인 쓰레드 역할 - 쓰는역할
			while (true) {
				// 입력 한 값을 String 변수에 담는다
				String line = sc.nextLine();
				//bye를 입력할 시 종료 한다.
				if("bye".equals(line)) {
					sc.close();
					break;
				}
				// 담은 내용을 Server측으로 보낸다
				writer.println(line);
				// writer를 비운다
				writer.flush();
			}
		} catch (IOException e) {
			// 에러 내용 출력
			System.out.println(e.getMessage());
		} finally {
			// 소켓통신 종료
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("오류가 발생 했습니다.");
			}
		}
	}
	// 파일을 읽고 ip주소와 port번호 취득
	public String[] getFile(File file) {
		// 파일 존재 검사
		if (file.exists()) {
			// 파일이 있을 경우 읽는다
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				// 파일 내용 담을 변수
				String line = "";
				// 한줄 읽기
				line = br.readLine();
				//읽은 내용을 , 로 나눈후 배열에 담는다
				String arr[] = line.split(",");
				// 배열을 반환한다
				return arr;
			} catch (IOException e) {
				// 에러 내용
				e.printStackTrace();
			}
		} else {
			// default IP주소, 포트 번호
			String arr[] = {"localhost","10000"};
			// 배열 반환
			return arr;
		}
		return null;
	}

	// Server측으로부터 값을 받기 위한 쓰레드
	class ReaderThread implements Runnable {

		@Override
		public void run() {
			// 값을 담기 위한 변수
			String line = null;
			try {
				// Input한 내용 한줄을 읽고 String 변수에 담는다, 담은 내용이 null이 아닐경우 반복문 실행
				while ((line = reader.readLine()) != null) {
					// 서버로부터 온 값을 출력
					System.out.println(line);
				}
			} catch (Exception e) {
				// 에러 내용 출력
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		// Client측 Socket실행
		new SocketClient();
	}

}
