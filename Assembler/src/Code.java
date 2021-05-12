
import java.util.HashMap;

public class Code {

    private HashMap<String, String> compCodes;
    private HashMap<String, String> destCodes;
    private HashMap<String, String> jumpCodes;


    // DESCRIPTION:		initilizes hashmaps with binary codes for easy lookup
    // PRE-CONDITION:	comp codes = 7 bits (includes a) , dest/jump code = 3 bits
    // POST-CONDITION:	all hashmaps have lookups for valid codes
    public Code()
    {
        compCodes = new HashMap<String,String>();
        destCodes = new HashMap<String,String>();
        jumpCodes = new HashMap<String,String>();

        //When A Bit = 0
        compCodes.put("0", "0101010");
        compCodes.put("1", "0111111");
        compCodes.put("-1", "0111010");
        compCodes.put("D", "0001100");
        compCodes.put("A", "0110000");
        compCodes.put("!D", "0001101");
        compCodes.put("!A", "0110001");
        compCodes.put("-D", "0001111");
        compCodes.put("-A", "0110011");
        compCodes.put("D+1", "0011111");
        compCodes.put("A+1", "0110111");
        compCodes.put("D-1", "0001110");
        compCodes.put("A-1", "0110010");
        compCodes.put("D+A", "0000010");
        compCodes.put("D-A", "0010011");
        compCodes.put("A-D", "0000111");
        compCodes.put("D&A", "0000000");
        compCodes.put("D|A", "0010101");

        //When A Bit = 1
        compCodes.put("M", "1110000");
        compCodes.put("!M", "1110001");
        compCodes.put("-M", "1110011");
        compCodes.put("M+1", "1110111");
        compCodes.put("M-1", "1110010");
        compCodes.put("D+M", "1000010");
        compCodes.put("D-M", "1010011");
        compCodes.put("M-D", "1000111");
        compCodes.put("D&M", "1000000");
        compCodes.put("D|M", "1010101");

        destCodes.put("null", "000");
        destCodes.put("M", "001");
        destCodes.put("D", "010");
        destCodes.put("MD", "011");
        destCodes.put("A", "100");
        destCodes.put("AM", "101");
        destCodes.put("AD", "110");
        destCodes.put("AMD", "111");

        jumpCodes.put("null", "000");
        jumpCodes.put("JGT", "001");
        jumpCodes.put("JEQ", "010");
        jumpCodes.put("JGE", "011");
        jumpCodes.put("JLT", "100");
        jumpCodes.put("JNE", "101");
        jumpCodes.put("JLE", "110");
        jumpCodes.put("JMP", "111");

    }
    // DESCRIPTION:		converts to string of bits(7) for given mnemonic
    // PRE-CONDITION:	hashmaps are built with valid values
    // POST-CONDITION:	returns string of bits if valid, else returns null
    public String getComp(String mnemonic)
    {
        if(compCodes.containsKey(mnemonic))
        {
            return compCodes.get(mnemonic);
        }
        else
        {
            System.out.println("Invalid Code!");
            return null;
        }
    }
    // DESCRIPTION:		converts to string of bits(3) for given mnemonic
    // PRE-CONDITION:	hashmaps are built with valid values
    // POST-CONDITION:	returns string of bits if valid, else returns null
    public String getDest(String mnemonic)
    {
        if(destCodes.containsKey(mnemonic))
        {
            return destCodes.get(mnemonic);
        }
        else
        {
            System.out.println("Invalid Code!");
            return null;
        }
    }
    // DESCRIPTION:		converts to string of bits(3) for given mnemonic
    // PRE-CONDITION:	hashmaps are built with valid values
    // POST-CONDITION:	returns string of bits if valid, else returns null
    public String getJump(String mnemonic)
    {
        if(jumpCodes.containsKey(mnemonic))
        {
            return jumpCodes.get(mnemonic);
        }
        else
        {
            System.out.println("Invalid Code!");
            return null;
        }
    }
    // DESCRIPTION:		converts integer from decimal notation to binary notation
    // PRE-CONDITION:	number is valid size for architecture, non-negative
    // POST-CONDITION:	returns 16-bit string of binary digits (first char is MSB)
    public static String decimalToBinary(int decimal)
    {
        StringBuilder binaryString = new StringBuilder();
        binaryString.append(Integer.toBinaryString(decimal));

        for(int i = binaryString.length(); i < 16; i++)
        {
            binaryString.insert(0, "0");
        }

        return binaryString.toString();
    }

}