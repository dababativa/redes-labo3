package cliente;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import javax.xml

public class Server {
	
	private static ServerSocket serverCoket;
	
	private static Socket socket;

	private static PrintWriter pw;

	private static BufferedReader br;
	
	private static DataOutputStream out = null;
    
	private static DataInputStream in = null;
	
	
	
	Server(int port){
		try {
			serverCoket = new ServerSocket(port);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}


	public static void main(String[] args){
		
		Server babyServer = new Server(6969);
		try {
			System.out.println("Esperando conexiones");
			socket = serverCoket.accept();
			System.out.println("Conectado con un baby client");
			pw = new PrintWriter(socket.getOutputStream(), true);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			File chivito = new File("./docs/202010-caso2-logistica.pdf");
			long tamanho = chivito.length();
			byte[] mordiscos = Files.readAllBytes(chivito.toPath());
			System.out.println(DatatypeConverter.printBase64Binary(mordiscos));
			pw.println(mordiscos);
			System.out.println("Buena esa mordisquitos");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}





}
