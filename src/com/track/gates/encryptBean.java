package com.track.gates;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import com.track.constants.MLoggerConstant;

import java.util.Base64;
/*import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;*/

/********************************************************************************************************
 * PURPOSE : Class for encrypting/ decrypting strings like User password using
 * Sun's JCE
 *******************************************************************************************************/

public class encryptBean {

	Key key;
	Cipher cipher;

	public encryptBean() throws Exception {

		Security.addProvider(new com.sun.crypto.provider.SunJCE());// adds JCE
																	// Provider
		initProcess();
	}

	/********************************************************************************************************
	 * PURPOSE : This method is for initiating the Encryption/Decryption process
	 * .... Reads the DES Key ..if not available creates new one. PARAMETER 1 :
	 * Nil RETURNS : Nil
	 *******************************************************************************************************/
	public void initProcess() throws Exception {

		try {
			// ObjectInputStream in=new ObjectInputStream(new FileInputStream(
			// new File("/PushProject/webapps/gateway/web-inf/props/des.key")));
			// ObjectInputStream in=new ObjectInputStream(new FileInputStream(
			// new File("/../../conf/des.key")));
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					new File(MLoggerConstant.PROPS_FOLDER + "/des.key")));
			key = (Key) in.readObject(); // reads key from the key file
			in.close();
		} catch (FileNotFoundException fnfe) // if key file not found generates
												// new key
		{
			KeyGenerator generator = KeyGenerator.getInstance("DES");
			generator.init(new SecureRandom());
			key = generator.generateKey();
			// ObjectOutputStream out=new ObjectOutputStream(new
			// FileOutputStream( new
			// File("/PushProject/webapps/gateway/web-inf/props/des.key")));
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(new File(MLoggerConstant.PROPS_FOLDER + "/des.key")));
			out.writeObject(key);
			out.close();
		}

		cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

	}

	/********************************************************************************************************
	 * PURPOSE : Method for encrypting a clear text using DES algorithm in ECB
	 * mode using the DES Key generated previously PARAMETER 1 : Clear String to
	 * be encrypted RETURNS : Encrypted String
	 *******************************************************************************************************/

	public String encrypt(String clearStr) throws Exception {
		if (clearStr.length() < 1)
			return "";

		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] clearText = clearStr.getBytes();
		byte[] cipherText = cipher.doFinal(clearText);

//		BASE64Encoder encoder = new BASE64Encoder();
//		String encryptedStr = encoder.encode(cipherText);
		Base64.Encoder encoder = Base64.getEncoder();
		String encryptedStr = encoder.encodeToString(cipherText);

		return encryptedStr;
	}

	/********************************************************************************************************
	 * PURPOSE : Method for decrypting an encrypted text using the same DES key
	 * & algorithm PARAMETER 1 : The Cipher(encrypted) text RETURNS : The Clear
	 * text
	 *******************************************************************************************************/
	public String decrypt(String cipherStr) throws Exception {
		cipher.init(Cipher.DECRYPT_MODE, key);
		//BASE64Decoder decoder = new BASE64Decoder();
		//byte[] raw = decoder.decodeBuffer(cipherStr);
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] raw = decoder.decode(cipherStr);
		byte[] stringBytes = cipher.doFinal(raw);
		String result = new String(stringBytes);

		return result;
	}

}
