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

	public Cliente(){
		try {
			socket = new Socket(IP, PUERTO);
			pw = new PrintWriter(socket.getOutputStream(), true);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
			mordiscos = br.readLine();
			System.out.println("soy un mordelon "+mordiscos);
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




