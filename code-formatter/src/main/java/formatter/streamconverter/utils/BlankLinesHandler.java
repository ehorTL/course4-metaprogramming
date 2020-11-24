package formatter.streamconverter.utils;

import com.yehorpolishchuk.lexercpp.token.Token;
import com.yehorpolishchuk.lexercpp.token.TokenMetadata;
import com.yehorpolishchuk.lexercpp.token.TokenNameAllowed;
import templatereader.TemplateProperties;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BlankLinesHandler {
    private TemplateProperties templateProperties;
    private ArrayList<Token> inputTokens;
    private StringBuilder valueBuffer;
    private ArrayList<Token> commentHandlerTokens;
    private ArrayList<Token> tokensBuffer;
    private TokenMetadata tokenMetadata;
    private int state;
    private int curInd;


    public BlankLinesHandler(TemplateProperties templateProperties, ArrayList<Token> tokens) {
        this.templateProperties = templateProperties;
        this.inputTokens = tokens;
        this.valueBuffer = new StringBuilder("");
        this.commentHandlerTokens = new ArrayList<>();
        this.state = 0;
        this.curInd = 0;
        this.tokensBuffer = new ArrayList<>();
        this.tokenMetadata = null;
    }


    private ArrayList<Boolean> helperGetBooleanArray(int quantity, int falseItemsNumber) {
        ArrayList<Boolean> ret = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            ret.add(i < (quantity - falseItemsNumber));
        }

        return ret;
    }

    /**
     * Corrects token array by removing redundant blank lines according to template properties.
     */
    private ArrayList<Token> sanitizeMaxBlankLines(ArrayList<Token> tokens) {
        ArrayList<Boolean> toRemove = new ArrayList<>();

        int ind = 0;
        int state = 0;
        int blankLinesCounter = 0;
        Token curToken = null;
        while (ind < tokens.size()) {
            curToken = tokens.get(ind);
            switch (state) {
                case 0:
                    if (curToken.isBlankLine()) {
                        state = 1;
                        blankLinesCounter++;
                    } else {
                        toRemove.add(false);
                    }
                    break;
                case 1:
                    if (curToken.isBlankLine()) {
                        blankLinesCounter++;
                    } else {
                        ArrayList<Boolean> addToRemoveArray = null;
                        if (curToken.getValue().getValue().equals("}")) {
                            addToRemoveArray = helperGetBooleanArray(blankLinesCounter,
                                    Math.min(Math.min(templateProperties.keep_max_blank_lines_in_code,
                                            templateProperties.keep_max_blank_lines_in_before_right_brace), blankLinesCounter));
                        } else {
                            addToRemoveArray = helperGetBooleanArray(blankLinesCounter,
                                    Math.min(templateProperties.keep_max_blank_lines_in_code, blankLinesCounter));
                        }
                        toRemove.addAll(addToRemoveArray);
                        state = 0;
                        ind--;
                        blankLinesCounter = 0;
                    }
                    break;
            }

            ind++;
        }

        ArrayList<Token> ans = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            if (!toRemove.get(i)) {
                ans.add(tokens.get(i));
            }
        }
        return ans;

    }


    private Token nextNotBlankLineToken(ArrayList<Token> tokens, int currentIndex) {
        for (int i = currentIndex + 1; i < tokens.size(); i++) {
            if (!tokens.get(i).isBlankLine()) {
                return tokens.get(i);
            }
        }

        return null;
    }

    private Token prevNotBlankLineToken(ArrayList<Token> tokens, int currentIndex) {
        for (int i = currentIndex - 1; i >= 0; i--) {
            if (!tokens.get(i).isBlankLine()) {
                return tokens.get(i);
            }
        }

        return null;
    }

    /**
     * @param before if true then counting before, otherwise - after
     */
    private int countBlankLines(ArrayList<Token> tokens, int currentIndex, boolean before) {
        int counter = 0;
        if (before) {
            for (int i = currentIndex - 1; i >= 0; i--) {
                if (tokens.get(i).isBlankLine()) {
                    counter++;
                } else {
                    break;
                }
            }
        } else {
            for (int i = currentIndex + 1; i < tokens.size(); i++) {
                if (tokens.get(i).isBlankLine()) {
                    counter++;
                } else {
                    break;
                }
            }
        }

        return counter;
    }

    /**
     * Minimun blank lines before and after includes settings
     */
    private ArrayList<Token> applyIncludesSettings(ArrayList<Token> tokens) {
        ArrayList<Token> ans = new ArrayList<>();
        ArrayList<Integer> addBLAfter = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            addBLAfter.add(0);
        }

        Token curToken = null, prevToken = null, nextToken = null;
        int blBefore = 0, blAfter = 0;
        for (int i = 0; i < tokens.size(); i++) {
            curToken = tokens.get(i);
            if (curToken.isIncludeDirective()) {
                prevToken = prevNotBlankLineToken(tokens, i);
                nextToken = nextNotBlankLineToken(tokens, i);
                blBefore = countBlankLines(tokens, i, true);
                blAfter = countBlankLines(tokens, i, false);
                if (templateProperties.minimum_blank_lines_before_includes > blBefore) {
                    if (i != 0) {
                        if (prevToken != null && !prevToken.isComment()){
                            addBLAfter.set(i-1, Math.max(templateProperties.minimum_blank_lines_after_includes - blBefore, addBLAfter.get(i-1)));
                        }
                    }
                }
                if (templateProperties.minimum_blank_lines_after_includes > blAfter) {
                    if (i != tokens.size() - 1) {
                        if (nextToken != null && !nextToken.isComment()){
                            addBLAfter.set(i, Math.max(templateProperties.minimum_blank_lines_after_includes - blAfter, addBLAfter.get(i)));
                        }
                    }
                }
            }
        }

        for (int i=0; i<addBLAfter.size(); i++){
            ans.add(tokens.get(i));
            int linesToAdd =addBLAfter.get(i);
            for (int j=0; j<linesToAdd; j++){
                ans.add(new Token(TokenNameAllowed.BLANK_LINE, "\n"));
            }
        }

        return ans;
    }

    private ArrayList<Token> sanitizeMinimumBlankLines(ArrayList<Token> tokens) {
        ArrayList<Token> ans = applyIncludesSettings(tokens);
        ans = applyIncludesSettings(ans);

        return ans;
    }

    private ArrayList<Token> removeFirstBlankLines(ArrayList<Token> tokens) {
        ArrayList<Token> sanitized = new ArrayList<>();
        boolean fromStart = true;
        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            if (fromStart) {
                if (!t.isBlankLine()) {
                    fromStart = false;
                    sanitized.add(t);
                }
            } else {
                sanitized.add(t);
            }
        }

        return sanitized;
    }

    /**
     * Handles the cases from template properties file for input tokens.
     * To be called after comments handling!
     * Should implement:
     * 1) removers first blank lines (at the beginning of the file)
     * 2) glues comments wit neighbouring blank lines
     * 3) minimum blank lines handling
     * 4) maximum blank lines handling (sanitizeMaxBlankLines)!
     */
    public ArrayList<Token> handleBlankLines() {
        this.inputTokens = removeFirstBlankLines(this.inputTokens);
        ArrayList<Token> tokensCommmentFixed = getTokensWithHandledComments();
        ArrayList<Token> tokensMinBlankLinesFixed = sanitizeMinimumBlankLines(tokensCommmentFixed);
        ArrayList<Token> tokensMaxBlankLinesFixed = sanitizeMaxBlankLines(tokensMinBlankLinesFixed);

        return tokensMaxBlankLinesFixed;
    }

    public ArrayList<Token> getTokensWithHandledComments() {
        this.handleComments();
        return this.commentHandlerTokens;
    }

    private void handleComments() {
        this.commentHandlerTokens = new ArrayList<>();
        this.valueBuffer = new StringBuilder("");
        this.state = 0;
        this.curInd = 0;

        Token curToken = null;

        while (curInd < inputTokens.size()) {
            curToken = this.inputTokens.get(curInd);
            switch (state) {
                case 0:
                    startState(curToken);
                    break;
                case 1:
                    blankLine1(curToken);
                    break;
                case 2:
                    comment2(curToken);
                    break;
            }

            curInd++;
        }
    }

    private void startState(Token t) {
        if (t.isBlankLine()) {
            moveAndAddToBuffer(t, 1);
        } else if (t.isComment()) {
            moveAndAddToBuffer(t, 2);
        } else {
            addTokenAndClearBuffer(t, 0);
        }
    }

    private void blankLine1(Token t) {
        if (t.isBlankLine()) {
            moveAndAddToBuffer(t, 1);
        } else if (t.isComment()) {
            moveAndAddToBuffer(t, 2);
        } else {
            addTokensFromBufferAndClear(0);
            this.curInd--;
        }
    }

    private void comment2(Token t) {
        if (t.isBlankLine()) {
            moveAndAddToBuffer(t, 2);
        } else {
            Token newToken = new Token(TokenNameAllowed.COMMENT, this.valueBuffer.toString());
            newToken.setMeta(this.tokenMetadata);
            addTokenAndClearBuffer(newToken, 0);
            this.curInd--;
        }
    }

    private void moveAndAddToBuffer(Token t, int newState) {
        this.tokensBuffer.add(t);
        this.valueBuffer.append(t.getValue().getValue());
        if (t.isComment()) {
            this.tokenMetadata = t.getMeta();
        }
        this.state = newState;
    }

    private void addTokenAndClearBuffer(Token token, int newState) {
        this.commentHandlerTokens.add(token);
        this.valueBuffer = new StringBuilder("");
        this.tokensBuffer = new ArrayList<>();
        this.tokenMetadata = null;
        this.state = newState;
    }

    private void addTokensFromBufferAndClear(int newState) {
        this.commentHandlerTokens.addAll(this.tokensBuffer);
        this.valueBuffer = new StringBuilder("");
        this.tokensBuffer = new ArrayList<>();
        this.tokenMetadata = null;
        this.state = newState;
    }
}
