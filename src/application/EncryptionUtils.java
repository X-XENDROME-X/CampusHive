/**
 * <p> EncryptionUtils Class </p>
 *
 * <p> Description: This class provides utility methods for converting between 
 * byte arrays and character arrays, generating an initialization vector, and 
 * printing character arrays. </p>
 *
 * <p> Copyright: Shorya Raj © 2024 </p>
 *
 * @author Shorya Raj
 * @version 1.00 2024-10-20
 */

package application;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class EncryptionUtils {
    
	// The below line defines the size of the initialization vector.
	private static int IV_SIZE = 16; 

	
	
	
	/**
     * <p> Converts a byte array into a character array. </p>
     *
     * @param bytes The byte array to be converted into a character array.
     * @return The resulting character array.
     */
    public static char[] toCharArray(byte[] bytes) {		
        // The following line decodes the byte array into a CharBuffer using the default charset.
        CharBuffer charBuffer = Charset.defaultCharset().decode(ByteBuffer.wrap(bytes));
        // The following line returns a copy of the character array from the CharBuffer, limited to its actual size.
        return Arrays.copyOf(charBuffer.array(), charBuffer.limit());
    }
	
    
    
    /**
     * <p> Converts a character array into a byte array. </p>
     *
     * @param chars The character array to be converted into a byte array.
     * @return The resulting byte array.
     */
    static byte[] toByteArray(char[] chars) {		
        // The following line encodes the character array into a ByteBuffer using the default charset.
        ByteBuffer byteBuffer = Charset.defaultCharset().encode(CharBuffer.wrap(chars));
        // The following line returns a copy of the byte array from the ByteBuffer, limited to its actual size.
        return Arrays.copyOf(byteBuffer.array(), byteBuffer.limit());
    }
		
    
    
    /**
     * <p> Generates an initialization vector (IV) from a character array. </p>
     *
     * <p> The IV is used in AES encryption in CBC mode to randomize the encryption. 
     * This method creates an IV from the provided character array by cycling through 
     * the characters until the IV is fully populated. </p>
     *
     * @param text The character array used to generate the initialization vector.
     * @return A byte array representing the initialization vector.
     */
    public static byte[] getInitializationVector(char[] text) {
        // The following line creates a character array to hold the initialization vector.
        char iv[] = new char[IV_SIZE];
		
        // The following line initializes pointers for the text and iv arrays.
        int textPointer = 0;
        int ivPointer = 0;

        // The following line fills the initialization vector using the provided text characters in a circular manner.
        while(ivPointer < IV_SIZE) {
            iv[ivPointer++] = text[textPointer++ % text.length];
        }
		
        // The following line converts the initialization vector character array to a byte array and returns it.
        return toByteArray(iv);
    }
	
    
    
    
    /**
     * <p> Prints the contents of a character array to the console. </p>
     *
     * @param chars The character array to be printed.
     */
    public static void printCharArray(char[] chars) {
        // The following line iterates through each character in the array.
        for(char c : chars) {
            // The following line prints the character to the console without a newline.
            System.out.print(c);
        }
    }
}
