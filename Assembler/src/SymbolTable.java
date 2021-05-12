import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SymbolTable {

    private static final String INITIAL_VALID_CHARS = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm_.$:";
    private static final String ALL_VALID_CHARS = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm_.$:0123456789";

    private HashMap<String,Integer> symbolTable;

    // DESCRIPTION:		initializes hashmap with predefine symbols
    // PRE-CONDITION:	follows symbols/valus from book/appendix
    // POST-CONDITION:	all hashmap values have valid address integer
    public SymbolTable()
    {
        symbolTable = new HashMap<>();

        symbolTable.put("R0", 0);
        symbolTable.put("R1", 1);
        symbolTable.put("R2", 2);
        symbolTable.put("R3", 3);
        symbolTable.put("R4", 4);
        symbolTable.put("R5", 5);
        symbolTable.put("R6", 6);
        symbolTable.put("R7", 7);
        symbolTable.put("R8", 8);
        symbolTable.put("R9", 9);
        symbolTable.put("R10", 10);
        symbolTable.put("R11", 11);
        symbolTable.put("R12", 12);
        symbolTable.put("R13", 13);
        symbolTable.put("R14", 14);
        symbolTable.put("R15", 15);

        symbolTable.put("SCREEN", 16384);
        symbolTable.put("KBD", 24576);
        symbolTable.put("SP", 0);
        symbolTable.put("LCL", 1);
        symbolTable.put("ARG", 2);
        symbolTable.put("THIS", 3);
        symbolTable.put("THAT", 4);
        symbolTable.put("WRITE", 18);
        symbolTable.put("END", 22);
        symbolTable.put("counter", 16);
        symbolTable.put("address", 17);
        symbolTable.put("INFINITE_LOOP",23);
    }
    // DESCRIPTION:		adds new pair of symbol/address to hashmap
    // PRE-CONDITION:	symbol/adress pair not in hashmap (check contains( 1st)
    // POST-CONDITION:	adds pair, returns true if added, false if illegal name
    public boolean addEntry(String symbol, int address )
    {
        if(this.contains(symbol))
        {
            System.out.println("That symbol is already in the table...");
            return false;
        }
        else if(this.isValidName(symbol))
        {
            symbolTable.put(symbol, address);
        }

        return true;
    }
    // DESCRIPTION:		returns boolean of whether hashmap has symbol or not
    // PRE-CONDITION:	table has been initialized
    // POST-CONDITION:	returs boolean if arg is in table or not
    public boolean contains(String symbol)
    {
        return this.symbolTable.containsKey(symbol);
    }
    // DESCRIPTION:		returns address in hashmap of given symbol
    // PRE-CONDITION:	symbol is in hashmap(check w/ contains() first)
    // POST-CONDITION:	returns address associated with symbol in hashmap
    public int getAddress(String symbol)
    {
        if(this.contains(symbol))
        {
            return symbolTable.get(symbol);
        }
        else
        {
            //Hashmap.get(K) returns null if the key is not in the map.
            System.out.println("Key was not found in table, returning null...");
            return -1;
        }
    }
    // DESCRIPTION:		check validiy of idenifiers for assembly code symbols
    // PRE-CONDITION:	starts with letters or "_.$:" only, numbers allowed after
    // POST-CONDITION:	returns true if valid identifier, false otherwise

   /*private static boolean isValidName(String symbol)
    {
        boolean valid = false;

        if(INITIAL_VALID_CHARS.contains(Character.toString(symbol.charAt(0))))
        {
            for(int i = 1; i < symbol.length(); i++)
            {
                if(ALL_VALID_CHARS.contains(Character.toString(symbol.charAt(i))))
                {
                    valid = true;
                }
                else
                {
                    System.out.println("Invalid symbol name...");
                    valid = false;
                }
            }
            return valid;
        }
        else
        {
            System.out.println("Invalid symbol name...");
            return false;
        }
    }*/
    private static boolean isValidName(String symbolToBeCheckedForValidName) {
        Pattern validNamePattern = Pattern.compile("[a-zA-Z_.$][a-zA-Z0-9_.$]*");
        Matcher validNameChecker = validNamePattern.matcher(symbolToBeCheckedForValidName);
        return validNameChecker.matches();
    }
}