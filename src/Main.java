import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static java.lang.Math.pow;

public class Main {

    public static void main(String args[]) throws Exception {
        Scanner input = new Scanner(System.in);

        System.out.print("Input file: ");
        String inputName = input.nextLine();

        File file = new File(inputName);
        File outputFile = new File("output.txt");

        PrintWriter output = new PrintWriter(outputFile);

        if (!file.exists())
            throw new Exception("input file does not exist: " + file.getName());

        Scanner fileInput = new Scanner(file);

        System.out.print("Byte ordering: ");
        String endian = input.nextLine();
        if(endian.equals(""))
            throw new Exception("no byte ordering value detected");

        if( !(endian.equalsIgnoreCase("l") || endian.equalsIgnoreCase("b") )  ){
            throw new Exception("invalid byte ordering: " + endian);
        }

        System.out.print("Data type: ");
        String dType = input.nextLine();
        if(dType.equals(""))
            throw new Exception("no Data type value detected");

        System.out.print("Data type size: ");
        String dSize = input.nextLine();
        if(dSize.equals(""))
            throw new Exception("no data type size value detected");

        int size = dSize.charAt(0)-'0';

        if( !(1<=size && size<=4) ){
            throw new Exception("invalid data type size: " + dSize);
        }


        String line = "";
        String tempLine = "";

        // Note: unnecessary checks, change to for loops in if
        String reversed = "";
        String convertedNumbers = "";
        while (fileInput.hasNextLine()) {
            line = fileInput.nextLine().trim();
            for(int i = 0; i < line.length() ; i++){
                if(line.charAt(i) == ' ' || (line.charAt(i) >= '0' && line.charAt(i) <= '9')
                        || (line.charAt(i) >= 'a' && line.charAt(i) <= 'f') || (line.charAt(i) >= 'A' && line.charAt(i) <= 'F') ) {
                    if (line.charAt(i) != ' ') {
                        tempLine += line.charAt(i);
                    }
                }
                else{
                    throw new Exception("the input file contains a non hexadecimal character " + line.charAt(i));
                }
            }
            line = CheckEndian(endian, tempLine);
            tempLine = "";


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
            // checking if it's little endian
            if(endian.equalsIgnoreCase("L")){
                String tokens[] = convertedNumbers.split(" ");
                for(int i = tokens.length - 1; i >= 0; i--){
                    reversed += tokens[i] + " ";
                }
                if(endian.equalsIgnoreCase("L")){
                    convertedNumbers = reversed;
                }
            }
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

    public static int Convert2Decimal4Signed(String hexNumber) {

        String binary = Hex2Binary(hexNumber);
        int value = 0;
        if (binary.charAt(0) == '1')
            value = (int) (-pow(2, binary.length() - 1));

        for (int i = 1; i < binary.length(); i++) {
            if (binary.charAt(i) == '1')
                value += (int) pow(2, binary.length() - i - 1);
        }
        return value;
    }

    // Method for converting hexadecimals to binary format
    public static String Hex2Binary(String hex) {
        String converted = "";
        int len = hex.length();
        int count = 0;
        while (len > 0) {
            if (hex.charAt(count) == '0') {
                converted += "0000";
                len--;
                count++;
            } else if (hex.charAt(count) == '1') {
                converted += "0001";
                len--;
                count++;
            } else if (hex.charAt(count) == '2') {
                converted += "0010";
                len--;
                count++;
            } else if (hex.charAt(count) == '3') {
                converted += "0011";
                len--;
                count++;
            } else if (hex.charAt(count) == '4') {
                converted += "0100";
                len--;
                count++;
            } else if (hex.charAt(count) == '5') {
                converted += "0101";
                len--;
                count++;
            } else if (hex.charAt(count) == '6') {
                converted += "0110";
                len--;
                count++;
            } else if (hex.charAt(count) == '7') {
                converted += "0111";
                len--;
                count++;
            } else if (hex.charAt(count) == '8') {
                converted += "1000";
                len--;
                count++;
            } else if (hex.charAt(count) == '9') {
                converted += "1001";
                len--;
                count++;
            } else if (hex.charAt(count) == 'a') {
                converted += "1010";
                len--;
                count++;
            } else if (hex.charAt(count) == 'b') {
                converted += "1011";
                len--;
                count++;
            } else if (hex.charAt(count) == 'c') {
                converted += "1100";
                len--;
                count++;
            } else if (hex.charAt(count) == 'd') {
                converted += "1101";
                len--;
                count++;
            } else if (hex.charAt(count) == 'e') {
                converted += "1110";
                len--;
                count++;
            } else if (hex.charAt(count) == 'f') {
                converted += "1111";
                len--;
                count++;
            }
        }
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
            // If exponent does not consists of only 1s, round fraction bits to 13 bits.
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
            // If exponent does not consists of only 1s, round fraction bits to 13 bits.
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
            e = 1;
            E = 1 - bias;
            if(isAllZeros(fraction)){
                if(signBit.equals("0"))
                    value = "0";
                else
                    value = "-0";
            }
            else{
                mantissa = Convert2Decimal4Fraction(fraction);
                value = sign + (mantissa * Math.pow(2, E));
                value = FractionShortener(value);
            }
        }
        else { // if it is special value
            if(isAllZeros(fraction)) {
                if(signBit.equals("0"))
                    value = "∞";
                else
                    value = "-∞";
            }
            else{
                value = "NaN";
            }
        }
        return value;
    }

    public static BigDecimal Convert2Decimal4Unsigned(String hexNumber) {
         BigDecimal convertedDecimalNumber = new BigDecimal("0") ;
         String convertedBinaryNumber = Hex2Binary(hexNumber);
         int number;
         for(int i = convertedBinaryNumber.length() -1 ; i >= 0  ; i--){
             if (convertedBinaryNumber.charAt(i) == '0')
                 number = 0;
             else
                 number = 1;
             convertedDecimalNumber = convertedDecimalNumber.add(new BigDecimal((number * pow(2,convertedBinaryNumber.length() -1 -i))));
         }
        return convertedDecimalNumber;
    }

    public static String Round2Even(String fraction) {
        String newFraction = "";
        boolean isHalfway = false;
        boolean isRoundUp = false;

        if (fraction.length() > 13) {
            if (fraction.charAt(13) == '1') {
                isHalfway = true;
            }
            if (fraction.contains("1")) {
                isRoundUp = true;
            }

            if (!isHalfway) {
                newFraction += fraction.substring(0, 13);
            }
            else if (isHalfway && !isRoundUp)  {
                newFraction += fraction.substring(0,13);
            }
            else {
                newFraction = binaryAddOne(fraction.substring(0,13));
            }
        }
        else
            return fraction;

        return newFraction;
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

    public static String binaryAddOne(String number){
        int i = number.length() - 1;
        char[] numberChar = number.toCharArray();

        while(i >= 0){
            if(numberChar[i] == '1') {
                numberChar[i] = '0';
                i--;
                if(i == -1) {
                    return "1" + String.valueOf(numberChar);
                }
            }
            else {
                numberChar[i] = '1';
                break;
            }
        }
        return String.valueOf(numberChar);
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
