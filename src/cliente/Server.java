package cliente;

import java.io.*;
import java.net.*;
import java.nio.file.Files;

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
		System.out.println("Nueva instancia del servidor");
		System.out.println("Esperando conexiones");
		 FileInputStream fis;
	        BufferedInputStream bis;
	        BufferedOutputStream out;
	        byte[] buffer = new byte[8192];
	        try {
	        	Socket socket = serverCoket.accept();
	        	System.out.println("Nueva conexión");
	        	File chivito2 = new File("./docs/video.mp4");
	            fis = new FileInputStream("./docs/video.mp4");
	            bis = new BufferedInputStream(fis);
	            out = new BufferedOutputStream(socket.getOutputStream());
	            int count;
	            System.out.println("Enviando el archivo");
	            int n = 0;
	            while ((count = bis.read(buffer)) > 0) {
	                n++;
	            	out.write(buffer, 0, count);

	            }
	            System.out.println("Terminando la transferencia del archivo");
	            out.close();
	            fis.close();
	            bis.close();
	            System.out.println("Apagando el servidor Salu2");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}





}
