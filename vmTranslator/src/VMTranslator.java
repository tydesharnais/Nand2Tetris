import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @Author Ty Desharnais
 */

public class VMTranslator {
    public static void main(String[] args) {

        String outFilename = null; // name of output file
        ArrayList<Parser> filesToParse = new ArrayList<>(); // contains the .vm files that need to be parsed into .asm
        File file;
        PrintWriter printWriter;

        //If the file is not included
        if (args.length != 1) {
           Scanner sc = new Scanner(System.in);
           String fileName;
           System.out.print("Please enter the file name <extension vm>: ");
           fileName = sc.nextLine();
           file = new File(fileName);

            boolean exists = file.exists(); // Check if file exists
            boolean isDirectory = file.isDirectory(); // Check if it's a directory
            boolean isFile = file.isFile(); // Check if it's a regular file

            if (!exists) {
                System.err.println(fileName + " is not a valid file or path");
                System.exit(1);
            } else if (isFile && fileName.endsWith(".vm")) { // single .vm file supplied
                Parser parser = getParser(file);
                parser.setFilename(fileName);
                filesToParse.add(parser);
               String[] fileparse = fileName.split(".vm");
                fileName = fileparse[0];
                System.out.println(fileName);
                outFilename = fileName + ".asm";
            } else if (isDirectory) { // directory supplied, scan it for all .vm files
                File[] files = file.listFiles();
                for (File f : files) {
                    if (f.getName().endsWith(".vm")) {
                        Parser parser = getParser(f);
                        fileName = f.getName().substring(0, f.getName().indexOf(".vm"));
                        parser.setFilename(fileName);
                        filesToParse.add(parser);
                    }
                }

                // throw error and exit if directory supplied contains no .vm files
                if (filesToParse.size() == 0) {
                    System.err.println("No .vm files to parse in " + fileName);
                    System.exit(1);
                }
                String[] fileparse = fileName.split(".vm");
                fileName = fileparse[0];
                System.out.println(fileName);
                outFilename = file.getAbsolutePath() + "/" + fileName + ".asm"; // output filename is dir name + .asm
            } else {
                printCommandLineErrorAndExit();
            }
            // instantiate a single codewriter
            printWriter = null;
            try {
                assert outFilename != null;
                printWriter = new PrintWriter(outFilename);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }

        //If the file is included
        else {

            file = new File(args[0]);
            boolean exists = file.exists(); // Check if file exists
            boolean isDirectory = file.isDirectory(); // Check if it's a directory
            boolean isFile = file.isFile(); // Check if it's a regular file

            if (!exists) {
                System.err.println(args[0] + " is not a valid file or path");
                System.exit(1);
            } else if (isFile && args[0].endsWith(".vm")) { // single .vm file supplied
                Parser parser = getParser(file);
                String filename = args[0].substring(0, args[0].indexOf(".vm"));
                parser.setFilename(filename);
                filesToParse.add(parser);
                outFilename = filename + ".asm";
            } else if (isDirectory) { // directory supplied, scan it for all .vm files
                File[] files = file.listFiles();
                for (File f : files) {
                    if (f.getName().endsWith(".vm")) {
                        Parser parser = getParser(f);
                        String filename = f.getName().substring(0, f.getName().indexOf(".vm"));
                        parser.setFilename(filename);
                        filesToParse.add(parser);
                    }
                }

                // throw error and exit if directory supplied contains no .vm files
                if (filesToParse.size() == 0) {
                    System.err.println("No .vm files to parse in " + args[0]);
                    System.exit(1);
                }

                outFilename = file.getAbsolutePath() + "/" + file.getName() + ".asm"; // output filename is dir name + .asm
            } else {
                printCommandLineErrorAndExit();
            }
            // instantiate a single codewriter
            printWriter = null;
            try {
                assert outFilename != null;
                printWriter = new PrintWriter(outFilename);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }


        CodeWriter codeWriter = new CodeWriter(printWriter);

        boolean initialized = false; // flag indicates if bootstrap code has been written

        // iterate through the parsers: one for each .vm file
        for (Parser fileToParse : filesToParse) {

            if (!initialized) { // write bootstrap code just once, at beginning of file
                codeWriter.writeInit();
                initialized = true;
            }

            codeWriter.setFileName(fileToParse.getFilename()); // set the file name of the file currently being parsed

            while (fileToParse.hasMoreCommands()) {
                fileToParse.advance(); // go to next line

                fileToParse.stripComments(); // strip comments
                if (fileToParse.commandLength() == 0) continue; // don't process if no valid command

                Parser.Command cmd = fileToParse.commandType();

                switch (cmd) {
                    case C_POP:
                    case C_PUSH:
                        codeWriter.writePushPop(fileToParse.commandType(), fileToParse.arg1(),
                                Integer.parseInt(fileToParse.arg2()));
                        break;
                    case C_ARITHMETIC:
                        codeWriter.writeArithmetic(fileToParse.command());
                        break;
                    case C_LABEL:
                        codeWriter.writeLabel(fileToParse.arg1());
                        break;
                    case C_GOTO:
                        codeWriter.writeGoto(fileToParse.arg1());
                        break;
                    case C_IF:
                        codeWriter.writeIf(fileToParse.arg1());
                        break;
                    case C_FUNCTION:
                        codeWriter.writeFunction(fileToParse.arg1(), Integer.parseInt(fileToParse.arg2()));
                        break;
                    case C_RETURN:
                        codeWriter.writeReturn();
                        break;
                    case C_CALL:
                        codeWriter.writeCall(fileToParse.arg1(), Integer.parseInt(fileToParse.arg2()));
                        break;
                    default:
                        break;
                }

            }

            fileToParse.close();
        }

        // close resources
        codeWriter.close();

    }

    // DESCRIPTION:		determines command type from parameter
    // PRE-CONDITION:	File name
    // POST-CONDITION: creates a new filereader/parser
    //						'L' (label), 'N' (no command)
    private static Parser getParser(File file) {
        Parser parser = null;
        try {
            parser = new Parser(new Scanner(new FileReader(file)));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return parser;
    }

    // DESCRIPTION:		throws an error message
    // PRE-CONDITION:	Wrong file
    // POST-CONDITION:	returns a wrong file message

    private static void printCommandLineErrorAndExit() {
        System.err.println("usage: java VMTranslator/VMTranslator <filename.vm>");
        System.err.println("OR");
        System.err.println("java VMTranslator/VMTranslator <directory>");
        System.exit(1);
    }
}