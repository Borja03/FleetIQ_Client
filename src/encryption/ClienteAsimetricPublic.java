/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

/**
 *
 * @author Borja
 */
import javax.crypto.Cipher;
import java.io.*;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class ClienteAsimetricPublic {

    public PublicKey loadPublicKey(String resourcePath) throws Exception {
        byte[] publicKeyBytes;
        try (InputStream keyInputStream = getClass().getResourceAsStream(resourcePath);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            if (keyInputStream == null) {
                throw new FileNotFoundException("No se encontró el archivo de clave pública.");
            }
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = keyInputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            publicKeyBytes = baos.toByteArray();
        }

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(publicKeySpec);
    }

    public byte[] encryptMessage(String message, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(message.getBytes());
    }

    public void sendEncryptedMessage(String host, int port, byte[] encryptedData) throws Exception {
        try (Socket socket = new Socket(host, port);
             OutputStream outputStream = socket.getOutputStream()) {
            outputStream.write(encryptedData);
            outputStream.flush();
        }
    }
}
