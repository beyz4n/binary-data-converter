import java.io.File;
import java.io.PrintWriter;
import java.util.*;

import static java.lang.Math.pow;

public class Main {

    public static void main(String args[]) throws Exception {
        File file = new File("input.txt");
        File outputFile = new File("output.txt");

        PrintWriter output = new PrintWriter(outputFile);

        if (!file.exists())
            throw new Exception("input file does not exist: " + file.getName());

        Scanner fileInput = new Scanner(file);

        Scanner input = new Scanner(System.in);

        System.out.print("Byte ordering: ");
        String endian = input.nextLine();

        System.out.print("Data type: ");
        String dType = input.nextLine();

        System.out.print("Data type size: ");
        String dSize = input.nextLine();

        int size = dSize.charAt(0)-'0';

        String line = "";
        String tempLine = "";

        // Note: unnecessary checks, change to for loops in if
        while (fileInput.hasNextLine()) {
            line = fileInput.nextLine().trim();
            for(int i = 0; i < line.length() ; i++){
                if(line.charAt(i) != ' '){
                    tempLine += line.charAt(i);
                }
            }
            line = CheckEndian(endian, tempLine);
            tempLine = "";

            for (int i = 0; line.length() - i != 0; i += size * 2) {

                if (dType.equalsIgnoreCase("Floating point"))
                    output.print(Float2Decimal(line.substring(i, i + size * 2)) + " ");
                else if (dType.equalsIgnoreCase("Signed integer"))
                    output.print(Convert2Decimal4Signed(line.substring(i, i + size * 2)) + " ");
                else if (dType.equalsIgnoreCase("Unsigned integer"))
                    output.print(Convert2Decimal4Unsigned(line.substring(i, i + size * 2)) + " ");
                else
                    throw new Exception("invalid data type: " + line);


            }
            output.println();
        }

    output.close();
    }

    // Method to change the byte ordering according to endian type
    public static String CheckEndian(String endianType, String hexNumber) {

        // if Little Endian, change the byte ordering-> ex: A3B4C5 -> C5B4A3
        if (endianType.equalsIgnoreCase("Little Endian")) {
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

        String binaryNumber = Hex2Binary(hexNumber);
        String exp = "";
        String signBit = "" + binaryNumber.charAt(0);
        String sign = (signBit.equals('1')) ? "-" : "" ;
        String fraction = "";
        String value = "";

        if (binaryNumber.length() == 8) { // for 1 byte
            exp = binaryNumber.substring(1, 5); // 4 bits will be used exp part
            fraction = binaryNumber.substring(5);
        } else if (binaryNumber.length() == 16) { // for 2 bytes
            exp = binaryNumber.substring(1, 7); // 6 bits will be used exp part
            fraction = binaryNumber.substring(7);
        } else if (binaryNumber.length() == 24) { // for 3 bytes
            exp = binaryNumber.substring(1, 9); // 8 bits will be used exp part
            fraction = binaryNumber.substring(9);
            //fraction = Round2Even(fraction);
            if(fraction.length() == 14){ // if rounded fraction has 14 bits
                exp = binaryAddOne(exp);
                fraction = fraction.substring(1);
            }
        } else if (binaryNumber.length() == 32) {// for 4 bytes
            exp = binaryNumber.substring(1, 11); // 10 bits will be used exp part
            fraction = binaryNumber.substring(11);
            //fraction = Round2Even(fraction);
            if(fraction.length() == 14){ // if rounded fraction has 14 bits
                exp = binaryAddOne(exp);
                fraction = fraction.substring(1);
            }
        }

        int bias = (int) (Math.pow(2, exp.length() - 1) - 1);
        int e; // variable for exp value in decimal
        int E; // variable for E in "value = (-1)^s * M * 2^E" calculation
        String mantissa;

        if (exp.contains("0") && exp.contains("1")) { // if it is normalized
            e = BinaryUnsigned2Decimal(exp);
            E = e - bias;
            mantissa = "1" + fraction; // for denormalized values: mantissa = 1.fraction
            String intPartOfMantissa;
            String fractionPartOfMantissa;
            if(E >= 0) {
                intPartOfMantissa = mantissa.substring(0, E + 1); // Take integer part of floating point number
                fractionPartOfMantissa = mantissa.substring(E + 1); // Take fraction part of floating point number
            }
            else {
                mantissa = AddZeros(E, mantissa);
                intPartOfMantissa = mantissa.substring(0, 1); // Take integer part of floating point number
                fractionPartOfMantissa = mantissa.substring(1); // Take fraction part of floating point number
            }
            value = sign + BinaryUnsigned2Decimal(intPartOfMantissa) + Convert2Decimal4Fraction(fractionPartOfMantissa);
        }
        else if (exp.contains("0")) { // if it is denormalized
            e = 1;
            E = e - bias;
            mantissa = "0" + fraction;
            String intPartOfMantissa = mantissa.substring(0, E + 1);
            String fractionPartOfMantissa = mantissa.substring(E+1);
            if(isAllZeros(fractionPartOfMantissa)){
                if(signBit.equals("0"))
                    value = "0";
                else
                    value = "-0";
            }
            value = sign + BinaryUnsigned2Decimal(intPartOfMantissa) + Convert2Decimal4Fraction(fractionPartOfMantissa);
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


    public static int Convert2Decimal4Unsigned(String hexNumber) {

         int convertedDecimalNumber = 0;
         String convertedBinaryNumber = Hex2Binary(hexNumber);
         int number;
         for(int i = convertedBinaryNumber.length() -1 ; i >= 0  ; i--){
             if (convertedBinaryNumber.charAt(i) == '0')
                 number = 0;
             else
                 number = 1;
             convertedDecimalNumber += (number) * pow(2,convertedBinaryNumber.length() -1 -i);
         }
        return convertedDecimalNumber;
    }

    public static String Round2Even(String fraction) {
        String newFraction = " ";
        String control = "notHalfway";

        if (fraction.length() > 13) {
            for (int i = 13; i < fraction.length(); i++) {
                if (fraction.charAt(i) != 1) {
                    control = "halfway";
                }
            }

            for (int i = 0; i < 13; i++) {
                if (fraction.charAt(13) == '0') {
                       newFraction = newFraction + fraction.charAt(i);
                       return newFraction;

                }
                else if (fraction.charAt(13) == '1' && control == "halfway")  {
                          if(fraction.charAt(12) == '0') {
                              newFraction = newFraction + fraction.charAt(i);
                              return newFraction;
                          }
                                  else {


                              }
                              }

                else if (fraction.charAt(13) == '1' && control == "notHalfway") {

                }
            }
        }

        else {
            return fraction;
        }
        return newFraction;
    }

    // Method for converting fraction part of the floating point number to decimal
    public static String Convert2Decimal4Fraction(String fraction){
        int numberOfDigit = fraction.length();
        double frac = 0;

        for(int i = 1; i <= numberOfDigit; i++){
            if(fraction.charAt(i-1) == '1'){
                frac += Math.pow(2, -i);
            }
        }
        return ("" + frac).substring(1);
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
    // Method to put zeros on the beginning of the binary number
    // ex: for this operation 1.0101 * 2^-1 = 0.10101
    public static String AddZeros(int exp, String binary){
        String zeros = "";

        for(int i = 0; i < -exp; i++){
            zeros += "0";
        }
        return zeros + binary;
    }


}
