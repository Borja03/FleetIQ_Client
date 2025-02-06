package encryption;

/**
 *
 * @author
 */
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

public class KeyGenerator {

	static {
    	Security.addProvider(new BouncyCastleProvider());
	}

	public static void generateKeys(String publicKeyFile, String privateKeyFile) throws Exception {
    	// Generate RSA Key Pair
    	KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
    	keyPairGenerator.initialize(2048);
    	KeyPair keyPair = keyPairGenerator.generateKeyPair();

    	// Save Public Key
    	saveKeyToFile(publicKeyFile, keyPair.getPublic().getEncoded());

    	// Save Private Key
    	saveKeyToFile(privateKeyFile, keyPair.getPrivate().getEncoded());

	}

	private static void saveKeyToFile(String fileName, byte[] keyBytes) throws IOException {
    	try (FileOutputStream fos = new FileOutputStream(fileName)) {
        	fos.write(keyBytes);
    	}
	}

	public static void main(String[] args) throws Exception {
    	// Generate the key files
    	generateKeys("public.key", "private.key");
	}
}



