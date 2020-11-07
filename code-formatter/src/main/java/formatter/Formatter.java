package formatter;

import com.yehorpolishchuk.lexercpp.lexer.Lexer;
import com.yehorpolishchuk.lexercpp.token.Token;
import formatter.dirtree.FileManager;
import formatter.exceptions.ConverterException;
import formatter.filewriter.TokenFileWriter;
import formatter.logger.Logger;
import formatter.streamconverter.TokensStreamConverter;
import formatter.utils.File;
import templatereader.TemplateProperties;
import templatereader.TemplatesReader;
import templatereader.util.BracesPlacement;
import templatereader.util.TemplateTypes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

/**
 * Implementation of C++ code formatter.
 * Entry point for the whole application.
 *
 * Do not forget before production to set 'DEBUG = false' !
 * */
public class Formatter {

    private static final boolean DEBUG = true;
    private static final String DEBUG_OUTPUT_DIR_SUFFIX = "\\src\\main\\resources\\testdata\\output\\";
    private static final String DEBUG_INPUT_DIR_SUFFIX = "\\src\\main\\resources\\testdata\\input\\";
    private static final String TEMP_DIR_SUFFIX = "\\src\\main\\resources\\tmp\\";
    private static final String TEMPLATE_DIR = "\\src\\main\\resources\\templates\\";

    public static void format(String fileFullName, TemplateProperties templateProperties) throws IOException, ConverterException {
        String tempFileName = System.getProperty("user.dir") + TEMP_DIR_SUFFIX + FileManager.getFileNameFromPath(fileFullName);

        format(fileFullName, tempFileName, templateProperties);
        Files.copy(Paths.get(tempFileName), Paths.get(fileFullName), StandardCopyOption.REPLACE_EXISTING);
        Files.delete(Paths.get(tempFileName));
    }

    /**
     *
     * @param fileFullName The full path to C++ file (.cpp)
     * */
    public static ArrayList<Token> format(String fileFullName, String outputFileName, TemplateProperties templateProperties) throws IOException, ConverterException {
        Lexer lexer = new Lexer (fileFullName);
        lexer.parse();
        ArrayList<Token> tokens = lexer.getTokens();

        if (DEBUG){
            for (Token t : tokens ) System.out.println(t.getName().getTokenName() + " --- " + t.getValue().getValue());
        }

        TokensStreamConverter tokensStreamConverter = new TokensStreamConverter(tokens, templateProperties);
        tokensStreamConverter.convert();
        ArrayList<Token> output = tokensStreamConverter.getTokens();

        // fix added
        TokensStreamConverter.removeDoubleSpaces(output);

        TokenFileWriter tokenFileWriter = new TokenFileWriter(output, outputFileName);
        tokenFileWriter.write();

        return output;
    }

    public static void main(String[] args) throws IOException, ConverterException, NoSuchFieldException, IllegalAccessException {
        if (DEBUG) {
            testFormat();
            return;
        }

        commandHandler(args);
    }

    public static void commandHandler(String[] args) throws IOException, ConverterException, NoSuchFieldException, IllegalAccessException {
        if (args.length == 0) {
            help();
            return;
        } else if (args.length == 1) {
            if (args[0].equals("help") || args[0].equals("h")){
                help();
            } else if (args[0].equals("list") || args[0].equals("l")){
                showCommandsList();
            }
            return;
        } else if (args.length == 2){
            if (args[0].equals("help") || args[0].equals("h")) {
                if (args[1].equals("format")){
                    helpFormat();
                } else if (args[1].equals("verify")){
                    helpVerify();
                } else if (args[1].equals("cppfiles")){
                    helpCppfiles();
                }
                return;
            }
        }

        if (args[0].equals("cppfiles") || args[0].equals("cpp")) {
            if (args[1].startsWith("--f=")){
                ArrayList<String> cppFilesNames = FileManager.getCppLanguageFilePathes(args[1].substring(4));
                System.out.println("\n------C++ FILES FOUND:-------");
                for (int i = 0; i < cppFilesNames.size(); i++){
                    System.out.println(i+1 + ". " + cppFilesNames.get(i));
                }
            } else{
                messageTermanation();
                return;
            }
        } else if (args[0].equals("format") || args[0].equals("f") || args[0].equals("verify") || args[0].equals("v")) {
            String filepath = null, templatepath = null, logfileName = null;
            File fileToAnalyze = File.NON_MENTIONED;

            for (String s : args){
                if (s.startsWith("--template=")){
                    templatepath = s.substring(11);
                } else if (s.startsWith("--t=")) {
                    templatepath = s.substring(4);
                }
                if (s.startsWith("--file=")){
                    filepath = s.substring(7);
                } else if (s.startsWith("--f=")){
                    filepath = s.substring(4);
                }

                if (s.equals("--p")) {
                    fileToAnalyze = File.PROJECT;
                } else if (s.equals("--d")){
                    fileToAnalyze = File.DIRECTORY;
                } else if (s.equals("--f")) {
                    fileToAnalyze = File.FILE;
                }

                if (s.startsWith("--l=")){
                    logfileName = s.substring(4);
                } else if (s.startsWith("--logfile=")){
                    logfileName = s.substring(10);
                }
            }

            if (filepath == null || templatepath == null || fileToAnalyze == File.NON_MENTIONED){
                messageTermanation();
                return;
            } else {
                TemplateProperties templateProperties = TemplatesReader.getTemplate(templatepath);
                if (DEBUG){
                    String outputFileName = System.getProperty("user.dir") + TEMP_DIR_SUFFIX + FileManager.getFileNameFromPath(filepath);
                    format(filepath, outputFileName, templateProperties);
                    return;
                }

                if (args[0].equals("format") || args[0].equals("f")){
                    format(filepath,templateProperties);
                } else {
                    verify(filepath, templateProperties, logfileName);
                }

                System.out.println("Successfully done");
            }
        }
    }

    public static void showCommandsList(){
        System.out.println("\n\n--------------COMMANDS--------------");
        System.out.println("h,\thelp\t\t" + "display help message");
        System.out.println("l,\tlist\t\t" + "display the list of available commands");
        System.out.println("f,\tformat\t\t" + "format the code");
        System.out.println("v,\tverify\t\t" + "verifies the code formatting correctness");
        System.out.println("cpp,\tcppfiles\t\t" + "finds the C++ files available");
        System.out.println("h,\thelp (format|verify|cppfiles)\t\t" + "commands usage help");
    }

    public static void help(){
        System.out.println("\n\n--------------HELP--------------");
        showCommandsList();
    }

    public static void helpFormat(){
        System.out.println("Formats the code in files provided");
        System.out.println("\n\nOPTIONS");
        System.out.println("--f, --file\t\t" + "full name (file, directory or project). Used with value after =");
        System.out.println("--t, --template\t\t" + "template file full name. Used with value after =");
        System.out.println("--p|--d|--f\t\t" + "project, directory, exact file");

    }

    public static void helpVerify(){
        System.out.println("Verifies if the code in given files conform the rules in template.\n" +
                "See the result in a log file");
        System.out.println("OPTIONS");
        System.out.println("--f, --file\t\t" + "full name (file, directory or project). Used with value after =");
        System.out.println("--t, --template\t\t" + "template file full name. Used with value after =");
        System.out.println("--p|--d|--f\t\t" + "project, directory, exact file");
        System.out.println("--l, --logfile\t\t" + "the file for logging full name. Used with value after =");
    }

    public static void helpCppfiles(){
        System.out.println("Finds all the C++ files in given directory. Prints the list of full files names");
        System.out.println("\n\nOPTIONS");
        System.out.println("--f, \t\t" + "full name (file, directory or project). Used with value after =");
    }

    public static void messageTermanation(){
        System.out.println("Missed some parameters! Try again. Use 'help <command>'");
        System.out.println("Terminating...");
    }

    /**
     * @param logFileName the absolute path to the error logging file
     * */
    public static void verify(String inputFileName, TemplateProperties templateProperties, String logFileName) throws IOException, ConverterException {
        Logger logger = new Logger(logFileName);
        String tempFileName = System.getProperty("user.dir") + TEMP_DIR_SUFFIX + FileManager.getFileNameFromPath(inputFileName);
        ArrayList<Token> outputStream = format(inputFileName, tempFileName, templateProperties);
        // todo verify

        // the same as format() + comparing to initial file (source)
        // option 1: reading input file and output tokens stream, mark position + error type (optionally)
        // option 2: reading input file and output file, just marking positions
        // alternative 2 should be used

        // remove as a side effect
        Files.delete(Paths.get(tempFileName));
    }


    /**
     * Used while debugging.
     * Set input and template variables to your test file names.
     * */
    public static void testFormat() throws IOException, ConverterException, NoSuchFieldException, IllegalAccessException {
        String input = "test.cpp";
        String template = "template.properties";

        String inputFullPath = System.getProperty("user.dir") + DEBUG_INPUT_DIR_SUFFIX + input;
        String templateFullPath = System.getProperty("user.dir") + TEMPLATE_DIR + template;
        String outputFullPath = System.getProperty("user.dir") + DEBUG_OUTPUT_DIR_SUFFIX + input;

        TemplateProperties templateProperties = TemplatesReader.getTemplate(templateFullPath);
        format(inputFullPath, outputFullPath, templateProperties);
    }
}
