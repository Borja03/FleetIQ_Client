package encryption;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class ClientSideEncryption {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String PUBLIC_KEY_PATH = "encryption/public.key";

    // Encrypts the message and returns Base64 encoded string
    public static String encrypt(String message) throws Exception {
        PublicKey publicKey = loadPublicKey();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        
        // Step 1: Encrypt the message
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        
        // Step 2: Get Base64 encoder instance
        Base64.Encoder encoder = Base64.getEncoder();
        
        // Step 3: Convert encrypted bytes to Base64 string
        String base64EncodedString = encoder.encodeToString(encryptedBytes);
        
        return base64EncodedString;
    }

    // Loads the public key from resources inside the JAR
    private static PublicKey loadPublicKey() throws Exception {
        byte[] keyBytes = readResourceBytes(PUBLIC_KEY_PATH);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return keyFactory.generatePublic(keySpec);
    }

    // Reads the public key as bytes (supports both JAR and filesystem)
    private static byte[] readResourceBytes(String resourcePath) throws Exception {
        try (InputStream inputStream = ClientSideEncryption.class.getClassLoader().getResourceAsStream(resourcePath);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Public key resource not found: " + resourcePath);
            }
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        }
    }
}