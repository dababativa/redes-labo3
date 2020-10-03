package cliente;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm_ss");  
		LocalDateTime createDate = LocalDateTime.now(); 
		String spaghetti = "log-server-"+dtf.format(createDate);
		File log = new File("./../../logs/server/"+spaghetti+".txt");
		Server babyServer = new Server(6969);
		dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		System.out.println("Nueva instancia del servidor");
		System.out.println("Esperando conexiones");
		 FileInputStream fis;
	        BufferedInputStream bis;
	        BufferedOutputStream out;
	        byte[] buffer = new byte[8192];
	        try {
	        	FileWriter fw = new FileWriter(log, true);
				fw.write("Se inicia el envio del archivo el " + dtf.format(LocalDateTime.now()) + "\n");
				fw.write("Se enviará el archivo video.mp4"+ "\n");
	        	Socket socket = serverCoket.accept();
	        	System.out.println("Nueva conexión");
	        	File chivito2 = new File("./../../docs/video.mp4");
	            fis = new FileInputStream("./../../docs/video.mp4");
	            bis = new BufferedInputStream(fis);
	            out = new BufferedOutputStream(socket.getOutputStream());
	            int count;
	            File chivaso = new File("./../../docs/test.mp4");
				fw.write("El tamaño del archivo es: " + chivaso.length() + " bytes" + "\n");
	            System.out.println("Enviando el archivo");
	            int n = 0;
	            long initio = System.currentTimeMillis();
	            while ((count = bis.read(buffer)) > 0) {
	                n++;
	            	out.write(buffer, 0, count);

	            }
	            int finito = (int) (System.currentTimeMillis() - initio);
				String tiempo = "";
				if(finito<1000 && finito >99) {
					tiempo = "0." + finito;
				} else {
					finito = finito/1000;
					tiempo += finito;
				}
	           
				fw.write("El archivo se envió con éxito en un tiempo de " + tiempo + " segundos" + "\n");
				fw.write("Total de segmentos enviados: " + n + " segmentos" + "\n");
	            System.out.println("Terminando la transferencia del archivo");
	            out.close();
	            fis.close();
	            bis.close();
	            fw.close();
	            System.out.println("Apagando el servidor Salu2");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}





}
