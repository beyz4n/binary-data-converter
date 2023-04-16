import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static java.lang.Math.pow;

public class Main {

    public static void main(String args[]) throws Exception {
        // creating scanner to take input from the user
        Scanner input = new Scanner(System.in);
        // prompting the user to enter the file name in a .txt format
        System.out.print("Input file: ");
        String inputName = input.nextLine();
        // creating output file
        File file = new File(inputName);
        File outputFile = new File("output.txt");

        PrintWriter output = new PrintWriter(outputFile);
        // exception handling for the case that the input file could not be found
        if (!file.exists())
            throw new Exception("input file does not exist: " + file.getName());
        // creating scanner to read the input file
        Scanner fileInput = new Scanner(file);
        // prompting the user to enter the byte ordering
        System.out.print("Byte ordering: ");
        String endian = input.nextLine();
        // exception handling for false inputs
        if(endian.equals(""))
            throw new Exception("no byte ordering value detected");

        if( !(endian.equalsIgnoreCase("l") || endian.equalsIgnoreCase("b") )  ){
            throw new Exception("invalid byte ordering: " + endian);
        }
        // prompting the user to enter the data type
        System.out.print("Data type: ");
        String dType = input.nextLine();
        // exception handling for false input
        if(dType.equals(""))
            throw new Exception("no Data type value detected");
        // prompting user to enter data type size
        System.out.print("Data type size: ");
        String dSize = input.nextLine();
        // exception handling for false input
        if(dSize.equals(""))
            throw new Exception("no data type size value detected");

        int size = dSize.charAt(0)-'0';
        // exception handling for false input
        if( !(1<=size && size<=4) ){
            throw new Exception("invalid data type size: " + dSize);
        }

        // creating strings to hold lines from the input file
        String line = "";
        String tempLine = "";
        // creating strings to reverse strings
        String reversed = "";
        String convertedNumbers = "";
        // taking a line from the input file
        while (fileInput.hasNextLine()) {
            line = fileInput.nextLine().trim();
            // skipping whitespace char and checking for non-hexadecimal chars
            for(int i = 0; i < line.length() ; i++){
                if(line.charAt(i) == ' ' || (line.charAt(i) >= '0' && line.charAt(i) <= '9')
                        || (line.charAt(i) >= 'a' && line.charAt(i) <= 'f') || (line.charAt(i) >= 'A' && line.charAt(i) <= 'F') ) {
                    if (line.charAt(i) != ' ') {
                        tempLine += line.charAt(i);
                    }
                }
                // exception handling for the case of non-hexadecimal chars
                else{
                    throw new Exception("the input file contains a non hexadecimal character " + line.charAt(i));
                }
            }
            // reversing the line if needed
            line = CheckEndian(endian, tempLine);
            tempLine = "";

            // calling according functions for the data type to be converted to
            for (int i = 0; line.length() - i != 0; i += size * 2) {

                if (dType.equalsIgnoreCase("Float"))
                    convertedNumbers += Float2Decimal(line.substring(i, i + size * 2)) + " ";
                else if (dType.equalsIgnoreCase("int"))
                    convertedNumbers += Convert2Decimal4Signed(line.substring(i, i + size * 2)) + " ";
                else if (dType.equalsIgnoreCase("Unsigned"))
                    convertedNumbers += Convert2Decimal4Unsigned(line.substring(i, i + size * 2)) + " ";
                else
                    throw new Exception("invalid data type: " + dType);


            }
            // checking if it's little endian, if it is reversing the converted numbers places
            if(endian.equalsIgnoreCase("L")){
                String tokens[] = convertedNumbers.split(" ");
                for(int i = tokens.length - 1; i >= 0; i--){
                    reversed += tokens[i] + " ";
                }
                if(endian.equalsIgnoreCase("L")){
                    convertedNumbers = reversed;
                }
            }
            // printing the converted numbers to the output file
            output.println(convertedNumbers);
            convertedNumbers = "";
            reversed = "";
        }
        output.close();
    }

    // Method to change the byte ordering according to endian type
    public static String CheckEndian(String endianType, String hexNumber) {

        // if Little Endian, change the byte ordering-> ex: A3B4C5 -> C5B4A3
        if (endianType.equalsIgnoreCase("L")) {
            String newHex = "";
            for (int i = hexNumber.length() - 1; i > 0; i = i - 2) {
                newHex = newHex + hexNumber.charAt(i - 1);
                newHex = newHex + hexNumber.charAt(i);
            }
            return newHex;
        }
        return hexNumber; // if it is not Little Endian, return initial hex number
    }
    // method to convert from hexadecimal to decimal using two's compliment
    public static int Convert2Decimal4Signed(String hexNumber) {
        // getting binary value
        String binary = Hex2Binary(hexNumber);
        int value = 0;
        // for the case of starting with a 1, adding the negative of the according number
        if (binary.charAt(0) == '1')
            value = (int) (-pow(2, binary.length() - 1));
        // traversing the binary number and adding the according value
        for (int i = 1; i < binary.length(); i++) {
            if (binary.charAt(i) == '1')
                value += (int) pow(2, binary.length() - i - 1);
        }
        // returning the converted value
        return value;
    }

    // Method for converting hexadecimals to binary format
    public static String Hex2Binary(String hex) {
        // creating a string that will be binary version of the hex
        String converted = "";
        // making len its length
        int len = hex.length();
        // count for indexing the hex string
        int count = 0;
        // while we have characters left in the hex form
        while (len > 0) {
            // if the character at index count is 0
            if (hex.charAt(count) == '0') {
                // then add the correct binary form to the converted string, update the parameters
                converted += "0000";
                len--;
                count++;
            // if the character at index count is 1
            } else if (hex.charAt(count) == '1') {
                // then add the correct binary form to the converted string, update the parameters
                converted += "0001";
                len--;
                count++;
            // if the character at index count is 2
            } else if (hex.charAt(count) == '2') {
                // then add the correct binary form to the converted string, update the parameters
                converted += "0010";
                len--;
                count++;
            // if the character at index count is 3
            } else if (hex.charAt(count) == '3') {
                // then add the correct binary form to the converted string, update the parameters
                converted += "0011";
                len--;
                count++;
            // if the character at index count is 4
            } else if (hex.charAt(count) == '4') {
                // then add the correct binary form to the converted string, update the parameters
                converted += "0100";
                len--;
                count++;
            // if the character at index count is 5
            } else if (hex.charAt(count) == '5') {
                // then add the correct binary form to the converted string, update the parameters
                converted += "0101";
                len--;
                count++;
            // if the character at index count is 6
            } else if (hex.charAt(count) == '6') {
                // then add the correct binary form to the converted string, update the parameters
                converted += "0110";
                len--;
                count++;
            // if the character at index count is 7
            } else if (hex.charAt(count) == '7') {
                // then add the correct binary form to the converted string, update the parameters
                converted += "0111";
                len--;
                count++;
            // if the character at index count is 8
            } else if (hex.charAt(count) == '8') {
                // then add the correct binary form to the converted string, update the parameters
                converted += "1000";
                len--;
                count++;
            // if the character at index count is 9
            } else if (hex.charAt(count) == '9') {
                // then add the correct binary form to the converted string, update the parameters
                converted += "1001";
                len--;
                count++;
            // if the character at index count is a
            } else if (hex.charAt(count) == 'a') {
                // then add the correct binary form to the converted string, update the parameters
                converted += "1010";
                len--;
                count++;
            // if the character at index count is b
            } else if (hex.charAt(count) == 'b') {
                // then add the correct binary form to the converted string, update the parameters
                converted += "1011";
                len--;
                count++;
            // if the character at index count is c
            } else if (hex.charAt(count) == 'c') {
                // then add the correct binary form to the converted string, update the parameters
                converted += "1100";
                len--;
                count++;
            // if the character at index count is d
            } else if (hex.charAt(count) == 'd') {
                // then add the correct binary form to the converted string, update the parameters
                converted += "1101";
                len--;
                count++;
            // if the character at index count is e
            } else if (hex.charAt(count) == 'e') {
                // then add the correct binary form to the converted string, update the parameters
                converted += "1110";
                len--;
                count++;
            // if the character at index count is f
            } else if (hex.charAt(count) == 'f') {
                // then add the correct binary form to the converted string, update the parameters
                converted += "1111";
                len--;
                count++;
            }
        }
        // then return the converted string
        return converted;
    }

    // Method to convert floating point number to decimal number
    public static String Float2Decimal(String hexNumber) {

        String binaryNumber = Hex2Binary(hexNumber); // Convert hex to binary representation
        String exp = ""; // Variable to keep exponent
        String signBit = "" + binaryNumber.charAt(0);
        String sign = (signBit.equals("1")) ? "-" : "" ;
        String fraction = ""; // Variable to keep fraction part in binary
        String value = ""; // Variable to keep decimal value of given hex number

        // For 1 byte
        if (binaryNumber.length() == 8) {
            exp = binaryNumber.substring(1, 5); // 4 bits will be used exp part
            fraction = binaryNumber.substring(5);
        }
        // For 2 bytes
        else if (binaryNumber.length() == 16) {
            exp = binaryNumber.substring(1, 7); // 6 bits will be used exp part
            fraction = binaryNumber.substring(7);
        }
        // For 3 bytes
        else if (binaryNumber.length() == 24) {
            exp = binaryNumber.substring(1, 9); // 8 bits will be used exp part
            fraction = binaryNumber.substring(9);
            // If exponent does not consist of only 1s, round fraction bits to 13 bits.
            fraction = (exp.equals("11111111")) ?  fraction : Round2Even(fraction);
            if(fraction.length() == 14){ // If rounded fraction has 14 bits
                // which mean is fraction was -> 11...11 (13 bits), but after rounding it is -> 10...00 (14 bits)
                exp = binaryAddOne(exp); // so, add extra one bit to exp
                fraction = fraction.substring(1); // delete fractions extra bit
            }
        }
        // For 4 bytes
        else if (binaryNumber.length() == 32) {
            exp = binaryNumber.substring(1, 11); // 10 bits will be used exp part
            fraction = binaryNumber.substring(11);
            // If exponent does not consist of only 1s, round fraction bits to 13 bits.
            fraction = (exp.equals("1111111111")) ?  fraction : Round2Even(fraction);
            if(fraction.length() == 14){ // If rounded fraction has 14 bits
                // which mean is fraction was -> 11...11 (13 bits), but after rounding it is -> 10...00 (14 bits)
                exp = binaryAddOne(exp); // so, add extra one bit to exp
                fraction = fraction.substring(1); // delete fractions extra bit
            }
        }

        int bias = (int) (Math.pow(2, exp.length() - 1) - 1); // Calculate bias
        int e; // Variable for exp value in decimal
        int E; // Variable for E in "value = (-1)^s * M * 2^E" calculation
        double mantissa; // Variable to keep mantissa

        // If it is normalized
        if (exp.contains("0") && exp.contains("1")) {
            e = BinaryUnsigned2Decimal(exp);
            E = e - bias;
            mantissa = 1 + Convert2Decimal4Fraction(fraction); // Mantissa for normalized values = 1.xxxx
            value = sign + (mantissa * Math.pow(2, E)) ; // Calculate decimal value
            value = FractionShortener(value); // Crop the fraction part
        }

        // if it is denormalized
        else if (exp.contains("0")) {
            // in this case E = e - bias will be E = 1 - bias
            E = 1 - bias;
            // and the condition for fraction to be all zeros means that this number is 0
            if(isAllZeros(fraction)){
                // according to the sign bit value will be positive or negative zero.
                if(signBit.equals("0"))
                    value = "0";
                else
                    value = "-0";
            }
            // if it is not all zeros then
            else{
                // we need to calculate the mantissa using our method for converting decimal for fractions
                mantissa = Convert2Decimal4Fraction(fraction);
                // then we will say that the value will be sign + mantissa's decimal value multiplied with the 2 to the power of E
                value = sign + (mantissa * Math.pow(2, E));
                // then we will use our method to cut the fraction's remaining numbers to make it 5 digit precision
                value = FractionShortener(value);
            }
        }
        // if it is special value
        else {
            // again checking for the fraction to be all zeros
            if(isAllZeros(fraction)) {
                // if it is all zeros then value be infinity according to the sign bit
                if(signBit.equals("0"))
                    value = "∞";
                else
                    value = "-∞";
            }
            // otherwise it will mean that our number is not-a-number
            else{
                value = "NaN";
            }
        }
        // after giving the right value for value, we return it
        return value;
    }

    public static BigDecimal Convert2Decimal4Unsigned(String hexNumber) {
         BigDecimal convertedDecimalNumber = new BigDecimal("0") ; //we created variable that has the data type BigDecimal
         String convertedBinaryNumber = Hex2Binary(hexNumber);  //converting hexadecimal to binary
         int number;
         for(int i = convertedBinaryNumber.length() -1 ; i >= 0  ; i--){
             if (convertedBinaryNumber.charAt(i) == '0') //return char '0' or '1' to number 0 or 1
                 number = 0;
             else
                 number = 1;
             convertedDecimalNumber = convertedDecimalNumber.add(new BigDecimal((number * pow(2,convertedBinaryNumber.length() -1 -i))));
         }  //multiplying number variable by the powers of 2
        return convertedDecimalNumber;  //returning converted number
    }

    public static String Round2Even(String fraction) {
        String newFraction = ""; //initializing variables
        boolean isHalfway = false;
        boolean isRoundUp = false;

        if (fraction.length() > 13) { //if fraction's length is more than 13, enters this block
            if (fraction.charAt(13) == '1') { //if char at 13 is 1 then set isHalfway to true
                isHalfway = true;
            }
            if (fraction.contains("1")) {  //if any digit after 13th contains 1, then set isRoundUp to true which means we will add 1 to the number
                isRoundUp = true;
            }

            if (!isHalfway) {  //if it is not halfway, then take first 13 bits and don't add 1
                newFraction += fraction.substring(0, 13);
            }
            else if (isHalfway && !isRoundUp)  {  //if it is halfway and not containing any 1 after 13th digit, then take first 13 bits and don't add 1
                newFraction += fraction.substring(0,13);
            }
            else {  ////if it is halfway and containing any 1 after 13th digit, then add 1
                newFraction = binaryAddOne(fraction.substring(0,13));
            }
        }
        else
            return fraction; //if fraction's length is less than 13, return fraction's itself directly

        return newFraction; //return new fraction
    }

    // Method for converting fraction part of the floating point number to decimal
    public static double Convert2Decimal4Fraction(String fraction){
        int numberOfDigit = fraction.length();
        double frac = 0;

        for(int i = 1; i <= numberOfDigit; i++){
            if(fraction.charAt(i-1) == '1'){
                frac += Math.pow(2, -i);
            }
        }
        return frac;
    }

    // Method to convert unsigned binary number to decimal number
    public static int BinaryUnsigned2Decimal(String binary){

        int decimal = 0;
        int exp = binary.length();

        for(int i = 0; i < binary.length(); i++){
            exp --;
            if(binary.charAt(i) == '1') {
                decimal += Math.pow(2, exp);
            }
        }
        return decimal;
    }

    public static boolean isAllZeros(String str)
    {
        for (int i = 0; i < str.length(); i++){
            if(str.charAt(i) != '0')
                return false;
        }
        return true;
    }

    public static String binaryAddOne(String number){   //this method adds 1 to the binary number for rounding it up
        int i = number.length() - 1;
        char[] numberChar = number.toCharArray(); //creating char array

        while(i >= 0){
            if(numberChar[i] == '1') {  //if it is 1, when we add 1 to it; it should be 0
                numberChar[i] = '0';
                i--;
                if(i == -1) {  //if it reaches the beginning of the string, which means only the first digit is 0, then add one more digit and set it 1
                    return "1" + String.valueOf(numberChar);
                }
            }
            else {  //if it is 0, when we add 1 to it; it should be 1
                numberChar[i] = '1';
                break;
            }
        }
        return String.valueOf(numberChar); //returning string
    }

    // Method to crop the fraction
    public static String FractionShortener(String str){

        int indexOfDot = str.indexOf('.'); // Variable to keep index of dot
        String shortenedNum = ""; // Variable to keep number that has precision of 5 digits after the decimal point
        String newFraction; // Variable to keep fraction that has 5 digit

        if(str.contains("E")) {
            int indexOfE = str.indexOf('E'); // Variable to keep index of 'E'
            // If fraction has more than 5 digits
            if(str.substring(indexOfDot + 1, indexOfE).length() > 5){
                newFraction = str.substring(indexOfDot, indexOfDot + 6);
                shortenedNum = str.substring(0, indexOfDot) + newFraction + "e" + str.substring(indexOfE + 1);
                return shortenedNum;
            }
        }
        else {
            // If fraction has more than 5 digits
            if (str.substring(indexOfDot + 1).length() > 5) {
                newFraction = str.substring(indexOfDot, indexOfDot + 6);
                shortenedNum = str.substring(0, indexOfDot) + newFraction;
                return shortenedNum;
            }
        }
        return str;
    }
}
