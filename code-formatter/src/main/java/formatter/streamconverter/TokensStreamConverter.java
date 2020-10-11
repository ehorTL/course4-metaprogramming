package formatter.streamconverter;

import com.yehorpolishchuk.lexercpp.token.Token;

import java.util.ArrayList;

public class TokensStreamConverter {
    private ArrayList<Token> outputStream;
    private ArrayList<Token> inputStream;

    public TokensStreamConverter(ArrayList<Token> inputStream){
        this.inputStream = inputStream;
        this.outputStream = new ArrayList<>();
    }

    public ArrayList<Token> getTokens() {
        return this.outputStream;
    }

    public void convert() {
        return;
    }
}
