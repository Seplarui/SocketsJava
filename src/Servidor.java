import java.io.*;
import java.net.*;

public class Servidor {

	public static void main(String[] args) {

		ServerSocket servidor = null;
		try {

			servidor = new ServerSocket(1024);
			servidor.setReuseAddress(true);
			System.out.println("Servidor en marcha");
			int cont=0;
			while (true) {
				Socket cliente = servidor.accept();
				cont++;
				System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
				System.out.println("Clientes conectados: "+ cont );
				GestorClientes clienteSocket = new GestorClientes(cliente);
				

				new Thread(clienteSocket).start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				servidor.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

	private static class GestorClientes implements Runnable {
		private final Socket clienteSocket;

		public GestorClientes(Socket socket) {
			this.clienteSocket = socket;
		}

		@Override
		public void run() {
			PrintWriter out = null;
			BufferedReader in = null;

			try {

				out = new PrintWriter(clienteSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
				String linea;
				while ((linea = in.readLine()) != null) {
					System.out.println("Enviado desde el cliente: "+clienteSocket.getLocalAddress()+" " + linea);
					out.println(linea);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				out.close();
				try {
					in.close();
					clienteSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}

	}
}
