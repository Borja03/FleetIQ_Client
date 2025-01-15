/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 *
 * @author Borja
 */
public class AsimetricoRSA_KeyGenerator {
 /**
     * Genera el fichero con la clave privada
     */
    public void generatePrivateKey() {

        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance("RSA"); // Si RSA no es posible implementar 
            // NoSuchAlgorithmException 
            generator.initialize(1024); // Indicamos el tamanio del key: 1024 bits
            KeyPair keypair = generator.generateKeyPair();
            PublicKey publicKey = keypair.getPublic();  
            PrivateKey privateKey = keypair.getPrivate();  
            System.out.println(publicKey.getFormat());
            System.out.println(privateKey.getFormat());
            
            // Publica     
            FileOutputStream fileOutputStream = new FileOutputStream(".\\FleetIQRSA_Public.key");
            fileOutputStream.write(publicKey.getEncoded());
            fileOutputStream.close();

            // Privada    
            fileOutputStream = new FileOutputStream(".\\FleetIQRSA_Private.key");
            fileOutputStream.write(privateKey.getEncoded());
            fileOutputStream.close();
        } catch (NoSuchAlgorithmException e)
        {
            System.exit(-1);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AsimetricoRSA_KeyGenerator asimetricoRSA_KeyGenerator = new AsimetricoRSA_KeyGenerator();
        asimetricoRSA_KeyGenerator.generatePrivateKey();
        System.out.println("Ficheros de Clave Generados!");
    }
}