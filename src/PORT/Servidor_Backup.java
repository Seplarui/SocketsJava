import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Servidor_Backup {

	public static void main(String[] args) {

		ServerSocket servidor = null;
		
		//Definimos mapa y arraylist
		
		HashMap mIPPuerto = new HashMap();
		ArrayList listaClientes = new ArrayList();
		
		try {

			servidor = new ServerSocket(1024);
			servidor.setReuseAddress(true);
			System.out.println("Servidor en marcha");
			int cont = 0;
			while (true) {
				Socket cliente = servidor.accept();
				cont++;
				System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress() + " Puerto Cliente: "+ cliente.getPort());
				System.out.println("Clientes conectados: " + cont);
				GestorClientes clienteSocket = new GestorClientes(cliente, listaClientes);
				
				mIPPuerto = new HashMap();
				//Guardo los datos del cliente en el arraylist
				mIPPuerto.put("IP", cliente.getInetAddress());
				mIPPuerto.put("PUERTO", cliente.getPort());
				System.out.println("mippuerto: "+mIPPuerto);
				listaClientes.add(mIPPuerto);
				System.out.println("Lista clientes conectados Servidor: "+listaClientes);
				
				System.out.println("clienteSocket: "+ clienteSocket.clienteSocket);
				
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
		private ArrayList listaClientes;

		public GestorClientes(Socket socket, ArrayList listaClientes) {
			this.clienteSocket = socket;
			this.listaClientes = listaClientes;
		}

		@Override
		public void run() {
			PrintWriter out = null;
			BufferedReader in = null;
			
			try {

				System.out.println("Socket: "+ clienteSocket);
				out = new PrintWriter(clienteSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
				String linea;
				while ((linea = in.readLine()) != null) {
					System.out.println("Enviado desde el cliente: " + "Ip: " + clienteSocket.getLocalAddress()
							+ " Puerto cliente: " + clienteSocket.getPort() + " " + linea);
					
					//Se envia al cliente de vuelta lo escrito desde el cliente.
					//Iterar la lista de mensajes
//					for (int i=0; i<listaClientes.size();i++) {
//						System.out.println("lista clientes " + listaClientes.get(i));
//					}
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
					
					e.printStackTrace();
				}

			}
		}

	}
	
	public void enviarMensajeTodos(String mensaje) {
		
	}
}
