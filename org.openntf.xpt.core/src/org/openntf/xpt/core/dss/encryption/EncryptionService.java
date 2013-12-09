package org.openntf.xpt.core.dss.encryption;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import com.ibm.designer.runtime.Application;
import com.ibm.xsp.application.ApplicationEx;

public class EncryptionService {
	private static final String ENC_SERVICE_KEY = "xpt.dss.encryption"; // $NON-NLS-1$

	private static final String ENC_DATAPROVIDER_SERVICE = "org.openntf.xpt.core.dss.Encryption"; // $NON-NLS-1$

	private static final String PREF_PROVIDER = "xpt.dss.encryption.provider";
	
	private List<IEncryptionKeyProvider> m_ENCServices;
	
	private IEncryptionKeyProvider m_ENCService;

	private SecretKeySpec m_Key;
	
	public static EncryptionService getInstance() {
		EncryptionService cls = (EncryptionService) Application.get().getObject(ENC_SERVICE_KEY);
		if (cls == null) {
			synchronized (EncryptionService.class) {
				cls = (EncryptionService) Application.get().getObject(ENC_SERVICE_KEY);
				if (cls == null) {
					cls = new EncryptionService();
					Application.get().putObject(ENC_SERVICE_KEY, cls);

				}

			}
		}
		return cls;
	}
	
	
	public List<IEncryptionKeyProvider> getEncryptionProviders() {
		if (m_ENCServices == null) {
			AccessController.doPrivileged(new PrivilegedAction<Void>() {

				@SuppressWarnings("unchecked")
				@Override
				public Void run() {
					m_ENCServices = ApplicationEx.getInstance().findServices(ENC_DATAPROVIDER_SERVICE);

					return null;
				}
			});
		}
		return m_ENCServices;
	}
	
	
	public IEncryptionKeyProvider getEncryptionProvider() {
		if (m_ENCService == null) {
			AccessController.doPrivileged(new PrivilegedAction<Void>() {
				public Void run() {
					String providersProp = ApplicationEx.getInstance()
							.getApplicationProperty(PREF_PROVIDER, null);
					List<IEncryptionKeyProvider> allEncProviders = getEncryptionProviders();
					
					for (IEncryptionKeyProvider p:  allEncProviders) {
						if (p.getName().equalsIgnoreCase(providersProp)) {
							m_ENCService = p;
							break;
						}
					}
					if (m_ENCService == null) {
						if(allEncProviders.size() == 1){
							m_ENCService = allEncProviders.get(0);
						}else{
							//"No Provider found. Assign UNID Provider!");
							m_ENCService = new UNIDKeyProvider();
						}
					}

					return null;
				}
			});
		}
		return m_ENCService;
	}
	
	public String encrypt(String strToEncrypt){
		if(strToEncrypt != null){
			return EncryptionFactory.encrypt(strToEncrypt, getMyKey());
		}
		return null;
	}

	public String decrypt(String strToDecrypt){
		if(strToDecrypt != null){
		//	System.out.println("Decypt: " + strToDecrypt);
			return EncryptionFactory.decrypt(strToDecrypt, getMyKey());
		}
		return null;
	}
	private SecretKeySpec getMyKey(){
		if(m_Key == null){
			 m_Key = getEncryptionProvider().getKey();
		}
		return m_Key;
	}
}
