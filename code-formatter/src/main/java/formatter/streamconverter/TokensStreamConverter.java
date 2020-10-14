package formatter.streamconverter;

import com.yehorpolishchuk.lexercpp.token.Token;
import com.yehorpolishchuk.lexercpp.token.TokenName;
import com.yehorpolishchuk.lexercpp.token.TokenNameAllowed;
import com.yehorpolishchuk.lexercpp.token.TokenValue;
import formatter.exceptions.ConverterException;
import templatereader.TemplateProperties;

import java.util.ArrayList;
import java.util.Stack;

public class TokensStreamConverter {
    private ArrayList<Token> inputStream;

    private ArrayList<Token> outputStream;
    private int lookahead_index;
    private Stack<StackUnit> stack;
    private int nestingLevel = 0;
    private int nestingBraces = 0;

    private TemplateProperties templateProperties;
    private static final String NEWLINE = "\n";
    private static final String SPACE = " ";
    private  static final String TAB = "\t";

    public TokensStreamConverter(ArrayList<Token> inputStream){
        this.inputStream = inputStream;
        this.outputStream = new ArrayList<>();
        this.lookahead_index = 0;
        this.nestingBraces = 0;
        this.nestingLevel = 0;
        this.stack = new Stack<>();
    }

    public TokensStreamConverter(ArrayList<Token> inputStream, TemplateProperties templateProperties){
        this.inputStream = inputStream;
        this.outputStream = new ArrayList<>();
        this.lookahead_index = 0;
        this.nestingBraces = 0;
        this.nestingLevel = 0;
        this.templateProperties = templateProperties;
        this.stack = new Stack<>();
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

            if (curTokenName == TokenNameAllowed.KEYWORD) {
                if (curTokenValue.equals("if")){
                    outputStream.add(curToken);
                    if (this.templateProperties.if_parentheses){
                        outputStream.add(new Token(TokenNameAllowed.SPACES, SPACE));
                    }
                } else if (curTokenValue.equals("for")){
                    outputStream.add(curToken);
                    if (this.templateProperties.for_parentheses){
                        outputStream.add(new Token(TokenNameAllowed.SPACES, SPACE));
                    }
                } else if (curTokenValue.equals("while")){
                    if (stack.peek().marker == StackMarker.DO_WHILE_RHT_BR){
                        stack.pop();
                        stack.pop();
                        stack.pop();
                        stack.push(new StackUnit(StackMarker.DO_WHILE_WHILE));
                        if (templateProperties.before_while) {
                            addSpaceToOutputStream();
                        }
                    } else {
                        stack.push(new StackUnit(StackMarker.WHILE));
                    }
                    addTokenToOutput(curToken);
                } else if (curTokenValue.equals("switch")){
                    outputStream.add(curToken);
                    if (this.templateProperties.switch_parentheses){
                        outputStream.add(new Token(TokenNameAllowed.SPACES, SPACE));
                    }
                } else if (curTokenValue.equals("catch")){
                    outputStream.add(curToken);
                    if (this.templateProperties.catch_parentheses){
                        outputStream.add(new Token(TokenNameAllowed.SPACES, SPACE));
                    }
                } else if (curTokenValue.equals("namespace")){
                    this.stack.push(new StackUnit(StackMarker.NAMESPACE, lookahead_index));
                    this.outputStream.add(curToken);
                } else if (curTokenValue.equals("class")){
                    stack.push(new StackUnit(StackMarker.CLASS));
                } else if (curTokenValue.equals("do")) {
                    stack.push(new StackUnit(StackMarker.DO_WHILE_DO));
                    addTokenToOutput(curToken);
                }
            } else if (curTokenName == TokenNameAllowed.IDENTIFIER) {

            } else if (curTokenName == TokenNameAllowed.OPERATOR) {
                if (curTokenValue.equals("=") || curTokenValue.equals("+=") || curTokenValue.equals("-=") ||
                        curTokenValue.equals("*=") || curTokenValue.equals("/=")){
                    if (templateProperties.around_assignment_ops){
                        this.addSpaceToOutputStream();
                        this.outputStream.add(curToken);
                        this.addSpaceToOutputStream();
                    } else {
                        this.outputStream.add(curToken);
                    }
                } else if (curTokenValue.equals("&&") || curTokenValue.equals("||")){
                    if (templateProperties.around_logical_ops){
                        this.addSpaceToOutputStream();
                        this.outputStream.add(curToken);
                        this.addSpaceToOutputStream();
                    } else {
                        this.outputStream.add(curToken);
                    }
                } else if (curTokenValue.equals("==") || curTokenValue.equals("!=")) {
                    if (templateProperties.around_equality_ops){
                        this.addSpaceToOutputStream();
                        this.outputStream.add(curToken);
                        this.addSpaceToOutputStream();
                    } else {
                        this.outputStream.add(curToken);
                    }
                } else if (curTokenValue.equals("<") || curTokenValue.equals(">") || curTokenValue.equals("<=") ||
                        curTokenValue.equals(">=") || curTokenValue.equals("<=>")) {
                    if (templateProperties.around_relational_ops){
                        this.addSpaceToOutputStream();
                        this.outputStream.add(curToken);
                        this.addSpaceToOutputStream();
                    } else {
                        this.outputStream.add(curToken);
                    }
                } else if (curTokenValue.equals("*") || curTokenValue.equals("/") || curTokenValue.equals("%")) {
                    if (templateProperties.around_multiplicative_ops){
                        this.addSpaceToOutputStream();
                        this.outputStream.add(curToken);
                        this.addSpaceToOutputStream();
                    } else {
                        this.addSpaceToOutputStream();
                    }
                } else if (curTokenValue.equals("->") || curTokenValue.equals(".") || curTokenValue.equals("->.") ||
                        curTokenValue.equals(".*")) {
                    if (templateProperties.around_pointer_to_member_ops_ops){
                        this.addSpaceToOutputStream();
                        this.outputStream.add(curToken);
                        this.addSpaceToOutputStream();
                    } else {
                        this.outputStream.add(curToken);
                    }
                } else if (curTokenValue.equals("!") || curTokenValue.equals("-") || curTokenValue.equals("+") ||
                        curTokenValue.equals("++") || curTokenValue.equals("--")) {
                    if (templateProperties.around_unary_ops){
                        this.addSpaceToOutputStream();
                        this.outputStream.add(curToken);
                        this.addSpaceToOutputStream();
                    } else {
                        this.outputStream.add(curToken);
                    }
                }
            } else if (curTokenName == TokenNameAllowed.PUNCTUATOR) {
                if (curTokenValue.equals("(")){
                    if (stack.peek().marker == StackMarker.DO_WHILE_WHILE){
                        stack.pop();
                        stack.push(new StackUnit(StackMarker.DO_WHILE_PARENTH_LEFT));
                        if (templateProperties.while_parentheses){
                            addSpaceToOutputStream();
                        }
                    } else if (stack.peek().marker == StackMarker.WHILE){

                    }
                    addTokenToOutput(curToken);
                }
                if (curTokenValue.equals(")")){
                    if (stack.peek().marker == StackMarker.DO_WHILE_PARENTH_LEFT){
                        stack.push(new StackUnit(StackMarker.DO_WHILE_PARENTH_RIGHT));
                    }
                    addTokenToOutput(curToken);
                }
                if (curTokenValue.equals(";")){
                    if (stack.peek().marker == StackMarker.DO_WHILE_PARENTH_RIGHT){
                        stack.pop();
                        stack.pop();
                    }
                    addTokenToOutput(curToken);
                    addNewlineWithIndent();
                }

                if (curTokenValue.equals("{")) {
                    if (this.stack.peek().marker == StackMarker.DO_WHILE_DO){
                        stack.push(new StackUnit(StackMarker.DO_WHILE_LFT_BR));
                        if (templateProperties.before_left_brace_do){
                            addSpaceToOutputStream();
                        }
                        addTokenToOutput(curToken);
                        nestingLevel++;
                        addNewlineWithIndent();
                    } else if (this.stack.peek().marker == StackMarker.NAMESPACE){
                        if (templateProperties.namespace){
                            addSpaceToOutputStream();
                        }
                    }
                } else if (curTokenValue.equals("}")) {
                    if (this.stack.peek().marker == StackMarker.NAMESPACE){
                        this.stack.pop();
                    }

                    if (stack.peek().marker == StackMarker.DO_WHILE_LFT_BR){
                        this.stack.push(new StackUnit(StackMarker.DO_WHILE_RHT_BR));
                        this.nestingLevel--;
                        addNewlineWithIndent();
                        addTokenToOutput(curToken);
                    }

//                    this.outputStream.add(new Token(TokenNameAllowed.SPACES, NEWLINE.concat(this.getCurrentIndent())));
//                    this.outputStream.add(curToken);
//                    this.outputStream.add(new Token(TokenNameAllowed.SPACES, NEWLINE.concat(this.getCurrentIndent())));
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
//        semicolon();
        forInstr2();
//        semicolon();
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

//    public void semicolon(){
//        this.outputStream.add(new Token(TokenNameAllowed.PUNCTUATOR, ";"));
//        this.lookahead_index++;
//    }

    public void boolExpr(){

    }

    public void instruction(){

    }

    public void handleWhile(){}

    public void addSpaceToOutputStream(){
        this.outputStream.add(new Token(TokenNameAllowed.SPACES, SPACE));
    }

    public String getCurrentIndent() {
        StringBuilder sb = new StringBuilder("");
        for (int i=0; i<this.nestingLevel; i++){
            sb.append(TAB);
        }
        return sb.toString();
    }

    public void addNewlineWithIndent() {
        this.outputStream.add(new Token(TokenNameAllowed.SPACES, NEWLINE.concat(this.getCurrentIndent())));
    }

    public void addTokenToOutput(Token token){
        this.outputStream.add(token);
    }

}
