import java.io.*;
import java.util.Scanner;

public class Parser {

    //Constants:
    public static final char NO_COMMAND = 'N';
    public static final char A_COMMAND = 'A';
    public static final char C_COMMAND = 'C';
    public static final char L_COMMAND = 'L';

    //Instance Variables:
    private Scanner inputFile;

    private int lineNumber;
    private String rawLine;
    private String cleanLine;

    private char commandType;
    private String symbol;
    private String destMnemonic;
    private String compMnemonic;
    private String jumpMnemonic;

    //Drivers:

    // DESCRIPTION:		opens input file/ stream and prepares to parse
    // PRE-CONDITION:	provided file is ASM file
    // POST-CONDITION:	if file can't be opened, ends program w/ error message
    public Parser(String inFileName)
    {
        try
        {
            inputFile = new Scanner(new FileInputStream(inFileName));
        }

        catch(FileNotFoundException e)
        {
            System.out.println("\"" + inFileName + "\" was not found...\nShutting down...");
            e.printStackTrace();
            System.exit(0);
        }
        this.lineNumber = 0;
    }
    // DESCRIPTION:		returns boolean if more commands left, closes stream if not
    // PRE-CONDITION:	file stream is open
    // POST-CONDITION:	returns true if more commands, else closes stream
    public boolean hasMoreCommands()
    {
       if(inputFile.hasNextLine())
        {
            return true;
        }
        else
        {
            inputFile.close();
            return false;
        }
    }
    // DESCRIPTION:		reads next line from file and parses it in to instance vars
    // PRE-CONDITION:	file stream is open, called only if hasMoreCommands()
    // POST-CONDITION:	current instruction parts put into insatnce vars
    public void advance()
    {
        if(hasMoreCommands())
        {
            this.rawLine = inputFile.nextLine();
            cleanLine();
            parseCommandType();
            parse();
        }

        if(commandType != 'N' || commandType != 'L')
        {
            lineNumber++;
        }
    }

    //Parsing Helpers:

    // DESCRIPTION:		cleans raw instruction by removing non-essential parts
    // PRE-CONDITION:	String parameter given (not null)
    // POST-CONDITION:	returned with out comments and whitespace
    private void cleanLine()
    {
        this.cleanLine = getRawLine().replaceAll(" ","");
        this.cleanLine = getCleanLine().replaceAll("\t", "");
        int commentLoc = getCleanLine().indexOf("//");

        if(commentLoc != -1)
        {
            this.cleanLine = getCleanLine().substring(0, commentLoc);
        }
    }
    // DESCRIPTION:		determines command type from parameter
    // PRE-CONDITION:	String parameter is clean instruction.
    // POST-CONDITION:	returns 'A' (A-instruction), 'C' (C-instruction),
    //						'L' (label), 'N' (no command)
    private void parseCommandType()
    {
        if(this.cleanLine == null || getCleanLine().isEmpty())
        {
            this.commandType = NO_COMMAND;
        }
        else if(getCleanLine().contains("@"))
        {
            this.commandType = A_COMMAND;
        }
        else if(getCleanLine().contains("(") || getCleanLine().contains(")"))
        {
            this.commandType = L_COMMAND;
        }
        else
        {
            this.commandType = C_COMMAND;
        }
    }
    // DESCRIPTION:		helper method parses line depending on instruction type
    // PRE-CONDITION:	advance() called so cleanLine has value
    // POST-CONDITION:	appropriate parts (instance vars) of instruction filled
    private void parse()
    {
        if(this.commandType == NO_COMMAND)
        {
            //do nothing.
        }
        else if((this.commandType == A_COMMAND) || (this.commandType == L_COMMAND))
        {
            this.parseSymbol();
        }
        else if(this.commandType == C_COMMAND)
        {
            this.parseDest();
            this.parseComp();
            this.parseJump();
        }
    }
    // DESCRIPTION:		parses symbol for A- or L-Commands
    // PRE-CONDITION:	advance() called so cleanLine has a value,
    //						call for A- and L-commands only.
    // POST-CONDITION:	symbol has appropriate value from instruction assigned
    private void parseSymbol()
    {
        String name = getCleanLine();

        if(this.commandType == A_COMMAND)
        {
            this.symbol = name.replaceAll("@", "");
        }
        else if(this.commandType == L_COMMAND)
        {
            name.replaceAll("\\(", "");
            name.replaceAll("\\)", "");
            this.symbol = name;
        }
    }
    // DESCRIPTION:		helper method parses line to get comp part
    // PRE-CONDITION:	advance() called so cleanLine has value,
    //						call for C-instructions only
    // POST-CONDITION:	compMnemonic set to appropriate value from instruction
    private void parseComp()
    {
        String line = getCleanLine();

        if(this.commandType == C_COMMAND)
        {
            if(line.contains("="))
            {
                this.compMnemonic = line.substring((line.indexOf('=')) + 1);
            }
            else if(line.contains(";"))
            {
                this.compMnemonic = line.substring(0, line.indexOf(';'));
            }
        }
    }
    // DESCRIPTION:		helper method parses line to get dest part
    // PRE-CONDITION:	advance() called so cleanLine has a value,
    //						call for C-instructions only
    // POST-CONDITION:	destMnemonic set to appropriate value from instruction
    private void parseDest()
    {
        if(this.getCleanLine().contains("="))
        {
            this.destMnemonic = getCleanLine().substring(0, getCleanLine().indexOf('='));
        }
        else
        {
            this.destMnemonic = "null";
        }
    }
    // DESCRIPTION:		helper method parses line to get jump part
    // PRE-CONDITION:	advance() called so cleanLine has value,
    //						call for C-instructions only
    // POST-CONDITION:	jumpMnemonic set to appropriate value from instruction
    private void parseJump()
    {
        if(this.commandType == C_COMMAND)
        {
            if(this.getCleanLine().contains(";"))
            {
                this.jumpMnemonic = getCleanLine().substring((getCleanLine().indexOf(";")) + 1);
            }
            else
            {
                this.jumpMnemonic = "null";
            }
        }
    }

    //Useful Getters:

    // DESCRIPTION:		getter for command type
    // PRE-CONDITION:	cleanLine has been parsed (advance was called)
    // POST-CONDITION:	returns char for command type (N/A/C/L)
    public char getCommandType()
    {
        return this.commandType;
    }
    // DESCRIPTION:		getter for symbol name
    // PRE-CONDITION:	cleanLine has been parsed (advance was called),
    //						call for labels only (use getCommandType())
    // POST-CONDITION:	returns string for symbol name
    public String getSymbol()
    {
        return this.symbol;
    }
    // DESCRIPTION:		getter for dest part of C-instruction
    // PRE-CONDITION:	cleanLine has been parsed (advanced was called),
    //						call for C-instructions only (use getCommandType)
    // POST-CONDITION:	returns mnemonic (ASM symbol) for dest part
    public String getDest()
    {
        return this.destMnemonic;
    }
    // DESCRIPTION:		getter for comp part of C-instruction
    // PRE-CONDITION:	cleanLine has been parsed (advanced was called),
    //						call for C-instructions only (use getCommandType)
    // POST-CONDITION:	returns mnemonic (ASM symbol) for comp part
    public String getComp()
    {
        return this.compMnemonic;
    }
    // DESCRIPTION:		getter for jump part of C-instruction
    // PRE-CONDITION:	cleanLine has been parsed (advanced was called),
    //						call for C-instructions only (use getCommandType)
    // POST-CONDITION:	returns mnemonic (ASM symbol) for jump part
    public String getJump()
    {
        return this.jumpMnemonic;
    }

    //Debuging Getters:

    // DESCRIPTION:		getter for the string version of command type (debugging)
    // PRE-CONDITION:	advance() and parse() have been called
    // POST-CONDITION:	returns string version of command type
    public String getCommandTypeString()
    {
        return Character.toString(this.commandType);
    }
    // DESCRIPTION:		getter for rawLine from file (debugging)
    // PRE-CONDITION:	advance() was called to put value from file in here
    // POST-CONDITION:	returns string of current original line from file
    public String getRawLine()
    {
        return this.rawLine;
    }
    // DESCRIPTION:		getter for cleanLine from file (debugging)
    // PRE-CONDITION:	advance() and cleanLine() were called
    // POST-CONDITION:	returns string of current clean instruction from file
    public String getCleanLine()
    {
        return this.cleanLine;
    }
    // DESCRIPTION:		getter for lineNumber (debugging)
    // PRE-CONDITION:	n/a
    // POST-CONDITION:	returns line number currently being processed from file
    public int getLineNumber()
    {
        return this.lineNumber;
    }
}