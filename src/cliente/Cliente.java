package cliente;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente{

	public final static int PUERTO = 8080;
	
	public final static String IP = "localhost";
	
	public final static String ALGORITMOS = "ALGORITMOS";
	
	public final static String ERROR = "ERROR";
	
	public final static String OK = "OK";
	
	public final static String HOLA = "HOLA";
	
	public final static String SEPARADOR = ":";
	
	public final static String ALG_S = "AES";
	
	public final static String ALG_A = "RSA";
	
	public final static String ALG_D = "HMACSHA1";
	
	private Socket socket;
	
	private static PrintWriter pr;
	
	private static BufferedReader br;
	
	public Cliente(){
		try {
			socket = new Socket(IP, PUERTO);
			pr = new PrintWriter(socket.getOutputStream(), true);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (Exception e) {
			System.out.println("Excepcion: "+e.getMessage());
		} 
	}
	

		public static void main(String[] args){
			Cliente cliente = new Cliente();
			
			try {
				pr.println("HOLA");
				String linea = br.readLine();
				System.out.println(linea);
				pr.println(ALGORITMOS+SEPARADOR+ALG_S+SEPARADOR+ALG_A+SEPARADOR+ALG_D);
				System.out.println(br.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
