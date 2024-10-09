package application;

public class PasswordEvaluator {

    public static String passwordErrorMessage = "";  
    // The input password string
    public static String passwordInput = "";          
    public static int passwordIndexofError = -1;     
    public static boolean foundUpperCase = false;    
    public static boolean foundLowerCase = false;    
    public static boolean foundNumericDigit = false;  
    public static boolean foundSpecialChar = false;   
    public static boolean foundLongEnough = false;    

    // Temporary variables for input processing
    private static String inputLine = "";              
    private static char currentChar;                  
    private static int currentCharNdx;                
    private static boolean running;                   


     // Displays the current state of the input being processed.
    
    private static void displayInputState() {
        System.out.println(inputLine);
        System.out.println(inputLine.substring(0, currentCharNdx) + "?");
        System.out.println("The password size: " + inputLine.length() + 
                           "  |  The currentCharNdx: " + currentCharNdx + 
                           "  |  The currentChar: \"" + currentChar + "\"");
    }


    public static String evaluatePassword(String input) {
        // Resetting evaluation variables for each new input
        passwordErrorMessage = "";
        passwordIndexofError = 0;
        inputLine = input;
        currentCharNdx = 0;
        
        // Check for empty password
        if (input.length() <= 0) return "*** Error *** The password is empty!";
        
        currentChar = input.charAt(0);  // Get the first character of the input

        // Initialize the flags for the required character types
        passwordInput = input;
        foundUpperCase = false;
        foundLowerCase = false;	
        foundNumericDigit = false;
        foundSpecialChar = false;
        foundLongEnough = false;
        running = true;  // Start the evaluation loop

        // Main loop to process each character of the input password
        while (running) {
            displayInputState();  // Show current state of input

            // Check character type and set corresponding flags
            if (currentChar >= 'A' && currentChar <= 'Z') {
                System.out.println("Upper case letter found");
                foundUpperCase = true;  // Found an uppercase letter
            } else if (currentChar >= 'a' && currentChar <= 'z') {
                System.out.println("Lower case letter found");
                foundLowerCase = true;  // Found a lowercase letter
            } else if (currentChar >= '0' && currentChar <= '9') {
                System.out.println("Digit found");
                foundNumericDigit = true;  // Found a numeric digit
            } else if ("~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/".indexOf(currentChar) >= 0) {
                System.out.println("Special character found");
                foundSpecialChar = true;  // Found a special character
            } else {
                // Invalid character found
                passwordIndexofError = currentCharNdx;
                return "*** Error *** An invalid character has been found!";
            }
            
            // Check if the password is long enough (at least 8 characters)
            if (currentCharNdx >= 7) {
                System.out.println("At least 8 characters found");
                foundLongEnough = true;
            }
            currentCharNdx++; 
      
            if (currentCharNdx >= inputLine.length())
                running = false;  
            else
                currentChar = input.charAt(currentCharNdx);  
            
            System.out.println();
        }
        
        // Construct error message based on missing criteria
        String errMessage = "";
        if (!foundUpperCase)
            errMessage += "Upper case; ";
        
        if (!foundLowerCase)
            errMessage += "Lower case; ";
        
        if (!foundNumericDigit)
            errMessage += "Numeric digits; ";
            
        if (!foundSpecialChar)
            errMessage += "Special character; ";
            
        if (!foundLongEnough)
            errMessage += "Long Enough; ";
        
        // If no errors, return an empty string
        if (errMessage.equals(""))
            return "";
        
        // Set the index of the error for reporting and return the error message
        passwordIndexofError = currentCharNdx;
        return errMessage + "conditions were not satisfied";
    }
}
