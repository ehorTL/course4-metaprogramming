package formatter.streamconverter;

import com.yehorpolishchuk.lexercpp.token.Token;
import com.yehorpolishchuk.lexercpp.token.TokenName;
import com.yehorpolishchuk.lexercpp.token.TokenNameAllowed;
import com.yehorpolishchuk.lexercpp.token.TokenValue;
import formatter.exceptions.ConverterException;

import java.util.ArrayList;
import java.util.Stack;

public class TokensStreamConverter {
    private ArrayList<Token> inputStream;

    private ArrayList<Token> outputStream;
    private int lookahead_index;
    private Stack<StackUnit> stack;
    private int nestingLevel = 0;
    private int nestingBraces = 0;

    private static final String NEWLINE = "\n";
    private static final String SPACE = " ";
    private  static final String TAB = "\t";

    public TokensStreamConverter(ArrayList<Token> inputStream){
        this.inputStream = inputStream;
        this.outputStream = new ArrayList<>();
        this.lookahead_index = 0;
        this.nestingBraces = 0;
        this.nestingLevel = 0;
    }

    public ArrayList<Token> getTokens() {
        return this.outputStream;
    }

    public void convert() throws ConverterException {
        Token curToken = null;

        while (lookahead_index != this.inputStream.size()) {
            curToken = this.inputStream.get(lookahead_index);
            String curTokenValue = curToken.getValue().getValue();
            TokenNameAllowed curTokenName = curToken.getName().getTokenName();

            if (curTokenName == TokenNameAllowed.PUNCTUATOR) {
                if (curTokenValue.equals("{")) {
                    this.outputStream.add(new Token(TokenNameAllowed.SPACES, NEWLINE.concat(this.getCurrentIndent())));
                    this.outputStream.add(curToken);
                    this.nestingLevel++;
                    this.outputStream.add(new Token(TokenNameAllowed.SPACES, NEWLINE.concat(this.getCurrentIndent())));
                } else if (curTokenValue.equals("}")) {
                    this.nestingLevel--;
                    this.outputStream.add(new Token(TokenNameAllowed.SPACES, NEWLINE.concat(this.getCurrentIndent())));
                    this.outputStream.add(curToken);
                    this.outputStream.add(new Token(TokenNameAllowed.SPACES, NEWLINE.concat(this.getCurrentIndent())));
                } else if (curTokenValue.equals(";")) {
                    this.outputStream.add(curToken);
                    this.outputStream.add(new Token(TokenNameAllowed.SPACES, NEWLINE.concat(this.getCurrentIndent())));
                }
             } else if (curTokenName == TokenNameAllowed.KEYWORD) {
                if (curTokenValue.equals("for")) {
                    handleFor();
                } else if (true) {

                }
            }

            this.lookahead_index++; // ?
        }

    }

    public void handleLeftBrace() {
        this.outputStream.add(new Token(TokenNameAllowed.PUNCTUATOR,"("));
        this.nestingBraces++;
        lookahead_index++;
    }

    public void handleRightBrace() throws ConverterException {
        if (this.nestingBraces != 0) {
            this.outputStream.add(new Token(TokenNameAllowed.PUNCTUATOR,"("));
            this.nestingBraces--;
            lookahead_index++;
        } else {
            throw new ConverterException("nesting braces error");
        }
    }

    public void handleFor() throws ConverterException {
//        this.stack.push(new Token(TokenNameAllowed.KEYWORD, "for"));
        handleLeftBrace();
        forInstr1();
        semicolon();
        forInstr2();
        semicolon();
        forInstr3();
        handleRightBrace();
//        this.stack.pop();
    }

    public void varDecl() {

    }

    public void forInstr1(){
        instruction();
    }
    public void forInstr2(){
        boolExpr();
    }
    public void forInstr3(){
        instruction();
    }

    public void semicolon(){
        this.outputStream.add(new Token(TokenNameAllowed.PUNCTUATOR, ";"));
        this.lookahead_index++;
    }

    public void boolExpr(){

    }

    public void instruction(){

    }

    public void handleWhile(){}

    public String getCurrentIndent() {
        StringBuilder sb = new StringBuilder("");
        for (int i=0; i<this.nestingLevel; i++){
            sb.append(TAB);
        }
        return sb.toString();
    }
}
