package org.openntf.xpt.core.dss.encryption;

import java.security.MessageDigest;

import javax.crypto.spec.SecretKeySpec;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class UNIDKeyProvider implements IEncryptionKeyProvider {

	@Override
	public SecretKeySpec getKey() {
		try {
			String passphrase = ExtLibUtil.getCurrentDatabase().getReplicaID();
			MessageDigest digest = MessageDigest.getInstance("SHA");
			digest.update(passphrase.getBytes());
			return new SecretKeySpec(digest.digest(), 0, 16, "AES");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
