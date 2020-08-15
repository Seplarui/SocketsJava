import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

	public static void main(String[] args) {
		String host = "localhost";
		int port = 1024;
		
		try {
			
			Socket socket = new Socket(host, port);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			Scanner scanner = new Scanner(System.in); // entrada por teclado
			String linea = null;
			
			while (!"exit".equals(linea)) {
				linea = scanner.nextLine();
				out.println(linea);
				out.flush();
				System.out.println("Server replied "+ in.readLine());
			}
			scanner.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
