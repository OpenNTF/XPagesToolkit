package org.openntf.xpt.core.dss.encryption;

import javax.crypto.spec.SecretKeySpec;

public interface IEncryptionKeyProvider {
	public SecretKeySpec getKey();
	public String getName();
}
