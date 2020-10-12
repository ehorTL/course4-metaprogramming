package formatter.filewriter;

import com.yehorpolishchuk.lexercpp.lexer.Lexer;
import com.yehorpolishchuk.lexercpp.token.Token;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Gets the full path to file as filename
 * */
public class TokenFileWriter {
    ArrayList<Token> tokens;
    private String filename;

    public TokenFileWriter(ArrayList<Token> tokens, String filename) {
        this.tokens = tokens;
        this.filename = filename;
    }

    public void write() throws IOException {
        FileWriter fileWriter = new FileWriter(this.filename);
        fileWriter.write(generateStringFromTokens());
        fileWriter.close();
    }

    public String generateStringFromTokens() {
        StringBuilder sb = new StringBuilder();

        for (Token t : this.tokens) {
            sb.append(t);
        }

        return sb.toString();
    }
}
