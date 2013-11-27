package org.openntf.xpt.core.dss.encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class EncryptionFactory {

	public static String encrypt(String strValue, SecretKeySpec key) {
		String strRC = "";
		try {
			Cipher aes = Cipher.getInstance("AES");
			aes.init(Cipher.ENCRYPT_MODE, key);
			byte[] ciphertext = aes.doFinal(strValue.getBytes());
			strRC = Base64.encodeBase64String(ciphertext);
			// strRC = Base64.encodeBase64String(ciphertext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRC;
	}

	public static String decrypt(String strHash, SecretKeySpec key) {
		String strRC = "";
		try {

			Cipher aes = Cipher.getInstance("AES");
			byte[] ciphertext = Base64.decodeBase64(strHash);
			aes.init(Cipher.DECRYPT_MODE, key);
			strRC = new String(aes.doFinal(ciphertext));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRC;

	}
}
