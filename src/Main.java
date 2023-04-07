import java.io.File;
import java.io.FileWriter;
import java.util.*;

import static java.lang.Math.pow;

public class Main {

    public static void main(String args[]) throws Exception{
        File file = new File("input.txt");
        FileWriter output = new FileWriter("output.txt");

        if (!file.exists())
            throw new Exception("input file does not exsist: " + file.getName());

        Scanner fileInput = new Scanner(file);

        Scanner input = new Scanner(System.in);

        System.out.println("Byte ordering: ");
        String endian = input.next();

        System.out.println("Data type: ");
        String dType = input.next();

        System.out.println("Data type size: ");
        String dSize = input.next();

        int size = dSize.charAt(0);

        String line = "";

        // Note: unnecessary checks, change to for loops in if
        while(fileInput.hasNextLine()){
            line = CheckEndian( endian, fileInput.nextLine().trim() );

            for(int i = 0;line.length() - i != 0; i += size*2){
                //line.substring(i,i+size*2);

                if(dType.equalsIgnoreCase("Floating point"))
                    line.substring(i,i+size*2);
                else if (dType.equalsIgnoreCase("Signed integer"))
                    output.write( Convert2Decimal4Signed( line.substring(i,i+size*2) ) );
                else if(dType.equalsIgnoreCase("Unsigned integer"))
                    line.substring(i,i+size*2);
                else
                    throw new Exception("invalid data type: " + line);



            }
            // Maybe switch case instead of if else
            /*
            switch (dType) {
                case "Floating point":
                    // call function in loop
                    break;
                }

                 */
        }




    }

    // Method to change the byte ordering according to endian type
    public static String CheckEndian(String endianType, String hexNumber){

        // if Little Endian, change the byte ordering-> ex: A3B4C5 -> C5B4A3
        if(endianType.equals("Little Endian")){
            String newHex = "";
            for(int i = hexNumber.length()-1; i > 0; i = i-2){
                newHex = newHex + hexNumber.charAt(i-1);
                newHex = newHex + hexNumber.charAt(i);
            }
            return newHex;
        }
        return hexNumber; // if it is not Little Endian, return initial hex number
    }

    public static int Convert2Decimal4Signed(String hexNumber){

        String binary = Hex2Binary(hexNumber);
        int value = 0;
        if(binary.charAt(0) == '1')
            value = (int)( -pow(2, binary.length()-1) );

        for(int i = 1; i<binary.length(); i++){
            if(binary.charAt(i) == '1')
                value += (int)pow(2,binary.length()-i-1);
        }
        return value;

    }
    // Note: not sure if this will work
    public static String Hex2Binary(String hexNumber){

        String binary = "";

        for(int i = 0; i < hexNumber.length() ;i++) {

            for (int j = 0; j < 4; j++) {
                binary = Integer.toString( (int)( (hexNumber.charAt(i)-'0')% pow(2,j) ) ).concat( binary );

            }
        }
        return binary;
    }

}
