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
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

public class Cliente{

	public final static int PUERTO = 8069;
	
	public final static String IP = "localhost";
	
	public final static String ALGORITMOS = "ALGORITMOS";
	
	public final static String ERROR = "ERROR";
	
	public final static String OK = "OK";
	
	public final static String HOLA = "HOLA";
	
	public final static String SEPARADOR = ":";
	
	public final static String ALG_S = "AES";
	
	public final static String ALG_A = "RSA";
	
	public final static String ALG_D = "HMACSHA1";
	
	private final static String PADDING = "AES/ECB/PKCS5Padding";
	
	private static String idUsuario;
	
	private static String hora="";
	
	private static KeyPair llaves;
	
	private static PublicKey llavePublica;
	
	private static PrivateKey llavePrivada;
	
	private Socket socket;
	
	private static PrintWriter pw;
	
	private static BufferedReader br;
	
	public Cliente(){
		try {
			idUsuario ="";
			for(int i=0;i<4;i++){
				int digito = (int) (Math.random()*9);
				idUsuario+=""+digito;
			}
			System.out.println("Id del usuario: "+idUsuario);
			socket = new Socket(IP, PUERTO);
			pw = new PrintWriter(socket.getOutputStream(), true);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			KeyPairGenerator generator = KeyPairGenerator.getInstance(ALG_A);
			generator.initialize(1024);
			llaves = generator.generateKeyPair();
			llavePublica = llaves.getPublic();
			llavePrivada = llaves.getPrivate();
		} catch (Exception e) {
			System.out.println("Excepcion: "+e.getMessage());
		} 
	}
	
	public static void arrojarExcepcion(String mensaje)throws Exception{
		throw new Exception(mensaje);
	}
	
	public static X509Certificate gc (KeyPair parLlaves)throws OperatorCreationException, CertificateException{
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.add(Calendar.YEAR, 10);
		X509v3CertificateBuilder x509v3CertificateBuilder = new X509v3CertificateBuilder(
				new X500Name("CN=localhost"),
				BigInteger.valueOf(1),
				Calendar.getInstance().getTime(),
				endCalendar.getTime(),
				new X500Name("CN=localhost"),
				SubjectPublicKeyInfo.getInstance(parLlaves.getPublic().getEncoded()));
		ContentSigner contentSigner = new JcaContentSignerBuilder("SHA1withRSA").build(parLlaves.getPrivate());
		X509CertificateHolder x509CertificateHolder = x509v3CertificateBuilder.build(contentSigner);
		return new JcaX509CertificateConverter().setProvider("BC").getCertificate(x509CertificateHolder);
	}
	
	public static byte[] cifrarSimetrico(SecretKey llave, String texto){
		byte[] textoCifrado;
		
		try{
			Cipher cifrador = Cipher.getInstance(PADDING);
			byte[] textoClaro = texto.getBytes();
			cifrador.init(Cipher.ENCRYPT_MODE, llave);
			textoCifrado = cifrador.doFinal(textoClaro);
			return textoCifrado;
		}catch(Exception e){
			System.out.println("Excepcion: "+e.getMessage());
			return null;
		}
	}
	
	public static byte[] descifrarSimetrico(SecretKey llave, byte[] texto){
		byte[] textoClaro;
		
		try {
			Cipher cifrador = Cipher.getInstance(PADDING);
			cifrador.init(Cipher.DECRYPT_MODE, llave);
			textoClaro = cifrador.doFinal(texto);
		} catch (Exception e) {
			System.out.println("Excepcion: "+e.getMessage());
			return null;
		}
		return textoClaro;
		
	}
	
	public static byte[] cifrarAsimetrico(Key llave, String algoritmo, byte[] textoClaro){
		byte[] textoCifrado;
		try {
			Cipher cifrador = Cipher.getInstance(algoritmo);
			cifrador.init(Cipher.ENCRYPT_MODE, llave);
			textoCifrado = cifrador.doFinal(textoClaro);
			return textoCifrado;
		} catch (Exception e) {
			System.out.println("Excepcion: "+e.getMessage());
			return null;
		}
	}
	
	public static byte[] descifrarAsimetrico(Key llave, String algoritmo, byte[] texto){
		byte[] textoClaro;
		try {
			Cipher cifrador = Cipher.getInstance(algoritmo);
			cifrador.init(Cipher.DECRYPT_MODE, llave);
			textoClaro = cifrador.doFinal(texto);
			
		} catch (Exception e) {
			System.out.println("Excepcion: "+e.getMessage());
			return null;
		}
		return textoClaro;
	}
	

	public static void main(String[] args){
		Cliente cliente = new Cliente();
		String respuesta;
		PublicKey llavePublicaServidor=null;
		SecretKey llaveSecreta = null;
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		
		//Etapa 1
		try {
			System.out.println("Enviando "+HOLA+"...");
			pw.println(HOLA);
			respuesta = br.readLine();
			System.out.println("Respuesta del servidor: "+respuesta);
			if(!respuesta.equals(OK)){
				arrojarExcepcion("No hubo respuesta del servidor.");
			}
			System.out.println("Enviando algoritmos...");
			pw.println(ALGORITMOS+SEPARADOR+ALG_S+SEPARADOR+ALG_A+SEPARADOR+ALG_D);
			respuesta = br.readLine();
			if(respuesta.equals(ERROR)){
				arrojarExcepcion("Hubo un error detectado por el servidor.");
			}
			System.out.println("Respuesta del servidor: "+respuesta);
		} catch (Exception e) {
			System.out.println("Excepcion en etapa 1: "+e.getMessage());
		}
			
			
			//Etapa 2
		try{
			System.out.println("Generando certificado del cliente...");
			X509Certificate certificadoCliente = gc(llaves);
			byte[] certificadoClienteEnBytes = certificadoCliente.getEncoded();
			String certificadoClienteEnString = DatatypeConverter.printBase64Binary(certificadoClienteEnBytes);
			System.out.println("Enviando certificado del cliente...");
			pw.println(certificadoClienteEnString);
			respuesta = br.readLine();
			System.out.println("Respuesta del servidor: "+respuesta);
			if(respuesta.equals(ERROR)){
				arrojarExcepcion("Hubo un error detectado por el servidor");
			}
		}catch(Exception e){
			System.out.println("Excepcion en etapa 2: "+e.getMessage());
		}
		
		
		try{
			String certificadoServidorEnString = br.readLine();
			byte[] certificadoServidorEnBytes = DatatypeConverter.parseBase64Binary(certificadoServidorEnString);
			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
			X509Certificate certificadoServidor = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(certificadoServidorEnBytes));
			llavePublicaServidor = certificadoServidor.getPublicKey();
			if(certificadoServidor!=null){
			pw.println(OK);
			}else{
				arrojarExcepcion("Error obteniendo la llave publica del servidor");
			}
		}catch(Exception e){
			System.out.println("Excepcion: "+e.getMessage());
		}
		
		
		try {
			String llaveSimetricaCifradaString = br.readLine();
			byte[] llaveSimetricaCifradaBytes = DatatypeConverter.parseBase64Binary(llaveSimetricaCifradaString);
			byte[] llaveSimetricaDescifradaBytes = descifrarAsimetrico(llavePrivada, ALG_A, llaveSimetricaCifradaBytes);
			llaveSecreta = new SecretKeySpec(llaveSimetricaDescifradaBytes, 0, llaveSimetricaDescifradaBytes.length, "AES");
			String retoCifradoString = br.readLine();
			byte[] retoCifradoBytes = DatatypeConverter.parseBase64Binary(retoCifradoString);
			byte[] retoDescifradoBytes = descifrarSimetrico(llaveSecreta,retoCifradoBytes);
			byte[] retoCifradoAsimetricoBytes = cifrarAsimetrico(llavePublicaServidor, ALG_A, retoDescifradoBytes);
			String retoCifradoAsimetricoString = DatatypeConverter.printBase64Binary(retoCifradoAsimetricoBytes);
			System.out.println("Enviando reto cifrado al servidor...");
			pw.println(retoCifradoAsimetricoString);
			respuesta = br.readLine();
			System.out.println("Respuesta del servidor: "+respuesta);
			if(respuesta.equals(ERROR)){
				arrojarExcepcion("Hubo un error detectado por el servidor.");
			}
		} catch (Exception e) {
			System.out.println("Excepcion: "+e.getMessage());	
		}
		
		//Etapa 3
		try {
			byte[] idBytesCifrado = cifrarSimetrico(llaveSecreta, idUsuario);
			String idStringCifrado = DatatypeConverter.printBase64Binary(idBytesCifrado);
			System.out.println("Enviando id del usuario cifrado al servidor...");
			pw.println(idStringCifrado);
			String horaCifradaString = br.readLine();
			byte[] horaCifradaBytes = DatatypeConverter.parseBase64Binary(horaCifradaString);
			byte[] horaDescifradaBytes = descifrarSimetrico(llaveSecreta, horaCifradaBytes);
			String horaDescifradaString = DatatypeConverter.printBase64Binary(horaDescifradaBytes);
			System.out.println("Hora recibida del servidor (formato militar): "+horaDescifradaString);
			pw.println(OK);
		} catch (Exception e) {
			pw.println(ERROR);
			System.out.println("Excepcion: "+e.getMessage());
		}
	}
}
	
	
	
	
