/**
 * <p> EncryptionHelper Class </p>
 *
 * <p> Description: This class provides methods for encrypting and decrypting data 
 * using the AES algorithm in CBC mode with PKCS5 padding.</p>
 *
 * <p> Copyright: Shorya Raj © 2024 </p>
 *
 * @author Shorya Raj
 * @version 1.00 2024-10-20 
 */

package application;

import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

// The following line defines the EncryptionHelper class.
public class EncryptionHelper {

    // The following line defines a constant for the Bouncy Castle provider identifier.
    private static String BOUNCY_CASTLE_PROVIDER_IDENTIFIER = "BC";    
    
    // The following line declares a Cipher object for encryption and decryption.
    private Cipher cipher;

    // The following line initializes a byte array representing the secret key.
    byte[] keyBytes = new byte[] {
        0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
        0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
        0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 
    };

    // The following line creates a SecretKey object from the byte array using AES.
    private SecretKey key = new SecretKeySpec(keyBytes, "AES");

    
    
    /**
     * <p> Constructor: Initializes the EncryptionHelper class by adding the Bouncy Castle provider
     * and setting up the AES/CBC/PKCS5Padding cipher. </p>
     *
     * @throws Exception If any error occurs during cipher initialization or provider setup.
     */
    public EncryptionHelper() throws Exception {
        // The following line adds the Bouncy Castle security provider.
        Security.addProvider(new BouncyCastleProvider());
        
        // The following line initializes the cipher with AES/CBC/PKCS5Padding.
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", BOUNCY_CASTLE_PROVIDER_IDENTIFIER);        
    }
    
    
    
    
    /**
     * <p> Encrypts the provided plain text using AES in CBC mode. </p>
     *
     * @param plainText The plain text (byte array) to be encrypted.
     * @param initializationVector The IV (initialization vector) used for AES encryption in CBC mode.
     * @return The encrypted byte array.
     * @throws Exception If an error occurs during the encryption process.
     */
    public byte[] encrypt(byte[] plainText, byte[] initializationVector) throws Exception {
        // The following line initializes the cipher in encryption mode with the provided key and initialization vector.
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(initializationVector));
        
        // The following line performs the encryption and returns the encrypted byte array.
        return cipher.doFinal(plainText);
    }
    
    
    
    
    /**
    * <p> Decrypts the provided cipher text using AES in CBC mode. </p>
    *
    * @param cipherText The encrypted byte array to be decrypted.
    * @param initializationVector The IV (initialization vector) used for decryption.
    * @return The decrypted byte array.
    * @throws Exception If an error occurs during the decryption process.
    */
    public byte[] decrypt(byte[] cipherText, byte[] initializationVector) throws Exception {
        // The following line initializes the cipher in decryption mode with the provided key and initialization vector.
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(initializationVector));
        
        // The following line performs the decryption and returns the decrypted byte array.
        return cipher.doFinal(cipherText);
    }
    
}
