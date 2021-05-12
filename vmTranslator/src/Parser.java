import java.util.Scanner;

/**
 * Parses VM file
 */
public class Parser {

    private Scanner in;
    private String command; // the current command
    private String filename; // filename of the .vm file being parsed

    private static final String[] ARITHMETIC_CMDS = {"add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not"};

    public Parser(Scanner in) {
        this.in = in;
    }

    // DESCRIPTION:		command enum
    // PRE-CONDITION:	results when called
    // POST-CONDITION:	returns the correct condition for each argument

    public enum Command {
        C_ARITHMETIC, C_PUSH, C_POP, C_LABEL, C_GOTO, C_IF, C_FUNCTION, C_RETURN, C_CALL
    }

    // DESCRIPTION:		line reader
    // PRE-CONDITION:	is called to check if the file has more lines
    // POST-CONDITION:	returns the boolean for if it has more lines or not
    public boolean hasMoreCommands() {
        return in.hasNextLine();
    }

    // DESCRIPTION:		strips the comments from the file
    // PRE-CONDITION:	when called
    // POST-CONDITION:	returns the stripped file without comments
    public void stripComments() {
        if (command.contains("//")) {
            command = command.substring(0, command.indexOf("//"));
        }
        command = command.replace("\n", "");
        command = command.replace("\t", "");
    }

    // DESCRIPTION:		command length
    // PRE-CONDITION:	results when called
    // POST-CONDITION:	returns the command length
    public int commandLength() {
        return command.length();
    }

    // reads the next command from the input
    public void advance() {
        this.command = in.nextLine();
    }

    // DESCRIPTION:		command type parser
    // PRE-CONDITION:	checks the command type of the argument
    // POST-CONDITION:	returns the correct argument by parsing
    public Command commandType() {
        if (command.startsWith("push")) {
            return Command.C_PUSH;
        } else if (command.startsWith("pop")) {
            return Command.C_POP;
        } else if (isArithmeticCmd()) {
            return Command.C_ARITHMETIC;
        } else if (command.startsWith("label")) {
            return Command.C_LABEL;
        } else if (command.startsWith("goto")) {
            return Command.C_GOTO;
        } else if (command.startsWith("if-goto")) {
            return Command.C_IF;
        } else if (command.startsWith("function")) {
            return Command.C_FUNCTION;
        } else if (command.startsWith("call")) {
            return Command.C_CALL;
        } else if (command.startsWith("return")) {
            return Command.C_RETURN;
        } else {
            return null;
        }
    }

    // returns the command
    public String command() {
        return command.split("\\s+")[0];
    }

    // returns the first argument of the current command
    public String arg1() {
        return command.split("\\s+")[1];
    }

    // returns the second argument of the current command
    // called only if current command is C_PUSH, C_POP, C_FUNCTION or C_CALL
    public String arg2() {
        return command.split("\\s+")[2];
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return this.filename;
    }

    // closes the scanner
    public void close() {
        in.close();
    }

    // checks if command is arithmetic
    private boolean isArithmeticCmd() {
        for (String cmd : ARITHMETIC_CMDS) {
            if (command.equals(cmd)) return true;
        }
        return false;
    }
}