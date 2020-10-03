package cliente;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

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

	public static void arrojarExcepcion(String mensaje)throws Exception{
		throw new Exception(mensaje);
	}

	//	public static byte[] cifrarSimetrico(SecretKey llave, String texto){
	//		byte[] textoCifrado;
	//		
	//		try{
	//			Cipher cifrador = Cipher.getInstance(PADDING);
	//			byte[] textoClaro = texto.getBytes();
	//			cifrador.init(Cipher.ENCRYPT_MODE, llave);
	//			textoCifrado = cifrador.doFinal(textoClaro);
	//			return textoCifrado;
	//		}catch(Exception e){
	//			System.out.println("Excepcion: "+e.getMessage());
	//			return null;
	//		}
	//	}

	//	public static byte[] descifrarSimetrico(SecretKey llave, byte[] texto){
	//		byte[] textoClaro;
	//		
	//		try {
	//			Cipher cifrador = Cipher.getInstance(PADDING);
	//			cifrador.init(Cipher.DECRYPT_MODE, llave);
	//			textoClaro = cifrador.doFinal(texto);
	//		} catch (Exception e) {
	//			System.out.println("Excepcion: "+e.getMessage());
	//			return null;
	//		}
	//		return textoClaro;
	//		
	//	}

	public static void main(String[] args){
		Cliente cliente = new Cliente();
		byte[] respuesta;
		String mordiscos;
		try {
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
			System.out.println("Tiempo total de descarga: " + tiempo + " segundos");
			System.out.println("Finaliza la descarga");
			bos.close();
			is.close();
			System.out.println("Desconectado del servidor");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		//Etapa 1
		try {
			pw.println(HOLA);
			//			respuesta = br.readLine();
			//			System.out.println("Respuesta del servidor: "+respuesta);
		} catch (Exception e) {
			System.out.println("Excepcion en etapa 1: "+e.getMessage());
		}
	}
}




