package cliente;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	private static ServerSocket serverCoket;

	private static Socket socket;

	private static PrintWriter pw;

	private static BufferedReader br;

	private static DataOutputStream out = null;

	private static DataInputStream in = null;
	
	private final static String ROOT = "./../../";



	public Server(int puerto) {
		try {
			serverCoket = new ServerSocket(puerto);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		Boolean ejecutado = false;
		while(!ejecutado) {
			System.out.println("-------------------------------------------------------");
			System.out.println("Bienvenido a TuServidorTCP");
			System.out.println("¿Qué deseas hacer?");
			System.out.println("1. Ver archivos disponibles");
			System.out.println("2. Enviar archivos");
			System.out.println("-------------------------------------------------------");
			String risposta = sc.next();
			switch (risposta) {
			case "1": {
				System.out.println("1. video.mp4");
				System.out.println("2. documental.mp4");
				System.out.println("-------------------------------------------------------");
				break;
			}
			case "2": {
				System.out.println("¿Qué archivo desea enviar?");
				System.out.println("-------------------------------------------------------");
				System.out.println("1. video.mp4 (100MB)");
				System.out.println("2. documental.mp4 (200MB)");
				System.out.println("-------------------------------------------------------");
				String archivo = sc.next();
				System.out.println("Ingrese el puerto en donde quiere atender a los clientes.");
				int puerto = sc.nextInt();
				System.out.println("Ingrese la cantidad de clientes a enviar el archivo");
				int clientes = sc.nextInt();
				Server maestro = new Server(puerto);
				if(archivo.equals("1")) {
					maestro.archivoLmao("video.mp4", puerto, clientes);
					ejecutado = true;
				} else if (archivo.equals("2")) {
					maestro.archivoLmao("documental.mp4", puerto, clientes);
					ejecutado = true;
				}
				break;
			}
			default:
				System.out.println("Ingrese un valor válido, no es tan dificil lmao");
			}
		}
	}

	public void archivoLmao(String nombre, int puerto, int clientes){
		ExecutorService executor = Executors.newFixedThreadPool(clientes);
		
		System.out.println("Esperando "+clientes+" clientes...");
		for (int i=0;i<clientes;i++) {
			try {
				
				Socket sc = serverCoket.accept();
				
				System.out.println("Cliente " + i + " aceptado.");
				Delegado d = new Delegado(i, nombre, sc);
				executor.execute(d);
			} catch (IOException e) {
				System.out.println("Error creando el socket cliente.");
				e.printStackTrace();
			}
		}
	}


	public class Delegado extends Thread{
		
		private String nombre;
		
		private int id;
		
		private Socket sc = null;
		
		public Delegado(int id, String filename, Socket sc){
			this.nombre = filename;
			this.id = id;
			this.sc = sc;
		}
		
		public void run() {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm_ss");  
			LocalDateTime createDate = LocalDateTime.now(); 
			String spaghetti = "log-servidor-delegado-"+id+"-"+dtf.format(createDate);
			File log = new File(ROOT+"logs/server/"+spaghetti+".txt");
			dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			FileInputStream fis;
			BufferedInputStream bis;
			BufferedOutputStream out;
			byte[] buffer = new byte[8192];
			try {
				FileWriter fw = new FileWriter(log, true);
				fw.write("Se inicia el envio del archivo el " + dtf.format(LocalDateTime.now()) + "\n");
				fw.write("Se enviará el archivo "+ nombre + "\n");
				System.out.println("Nueva "
						+ "conexión");
				File chivito2 = new File(ROOT+"docs/"+nombre);
				fis = new FileInputStream(ROOT+"docs/"+nombre);
				bis = new BufferedInputStream(fis);
				out = new BufferedOutputStream(sc.getOutputStream());
				int count;
				File chivaso = new File(ROOT+"docs/test.mp4");
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
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	}

}


