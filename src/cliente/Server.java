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
		
		 FileInputStream fis;
	        BufferedInputStream bis;
	        BufferedOutputStream out;
	        byte[] buffer = new byte[8192];
	        try {
	        	Socket socket = serverCoket.accept();
	            fis = new FileInputStream("./docs/video.mp4");
	            bis = new BufferedInputStream(fis);
	            out = new BufferedOutputStream(socket.getOutputStream());
	            int count;
	            while ((count = bis.read(buffer)) > 0) {
	                out.write(buffer, 0, count);

	            }
	            out.close();
	            fis.close();
	            bis.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}





}
