/*
 * © Copyright WebGate Consulting AG, 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.xpt.core.dss.encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.openntf.xpt.core.dss.DSSException;

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

	public static String decrypt(String strHash, SecretKeySpec key) throws DSSException  {
		String strRC = "";
		try {
			Cipher aes = Cipher.getInstance("AES");
			byte[] ciphertext = Base64.decodeBase64(strHash);
			aes.init(Cipher.DECRYPT_MODE, key);
			strRC = new String(aes.doFinal(ciphertext));
		} catch (IllegalBlockSizeException e){
			//System.out.println("- BlockSize : Not encrypted Values in Store - ");
			throw new DSSException("- BlockSize : Not encrypted Values in Store -");
		} catch (BadPaddingException e){
			 //System.out.println("- BadPadding : try clean build -");
			throw new DSSException("- BadPadding : try clean build -");
		} catch (Exception e) {
			//e.printStackTrace();
			throw new DSSException(e.getMessage());
		}
		return strRC;

	}
}
