//package application;
//
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import javax.crypto.Cipher;
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//import java.security.Security;
//import java.util.Base64;
//
//
//public class EncryptionManager {
//
//    static {
//        // Add Bouncy Castle as a security provider
//        Security.addProvider(new BouncyCastleProvider());
//    }
//
//    private static final String ENCRYPTION_ALGORITHM = "AES";
//    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
//
//    // Generate a new AES key
//    public static SecretKey generateKey() throws Exception {
//        KeyGenerator keyGen = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM, "BC");
//        keyGen.init(256); // 256-bit AES
//        return keyGen.generateKey();
//    }
//
//    // Generate a new IV (Initialization Vector)
//    public static byte[] generateIV() {
//        byte[] iv = new byte[16]; // AES block size is 16 bytes
//        new java.security.SecureRandom().nextBytes(iv);
//        return iv;
//    }
//
//    // Encrypt a string
//    public static String encrypt(String data, SecretKey key, byte[] iv) throws Exception {
//        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION, "BC");
//        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
//        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
//        return Base64.getEncoder().encodeToString(encryptedBytes);
//    }
//
//    // Decrypt a string
//    public static String decrypt(String encryptedData, SecretKey key, byte[] iv) throws Exception {
//        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION, "BC");
//        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
//        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
//        return new String(decryptedBytes);
//    }
//
//    // Convert key to Base64 string for storage
//    public static String keyToString(SecretKey key) {
//        return Base64.getEncoder().encodeToString(key.getEncoded());
//    }
//
//    // Convert Base64 string back to key
//    public static SecretKey stringToKey(String keyStr) {
//        byte[] decodedKey = Base64.getDecoder().decode(keyStr);
//        return new SecretKeySpec(decodedKey, ENCRYPTION_ALGORITHM);
//    }
//}
