package formatter   ;

import com.yehorpolishchuk.lexercpp.lexer.Lexer;
import com.yehorpolishchuk.lexercpp.token.Token;
import formatter.exceptions.ConverterException;
import formatter.exceptions.DialogException;
import formatter.filewriter.TokenFileWriter;
import formatter.streamconverter.TokensStreamConverter;
import formatter.utils.File;
import templatereader.TemplatesReader;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Implementation of C++ code formatter.
 * Entry point for the whole application.
 * */
public class Formatter {
    /**
     *
     * @param fileFullName The full path to C++ file (.cpp) OR RELATIVE??!!?!?
     * */
    public static void format(String fileFullName, String outputFileName) throws IOException, ConverterException {
        Lexer lexer = new Lexer (fileFullName);
        lexer.parse();

        ArrayList<Token> tokens = lexer.getTokens();
        TokensStreamConverter tokensStreamConverter = new TokensStreamConverter(tokens);
        tokensStreamConverter.convert();
        ArrayList<Token> output = tokensStreamConverter.getTokens();

        TokenFileWriter tokenFileWriter = new TokenFileWriter(output, outputFileName);
        tokenFileWriter.write();
    }

    public static void main(String[] args) throws IOException, ConverterException, DialogException {
        commandHandler(args);


//        String inputFileName = "C:\\Users\\user\\Desktop\\course4\\metaprogramming\\code-formatter\\src\\main\\resources\\testdata\\input\\input.cpp";
//        String outputFileName = "C:\\Users\\user\\Desktop\\course4\\metaprogramming\\code-formatter\\src\\main\\resources\\testdata\\output\\output.cpp";
//
//        TemplatesReader.dialog(true);

//        format(inputFileName, outputFileName);
    }

    public static void commandHandler(String[] args){
        if (args.length == 0) {
            help();
        } else if (args.length == 1) {
            if (args[0].equals("help") || args[0].equals("h")){
                help();
            }
        } else if (args.length == 2){
            if (args[0].equals("help")) {
                if (args[1].equals("format")){
                    helpFormat();
                } else if (args[1].equals("verify")){
                    helpVerify();
                } else if (args[1].equals("cppfiles")){
                    helpCppfiles();
                }
            }
        }

        if (args[0].equals("format")) {
            String filepath = null, templatepath = null;
            File fileToAnalyze = File.NON_MENTIONED;

            for (String s : args){
                if (s.startsWith("--template=") || s.startsWith("--t=")){
                    templatepath = s.substring(10);
                }
                if (s.startsWith("--file=") || s.startsWith("--f=")){
                    filepath = s.substring(7);
                }

                if (s.equals("--p")) {
                    fileToAnalyze = File.PROJECT;
                } else if (s.equals("--d")){
                    fileToAnalyze = File.DIRECTOmainRY;
                } else if (s.equals("--f")) {
                    fileToAnalyze = File.FILE;
                }
            }

            if (filepath == null || templatepath == null || fileToAnalyze == File.NON_MENTIONED){
                // exit
            } else {
                System.out.println("success");
            }
        }
    }

    public static void help(){

    }

    public static void helpFormat(){

    }

    public static void helpVerify(){

    }

    public static void helpCppfiles(){

    }

}
