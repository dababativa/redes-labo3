package cliente;

import java.io.*;
import java.net.Socket;

import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    


public class Cliente{

	public final static int PUERTO = 6969;

	public final static String IP = "";

	public final static String ERROR = "ERROR";

	public final static String OK = "OK";

	public final static String HOLA = "HOLA";

	private Socket socket;

	private static PrintWriter pw;

	private static BufferedReader br;

	private static int bufferSize;

	private static InputStream is;

	public Cliente(){
		try {
			System.out.println("Conectándose al servidor " + IP + " a través del puerto " + PUERTO);
			socket = new Socket(IP, PUERTO);
			System.out.println("Conexión exitosa");
			is = socket.getInputStream();

			bufferSize = socket.getReceiveBufferSize();
			pw = new PrintWriter(socket.getOutputStream(), true);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("Listo para la transferencia de archivos");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public static void main(String[] args){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm_ss");  
		LocalDateTime createDate = LocalDateTime.now(); 
		String spaghetti = "log-client-"+dtf.format(createDate);
		File log = new File("./logs/client/"+spaghetti+".txt");
		Cliente cliente = new Cliente();
		dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		byte[] respuesta;
		String mordiscos;
		try {
			FileWriter fw = new FileWriter(log, true);
			fw.write("Se inicia la descarga del archivo el " + dtf.format(LocalDateTime.now()) + "\n");
			fw.write("Se descargará el archivo video.mp4 y se guardará como test.mp4" + "\n");
			FileOutputStream fos = new FileOutputStream("./docs/test.mp4");
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			byte[] bytes = new byte[bufferSize];
			int count;
			System.out.println("Inicia la transferencia de archivos");
			int n = 0;
			long initio = System.currentTimeMillis();
			while ((count = is.read(bytes)) >= 0) {
				n++;
				bos.write(bytes, 0, count);
				System.out.println("Descargando el segmento " + n);
			}
			int finito = (int) (System.currentTimeMillis() - initio);
			String tiempo = "";
			if(finito<1000 && finito >99) {
				tiempo = "0." + finito;
			} else {
				finito = finito/1000;
				tiempo += finito;
			}
			File chivaso = new File("./docs/test.mp4");
			fw.write("El tamaño del archivo es: " + chivaso.length() + " bytes" + "\n");
			fw.write("El archivo se descargó con éxito en un tiempo de " + tiempo + " segundos" + "\n");
			fw.write("Total de segmentos descargados: " + n + " segmentos" + "\n");
			System.out.println("Tiempo total de descarga: " + tiempo + " segundos");
			System.out.println("Finaliza la descarga");
			bos.close();
			is.close();
			pw.println("Descarga finalizada. Código:1");
			System.out.println(spaghetti);
			System.out.println("Desconectado del servidor");
			fw.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}




