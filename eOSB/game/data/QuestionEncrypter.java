package eOSB.game.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.io.IOUtils;

import eOSB.game.controller.Constants;
import eOSB.game.controller.Handler;


public class QuestionEncrypter {

	
	public static void main(String[] args) {
		String password = "mop123";

		InputStreamReader isReader = new InputStreamReader(Handler.getResourceAsStream(
				Constants.PASSWORD_FILE));
		BufferedReader reader = new BufferedReader(isReader);
		try {
			if (reader.ready()) {
				String saltAndHash = reader.readLine();
				String realSalt = saltAndHash.split("\\$")[0];
				String passAndSalt = password + realSalt;
				
				String directoryPath = "C:\\Users\\Caine\\Desktop\\files";
				String encryptedFilesPath = directoryPath + "\\encrypted";
				String decryptedFilesPath = directoryPath + "\\decrypted";

				File rootDir = new File(directoryPath);
				if (rootDir.exists() && rootDir.isDirectory()) {
					boolean directoryMade = new File(encryptedFilesPath).mkdir();
					System.out.println("created directory " + encryptedFilesPath + " successfully? " + directoryMade);
					
					for (File inputFile : rootDir.listFiles()) {
						if (!inputFile.isDirectory()) {
							FileInputStream fis = new FileInputStream(inputFile);
							System.out.println("encrypting: " + inputFile.getName());
							OutputStream os = encrypt(passAndSalt, new FileOutputStream(encryptedFilesPath + "\\" + inputFile.getName()));
							IOUtils.copy(fis, os);
							os.flush();
							os.close();
						}
					}
				}
				
				File encryptedFileDir = new File(encryptedFilesPath);
				if (encryptedFileDir.exists() && encryptedFileDir.isDirectory()) {
					boolean directoryMade = new File(decryptedFilesPath).mkdir();
					System.out.println("created directory " + decryptedFilesPath + " successfully? " + directoryMade);
					
					for (File inputFile : encryptedFileDir.listFiles()) {
						if (!inputFile.isDirectory()) {
							System.out.println("decrypting: " + inputFile.getName());
							InputStream is = decrypt(passAndSalt, new FileInputStream(inputFile));
							BufferedReader bufReader = new BufferedReader(new InputStreamReader(is));
							String line = bufReader.readLine();
							while (line != null) {
								System.out.println("line: " + line);
								line = bufReader.readLine();
							}
						}
					}
				}
			}
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static InputStream decrypt(String key, InputStream encryptedInputStream) throws Exception {
	    return new CipherInputStream(encryptedInputStream,
	                                 makeCipher(key, Cipher.DECRYPT_MODE));
	  }

	  static OutputStream encrypt(String key, OutputStream unencryptedOutputStream) throws Exception {
	    return new CipherOutputStream(unencryptedOutputStream,
	                                  makeCipher(key, Cipher.ENCRYPT_MODE));
	  }

	  public static Cipher makeCipher(String key, int mode) throws Exception{
	    DESKeySpec dks = new DESKeySpec(key.getBytes());
	    SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
	    SecretKey desKey = skf.generateSecret(dks);
	    Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE
	    cipher.init(mode, desKey);
	    return cipher;
	  }
	public static void doCopy(InputStream is, OutputStream os) throws IOException {
//		byte[] bytes = new byte[64];
//		int numBytes;
//		while ((numBytes = is.read(bytes)) != -1) {
//			System.out.println("writing bytes: " + new String(bytes));
//			os.write(bytes, 0, numBytes);
//		}
	      IOUtils.copy(is, os);

		os.flush();
		os.close();
		is.close();
	}
}
