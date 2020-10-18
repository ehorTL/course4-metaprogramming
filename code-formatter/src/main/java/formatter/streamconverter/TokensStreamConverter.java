package formatter.streamconverter;

import com.yehorpolishchuk.lexercpp.token.Token;
import com.yehorpolishchuk.lexercpp.token.TokenNameAllowed;
import formatter.exceptions.ConverterException;
import jdk.vm.ci.code.site.Mark;
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

            if (curTokenName == TokenNameAllowed.COMMENT){
                if (curToken.getMeta().isFirstFromLineStart()){
                    addNewlineWithIndent();
                } else {
                    addSpaceToOutputStream();
                }

                addTokenToOutput(curToken);
                if (curToken.isSingleLineComment()){
                    addNewlineWithIndent();
                }
            } else if (curTokenName == TokenNameAllowed.PREPROCESSOR_DIR){
                addTokenToOutput(curToken);
                addNewlineWithIndent();
            } else if (curTokenName == TokenNameAllowed.KEYWORD) {
                if (curTokenValue.equals("operator")){
                    addTokenToOutput(curToken);
                    if (templateProperties.other_between_operator_keyword_and_punctuator){
                        addSpaceToOutputStream();
                    }
                } else if (curTokenValue.equals("typename")){
                    if (stack.peek().marker == StackMarker.TEMPLATE_LEFT_ANGLE_BRACKET){

                    }
                    addTokenToOutput(curToken);
//                    addTokenAndSpaceToOutput(curToken);
                } else if (curTokenValue.equals("template")){
                  stack.push(new StackUnit(StackMarker.TEMPLATE_DECL));
                  addTokenToOutput(curToken);
                } else if (curTokenValue.equals("class")) {
                    if (stack.peek().marker == StackMarker.TEMPLATE_LEFT_ANGLE_BRACKET) {
//                        stack.push(new StackUnit(StackMarker.CLASS_IN_TEMPLATE_DECL));
                    } else if (stack.peek().marker == StackMarker.TEMPLATE_RIGHT_ANGLE_BRACKET){
                        removeFromStack(2);
                        stack.push(new StackUnit(StackMarker.CLASS_DECL));
                    } else {
                        stack.push(new StackUnit(StackMarker.CLASS_DECL));
                    }
                    addTokenToOutput(curToken);
//                    addTokenAndSpaceToOutput(curToken);
//                    addSpaceToOutputStream();
                } else if (curTokenValue.equals("structure")) {
                    stack.push(new StackUnit(StackMarker.STRUCTURE_DECL));
                    addTokenToOutput(curToken);
                    addSpaceToOutputStream();
                } else if (curTokenValue.equals("if")){
                    stack.add(new StackUnit(StackMarker.IF));
                    addTokenToOutput(curToken);
                } else if (curTokenValue.equals("for")){
                    addTokenToOutput(curToken);
                    stack.push(new StackUnit(StackMarker.FOR));
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
                    if (this.templateProperties.before_switch_parentheses){
                        outputStream.add(new Token(TokenNameAllowed.SPACES, SPACE));
                    }
                } else if (curTokenValue.equals("try")){
                    stack.push(new StackUnit(StackMarker.TRY));
                    addTokenToOutput(curToken);
                } else if (curTokenValue.equals("catch")){
                    addTokenToOutput(curToken);
                    stack.push(new StackUnit(StackMarker.CATCH));
                } else if (curTokenValue.equals("namespace")){
                    this.stack.push(new StackUnit(StackMarker.NAMESPACE_DECL, lookahead_index));
                    addTokenToOutput(curToken);
                    addSpaceToOutputStream();
                } else if (curTokenValue.equals("do")) {
                    stack.push(new StackUnit(StackMarker.DO_WHILE_DO));
                    addTokenToOutput(curToken);
                } else {
                    addTokenToOutput(curToken);
                }
            } else if (curTokenName == TokenNameAllowed.IDENTIFIER) {
                if (stack.peek().marker == StackMarker.STRUCTURE_DECL) {
                    stack.pop();
                    stack.push(new StackUnit(StackMarker.STRUCTURE_NAME_DECL));
                } else if (stack.peek().marker == StackMarker.CLASS_DECL) {
                    stack.pop();
                    stack.push(new StackUnit(StackMarker.CLASS_NAME_DECL));
                } else if (stack.peek().marker == StackMarker.TEMPLATE_LEFT_ANGLE_BRACKET){
                    // do nothing
                } else {
                    addSpaceToOutputStream();
                }
                addSpaceToOutputStream();
                addTokenToOutput(curToken);
            } else if (curTokenName == TokenNameAllowed.OPERATOR) {
                if (curTokenValue.equals("<")){
                    if (stack.peek().marker == StackMarker.TEMPLATE_DECL) {
                        stack.pop();
                        stack.push(new StackUnit(StackMarker.TEMPLATE_LEFT_ANGLE_BRACKET));

                        if (templateProperties.template_before_left_angle_bracket) {
                            addSpaceToOutputStream();
                        }
                        addTokenToOutput(curToken);

                        Token nextToken = peekNextTokenIfExist();
                        if (nextToken != null) {
                            if (curToken.getValue().getValue().equals(">")){
                                if (templateProperties.template_within_empty_brackets) {
                                    addSpaceToOutputStream();
                                }
                            } else {
                                if (templateProperties.template_within_brackets){
                                    addSpaceToOutputStream();
                                }
                            }
                        }
                    } else {
                        if (templateProperties.around_relational_ops){
                            addSpaceToOutputStream();
                            addTokenToOutput(curToken);
                            addSpaceToOutputStream();
                        } else {
                            addTokenToOutput(curToken);
                        }
                    }
                } else if (curTokenValue.equals(">")){
                    if (stack.peek().marker == StackMarker.TEMPLATE_LEFT_ANGLE_BRACKET){
                        Token prevToken = peekPrevTokenIfExists();
                        if (prevToken != null){
                            if (!prevToken.getValue().getValue().equals("<")){
                                if (templateProperties.template_within_brackets){
                                    addSpaceToOutputStream();
                                }
                            }
                        }
                        stack.push(new StackUnit(StackMarker.TEMPLATE_RIGHT_ANGLE_BRACKET));
                        addTokenToOutput(curToken);
                        addNewlineWithIndent();
                    } else {
                        if (templateProperties.around_relational_ops){
                            addSpaceToOutputStream();
                            addTokenToOutput(curToken);
                            addSpaceToOutputStream();
                        } else {
                            addTokenToOutput(curToken);
                        }
                    }
                } else if (curTokenValue.equals(">>") || curTokenValue.equals("<<")) {
                    if (templateProperties.around_shift_ops) {
                        addSpaceToOutputStream();
                    }
                    addTokenToOutput(curToken);
                    if (templateProperties.around_shift_ops) {
                        addSpaceToOutputStream();
                    }
                } else if (curTokenValue.equals("?")){
                    stack.push(new StackUnit(StackMarker.TERNARY_OP));
                    if (templateProperties.in_tern_op_bf_qm){
                        addSpaceToOutputStream();
                    }
                    addTokenToOutput(curToken);
                    if (templateProperties.in_tern_op_af_qm){
                        addSpaceToOutputStream();
                    }
                } else if (curTokenValue.equals(":")) {
                    if (stack.peek().marker == StackMarker.CLASS_NAME_DECL) {
                        stack.push(new StackUnit(StackMarker.BASE_COLON));
                        if (templateProperties.class_structure_bf_base_class_colon){
                            addSpaceToOutputStream();
                        }
                        addTokenToOutput(curToken);
                        if (templateProperties.class_structure_af_base_class_colon){
                            addSpaceToOutputStream();
                        }
                    } else if (stack.peek().marker == StackMarker.STRUCTURE_NAME_DECL) {
                        stack.push(new StackUnit(StackMarker.BASE_COLON));
                        if (templateProperties.class_structure_bf_base_class_colon){
                            addSpaceToOutputStream();
                        }
                        addTokenToOutput(curToken);
                        if (templateProperties.class_structure_af_base_class_colon){
                            addSpaceToOutputStream();
                        }
                    } else if (stack.peek().marker == StackMarker.TERNARY_OP){
                        if (templateProperties.in_tern_op_bf_colon) {
                            addSpaceToOutputStream();
                        }
                        addTokenToOutput(curToken);
                        if (templateProperties.in_tern_op_af_colon) {
                            addSpaceToOutputStream();
                        }
                    }
                } else if (curTokenValue.equals("=") || curTokenValue.equals("+=") || curTokenValue.equals("-=") ||
                        curTokenValue.equals("*=") || curTokenValue.equals("/=")){
                    if (templateProperties.around_assignment_ops){
                        this.addSpaceToOutputStream();
                        addTokenToOutput(curToken);
                        this.addSpaceToOutputStream();
                    } else {
                        addTokenToOutput(curToken);
                    }
                } else if (curTokenValue.equals("&&") || curTokenValue.equals("||")){
                    if (templateProperties.around_logical_ops){
                        this.addSpaceToOutputStream();
                        addTokenToOutput(curToken);
                        this.addSpaceToOutputStream();
                    } else {
                        addTokenToOutput(curToken);
                    }
                } else if (curTokenValue.equals("==") || curTokenValue.equals("!=")) {
                    if (templateProperties.around_equality_ops){
                        this.addSpaceToOutputStream();
                        this.outputStream.add(curToken);
                        this.addSpaceToOutputStream();
                    } else {
                        this.outputStream.add(curToken);
                    }
                }
                // consider > and < as relational operators too
                else if (curTokenValue.equals("<=") || curTokenValue.equals(">=") || curTokenValue.equals("<=>")) {
                    if (templateProperties.around_relational_ops){
                        this.addSpaceToOutputStream();
                        this.outputStream.add(curToken);
                        this.addSpaceToOutputStream();
                    } else {
                        this.outputStream.add(curToken);
                    }
                } else if (curTokenValue.equals("/") || curTokenValue.equals("%")) {
                    if (templateProperties.around_multiplicative_ops){
                        this.addSpaceToOutputStream();
                        this.outputStream.add(curToken);
                        this.addSpaceToOutputStream();
                    } else {
                        this.addSpaceToOutputStream();
                    }
                } else if (curTokenValue.equals("*")) {
                    // TODO
                    // handle 4 cases
                    // templateProperties.around_multiplicative_ops
                    // templateProperties.other_after_asterisk_in_declarations
                    // templateProperties.other_before_asterisk_in_declarations
                    // templateProperties.other_after_dereference_and_address_of
                } else if (curTokenValue.equals(".") || curTokenValue.equals("->.") || curTokenValue.equals(".*")) {
                    if (templateProperties.around_pointer_to_member_ops_ops){
                        this.addSpaceToOutputStream();
                        this.outputStream.add(curToken);
                        this.addSpaceToOutputStream();
                    } else {
                        this.outputStream.add(curToken);
                    }
                } else if (curTokenValue.equals("->")){
                    // TODO
                    // handle 2 cases
                    // templateProperties.around_pointer_in_ret_type_ops
                    // templateProperties.around_pointer_to_member_ops_ops
                } else if (curTokenValue.equals("!") || curTokenValue.equals("-") || curTokenValue.equals("+") ||
                        curTokenValue.equals("++") || curTokenValue.equals("--")) {
                    if (isBinaryOperator(this.lookahead_index)){ // + or -
                        if (templateProperties.around_additive_ops){
                            addSpaceToOutputStream();
                            addTokenToOutput(curToken);
                            addSpaceToOutputStream();
                        } else {
                            addTokenToOutput(curToken);
                        }
                    } else {
                        if (templateProperties.around_unary_ops){
                            addSpaceToOutputStream();
                            outputStream.add(curToken);
                            addSpaceToOutputStream();
                        } else {
                            outputStream.add(curToken);
                        }
                    }
                } else if (curTokenValue.equals("|") || curTokenValue.equals("^")) {
                    if (templateProperties.around_bitwise_ops) {
                        addSpaceToOutputStream();
                        addTokenToOutput(curToken);
                        addSpaceToOutputStream();
                    } else {
                        addTokenToOutput(curToken);
                    }
                } else if (curTokenValue.equals("&")){
                    // TODO
                    // handle 4 cases:
                    // around_bitwise_ops and +
                    // templateProperties.other_after_dereference_and_address_of
                    // templateProperties.other_before_amp_in_declarations;
                    // templateProperties.other_after_amp_in_declarations;
                    if (false){} else {
                        if (templateProperties.around_bitwise_ops){
                            addSpaceToOutputStream();
                            addTokenToOutput(curToken);
                            addSpaceToOutputStream();
                        } else {
                            addTokenToOutput(curToken);
                        }
                    }
                }
            } else if (curTokenName == TokenNameAllowed.PUNCTUATOR) {
                if (curTokenValue.equals("[")){
                    addTokenToOutput(curToken);
                    if (templateProperties.within_array_brackets){
                        addSpaceToOutputStream();
                    }
                } else if (curTokenValue.equals("]")) {
                    if (templateProperties.within_array_brackets){
                        if (!prevTokenIsSpace()){
                            addSpaceToOutputStream();
                        }
                    }
                    addTokenToOutput(curToken);
                } else if (curTokenValue.equals(",")) {
                    addTokenToOutput(curToken);
                    if (templateProperties.other_after_comma){
                        addSpaceToOutputStream();
                    }
                } else if (curTokenValue.equals("(")){
                    if (stack.peek().marker == StackMarker.CATCH){
                        stack.pop();
                        stack.push(new StackUnit(StackMarker.CATCH_LEFT_PARENTH));
                        if (this.templateProperties.before_catch_parentheses){
                            addSpaceToOutputStream();
                        }
                        addTokenToOutput(curToken);
                        if (templateProperties.within_catch_parenth){
                            addSpaceToOutputStream();
                        }
                    } else if (stack.peek().marker == StackMarker.DO_WHILE_WHILE){
                        stack.pop();
                        stack.push(new StackUnit(StackMarker.DO_WHILE_PARENTH_LEFT));
                        if (templateProperties.before_while_parentheses){
                            addSpaceToOutputStream();
                        }
                    } else if (stack.peek().marker == StackMarker.WHILE){
                        stack.pop();
                        stack.push(new StackUnit(StackMarker.WHILE_LEFT_PARENTH));
                        if (templateProperties.before_while_parentheses){
                            addSpaceToOutputStream();
                        }
                        addTokenToOutput(curToken);
                        if (templateProperties.within_while_parenth){
                            addSpaceToOutputStream();
                        }
                    } else if (stack.peek().marker == StackMarker.FOR){
                        stack.pop();
                        stack.push(new StackUnit(StackMarker.FOR_LEFT_PARENTH));
                        if (this.templateProperties.before_for_parentheses){
                            addSpaceToOutputStream();
                        }
                        addTokenToOutput(curToken);
                        if (templateProperties.within_for_parenth){
                            addSpaceToOutputStream();
                        }
                    } else if (stack.peek().marker == StackMarker.IF){
                        stack.pop();
                        stack.push(new StackUnit(StackMarker.IF_PARENTH_LEFT));
                        if (templateProperties.before_if_parentheses){
                            addSpaceToOutputStream();
                        }
                        addTokenToOutput(curToken);
                        if (templateProperties.within_if_parenth){
                            addSpaceToOutputStream();
                        }
                    } else if (stack.peek().marker == StackMarker.SWITCH){
                        stack.pop();
                        stack.push(new StackUnit(StackMarker.IF_PARENTH_LEFT));
                        if (templateProperties.before_switch_parentheses){
                            addSpaceToOutputStream();
                        }
                        addTokenToOutput(curToken);
                        if (templateProperties.within_switch_parenth){
                            addSpaceToOutputStream();
                        }
                    } else if (stack.peek().marker == StackMarker.CATCH){
                        stack.pop();
                        stack.push(new StackUnit(StackMarker.CATCH_LEFT_PARENTH));
                        if (templateProperties.before_catch_parentheses){
                            addSpaceToOutputStream();
                        }
                    }
//                    addTokenToOutput(curToken);
                } else if (curTokenValue.equals(")")){
                    if (stack.peek().marker == StackMarker.DO_WHILE_PARENTH_LEFT){
                        stack.push(new StackUnit(StackMarker.DO_WHILE_PARENTH_RIGHT));
                    } else if (stack.peek().marker == StackMarker.IF_PARENTH_LEFT){
                        if (templateProperties.within_if_parenth){
                            addSpaceToOutputStream();
                        }
                        stack.push(new StackUnit(StackMarker.IF_PARENTH_RIGHT));
                    } else if (stack.peek().marker == StackMarker.FOR_SECOND_SEMICOLON){
                        removeFromStack(2); // ;;
                        if (templateProperties.within_for_parenth){
                            addSpaceToOutputStream();
                        }
                        stack.push(new StackUnit(StackMarker.FOR_RIGHT_PARENTH));
                    }
//                    else if (stack.peek().marker == StackMarker.FOR_LEFT_PARENTH){
//                        if (templateProperties.within_for_parenth){
//                            addSpaceToOutputStream();
//                        }
//                        stack.push(new StackUnit(StackMarker.FOR_RIGHT_PARENTH));
//                    }
                    else if (stack.peek().marker == StackMarker.WHILE_LEFT_PARENTH){
                        if (templateProperties.within_while_parenth){
                            addSpaceToOutputStream();
                        }
                        stack.push(new StackUnit(StackMarker.WHILE_RIGHT_PARENTH));
                    } else if (stack.peek().marker == StackMarker.CATCH_LEFT_PARENTH){
                        if (templateProperties.within_catch_parenth){
                            addSpaceToOutputStream();
                        }
                        stack.push(new StackUnit(StackMarker.CATCH_RIGHT_PARENTH));
                    } else if (stack.peek().marker == StackMarker.SWITCH_LEFT_PARENTH){
                        if (templateProperties.within_switch_parenth){
                            addSpaceToOutputStream();
                        }
                        stack.push(new StackUnit(StackMarker.SWITCH_RIGHT_PARENTH));
                    }
                    addTokenToOutput(curToken);
                }
                if (curTokenValue.equals(";")){
                    if (stack.peek().marker == StackMarker.FOR_FIRST_SEMICOLON) {
                        if (templateProperties.other_before_for_semicolon){
                            addSpaceToOutputStream();
                        }
                        addTokenToOutput(curToken);
                        if (templateProperties.other_after_for_semicolon) {
                            addSpaceToOutputStream();
                        }
                        stack.push(new StackUnit(StackMarker.FOR_SECOND_SEMICOLON));
                    } else if (stack.peek().marker == StackMarker.FOR_LEFT_PARENTH){
                        if (templateProperties.other_before_for_semicolon){
                            addSpaceToOutputStream();
                        }
                        addTokenToOutput(curToken);
                        if (templateProperties.other_after_for_semicolon) {
                            addSpaceToOutputStream();
                        }
                        stack.push(new StackUnit(StackMarker.FOR_FIRST_SEMICOLON));
                    } else if (stack.peek().marker == StackMarker.TERNARY_OP){
                        stack.pop();
                        addTokenToOutput(curToken);
                    } else if (stack.peek().marker == StackMarker.DO_WHILE_PARENTH_RIGHT){
                        stack.pop();
                        stack.pop();
                        addTokenToOutput(curToken);
                    }
                }

                if (curTokenValue.equals("{")) {
                    if (stack.peek().marker == StackMarker.CLASS_NAME_DECL){
                        stack.pop();
                        stack.push(new StackUnit(StackMarker.CLASS_DEF_LEFT_BRACE));
                        addTokenToOutput(curToken);
                        nestingLevel++;
                        addNewlineWithIndent();
                    } else if (stack.peek().marker == StackMarker.IF_PARENTH_RIGHT){
                        removeFromStack(2);
                        stack.push(new StackUnit(StackMarker.IF_LEFT_BRACE));
                        if (templateProperties.before_left_brace_if){
                            addSpaceToOutputStream();
                        }
                        addTokenToOutput(curToken);
                    } else if (stack.peek().marker == StackMarker.ELSE){
                        stack.pop();
                        stack.push(new StackUnit(StackMarker.ELSE_LEFT_BRACE));
                        if (templateProperties.before_left_brace_else){
                            addSpaceToOutputStream();
                        }
                        addTokenToOutput(curToken);
                    } else if (stack.peek().marker == StackMarker.FOR_RIGHT_PARENTH){
                           removeFromStack(2);
                           stack.push(new StackUnit(StackMarker.FOR_LEFT_BRACE));
                           if(templateProperties.before_left_brace_for){
                               addSpaceToOutputStream();
                           }
                           addTokenToOutput(curToken);
                    } else if (stack.peek().marker == StackMarker.WHILE_RIGHT_PARENTH){
                        removeFromStack(2);
                        stack.push(new StackUnit(StackMarker.WHILE_LEFT_BRACE));
                        if(templateProperties.before_left_brace_while){
                            addSpaceToOutputStream();
                        }
                        addTokenToOutput(curToken);
                    } else if (stack.peek().marker == StackMarker.SWITCH_RIGHT_PARENTH){
                        removeFromStack(2); // ()
                        stack.push(new StackUnit(StackMarker.SWITCH_LEFT_BRACE));
                        if (templateProperties.before_left_brace_switch){
                            addSpaceToOutputStream();
                        }
                        addTokenToOutput(curToken);
                    } else if (stack.peek().marker == StackMarker.CATCH_RIGHT_PARENTH){
                        removeFromStack(2); // ()
                        stack.push(new StackUnit(StackMarker.CATCH_LEFT_BRACE));
                        if (templateProperties.before_left_brace_catch){
                            addSpaceToOutputStream();
                        }
                        addTokenToOutput(curToken);
                    } else if (stack.peek().marker == StackMarker.TRY){
                        stack.pop();
                        stack.push(new StackUnit(StackMarker.TRY_LEFT_BRACE));
                        if (templateProperties.before_left_brace_try){
                            addSpaceToOutputStream();
                        }
                        addTokenToOutput(curToken);
                    } else if (this.stack.peek().marker == StackMarker.DO_WHILE_DO){
                        stack.push(new StackUnit(StackMarker.DO_WHILE_LFT_BR));
                        if (templateProperties.before_left_brace_do){
                            addSpaceToOutputStream();
                        }
                        addTokenToOutput(curToken);
                        nestingLevel++;
                        addNewlineWithIndent();
                    } else if (this.stack.peek().marker == StackMarker.NAMESPACE_DECL){
                        stack.pop();
                        stack.push(new StackUnit(StackMarker.NAMESPACE_LEFT_BRACE));
                        if (templateProperties.before_left_brace_namespace){
                            addSpaceToOutputStream();
                        }
                        addTokenToOutput(curToken);
                        nestingLevel++;
                        addNewlineWithIndent();
                    }
                } else if (curTokenValue.equals("}")) {
                    if (stack.peek().marker == StackMarker.CLASS_DEF_LEFT_BRACE){
                        this.stack.pop();
                        this.nestingLevel--;
                        addNewlineWithIndent();
                        addTokenToOutput(curToken);
                    } else if (this.stack.peek().marker == StackMarker.NAMESPACE_LEFT_BRACE){
                        this.stack.pop();
                        this.nestingLevel--;
                        addNewlineWithIndent();
                        addTokenToOutput(curToken);
                    } else if (stack.peek().marker == StackMarker.DO_WHILE_LFT_BR){
                        this.stack.push(new StackUnit(StackMarker.DO_WHILE_RHT_BR));
                        this.nestingLevel--;
                        addNewlineWithIndent();
                        addTokenToOutput(curToken);
                    }

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

    public void addTokenAndSpaceToOutput(Token token) {
        addTokenToOutput(token);
        addSpaceToOutputStream();
    }

    /**
     * @return null if not found any
     * */
    public Token peekNextTokenIfExist() {
        return this.getOrNullTokenWithIndex(this.lookahead_index + 1);
    }

    public Token peekPrevTokenIfExists(){
        return this.getOrNullTokenWithIndex(this.lookahead_index - 1);
    }

    public Token getOrNullTokenWithIndex(int index) {
        if (index < this.inputStream.size() && index >= 0) {
            return this.inputStream.get(index);
        }

        return null;
    }

    private void removeFromStackLastElems(Stack stack, int quantity) {
        for (int i=0; i<stack.size() && i<quantity; i++){
            stack.pop();
        }
    }

    public void removeFromStack(int quantity){
        removeFromStackLastElems(this.stack, quantity);
    }

    public boolean prevTokenIsSpace(){
        Token prev = peekNextTokenIfExist();
        if (prev == null){
            return false;
        }

        if (prev.getValue().getValue().equals(SPACE)){
            return true;
        }

        return false;
    }

    /**
     * "+" and "-" can be as binary as unary ops depending on context
     * "+" and "-" are unary if they follows identifier, ), ], any literal
     * */
    public boolean isBinaryOperator(int tokenPosition){
        Token curToken = getOrNullTokenWithIndex(tokenPosition);
        if (curToken == null) return false;
        if (!curToken.isOperator()) return false;

        String tokenValue = curToken.getValue().getValue();
        Token prevToken = getOrNullTokenWithIndex(tokenPosition - 1);
        if (prevToken == null) return false;

        if (tokenValue.equals("+") || tokenValue.equals("-")) {
            if (prevToken.getName().getTokenName() == TokenNameAllowed.IDENTIFIER || prevToken.isLiteral() ||
            prevToken.getValue().getValue().equals(")") || prevToken.getValue().getValue().equals("]")) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    /**
     * Unary operators can be: + - ++ -- !
     * ++, --, ! - always unary
     * + and - can be as binary as unary depending on context
     * If they are not binary, we consider them as unary
     * */
    public boolean isUnaryOperator(int tokenPosition){
        Token curToken = getOrNullTokenWithIndex(tokenPosition);
        if (curToken == null) return false;
        if (!curToken.isOperator()) return false;

        String tokenValue = curToken.getValue().getValue();
        if (tokenValue.equals("!") || tokenValue.equals("++") || tokenValue.equals("--")) {
            return true;
        } else if (tokenValue.equals("+") || tokenValue.equals("-")) {
            return !isBinaryOperator(tokenPosition);
        }

        return false;
    }

    /**
     * Finds last token in the output that is not of type SPACES
     * */
    private Token lastNonSpaceTokenInOutput(){
        if (outputStream.size() == 0){
            return null;
        }

        Token last = null;
        int lastIndex = outputStream.size() - 1;
        for (; lastIndex>=0 && (last.getName().getTokenName() == TokenNameAllowed.SPACES); lastIndex--){
            last = outputStream.get(lastIndex);
        }

        if (last.getName().getTokenName() == TokenNameAllowed.SPACES){
            return null;
        }

        return last;
    }

    private Token lastNonSpaceNonCommentTokenInOutput(){
        if (outputStream.size() == 0){
            return null;
        }

        Token last = null;
        int lastIndex = outputStream.size() - 1;
        for (; lastIndex>=0 && ((last.getName().getTokenName() == TokenNameAllowed.SPACES)
                || last.getName().getTokenName() == TokenNameAllowed.COMMENT); lastIndex--){
            last = outputStream.get(lastIndex);
        }

        if (last.getName().getTokenName() == TokenNameAllowed.SPACES
                || last.getName().getTokenName()== TokenNameAllowed.COMMENT){
            return null;
        }

        return last;
    }

    /**
     * Checks if last not spacing token equals "}".
     * Can be used when
     * */
    private boolean isLastTokenRightBrace(){
        Token last = lastNonSpaceTokenInOutput();
        if (last == null) {
            return false;
        }

        if (last.getValue().getValue().equals("}")){
            return true;
        }

        return false;
    }



}
