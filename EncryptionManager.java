package application;

import java.util.Base64;

public class EncryptionManager {

    /**
     * Encrypts the given data using a custom XOR-based encryption algorithm.
     *
     * @param data Plaintext data to be encrypted
     * @param key A string-based key for encryption
     * @return Encrypted data as Base64 encoded string
     */
	public static String encrypt(String data, String key) {
	    byte[] dataBytes = data.getBytes();
	    byte[] keyBytes = key.getBytes();
	    byte[] encryptedBytes = new byte[dataBytes.length];

	    for (int i = 0; i < dataBytes.length; i++) {
	        encryptedBytes[i] = (byte) (dataBytes[i] ^ keyBytes[i % keyBytes.length]);
	    }

	    // Ensure proper Base64 encoding
	    return Base64.getEncoder().encodeToString(encryptedBytes);
	}


    /**
     * Decrypts the given data using a custom XOR-based encryption algorithm.
     *
     * @param encryptedData Encrypted data as Base64 encoded string
     * @param key A string-based key for decryption
     * @return Decrypted plaintext
     */
	public static String decrypt(String encryptedData, String key) {
	    byte[] encryptedBytes;

	    try {
	        encryptedBytes = Base64.getDecoder().decode(encryptedData);
	    } catch (IllegalArgumentException e) {
	        throw new IllegalArgumentException("Invalid Base64 string provided for decryption.");
	    }

	    byte[] keyBytes = key.getBytes();
	    byte[] decryptedBytes = new byte[encryptedBytes.length];

	    for (int i = 0; i < encryptedBytes.length; i++) {
	        decryptedBytes[i] = (byte) (encryptedBytes[i] ^ keyBytes[i % keyBytes.length]);
	    }

	    return new String(decryptedBytes);
	}
	
	/**
	 * Checks if a given string is encrypted using the custom XOR-based encryption algorithm.
	 *
	 * @param data The string to check
	 * @param key The key to attempt decryption
	 * @return true if the string is encrypted, false otherwise
	 */
	public static boolean isEncrypted(String data, String key) {
	    try {
	        // Try to decode the data using Base64
	        byte[] decodedBytes = Base64.getDecoder().decode(data);

	        // Attempt decryption to verify if it makes sense
	        byte[] keyBytes = key.getBytes();
	        byte[] decryptedBytes = new byte[decodedBytes.length];

	        for (int i = 0; i < decodedBytes.length; i++) {
	            decryptedBytes[i] = (byte) (decodedBytes[i] ^ keyBytes[i % keyBytes.length]);
	        }

	        // Check if decrypted data is a valid string
	        String decryptedString = new String(decryptedBytes);

	        // If no exception occurs and the data is valid, return true
	        return true;
	    } catch (IllegalArgumentException | UnsupportedOperationException e) {
	        // If decoding or decryption fails, it is not encrypted
	        return false;
	    }
	}



    public static void main(String[] args) {
        try {
            // Example usage
            String key = "CustomKey123"; // Custom encryption key
            String originalText = "Hello, Custom Encryption!";

            System.out.println("Original Text: " + originalText);

            // Encrypt the data
            String encryptedBody = "BhsQBhYdPwwWX39SLRQUER1DLwAaQ0tDN10RGwsU"; // Example
            String decryptedBody = EncryptionManager.decrypt(encryptedBody, "CustomKey123");
            System.out.println("Decrypted Body: " + decryptedBody);
            
            String encryptedText = encrypt(originalText, key);
            System.out.println("Encrypted Text: " + encryptedText);

            // Decrypt the data
            String decryptedText = decrypt(encryptedText, key);
            System.out.println("Decrypted Text: " + decryptedText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
