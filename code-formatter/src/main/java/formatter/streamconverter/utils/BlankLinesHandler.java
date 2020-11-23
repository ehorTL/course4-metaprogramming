package formatter.streamconverter.utils;
import com.yehorpolishchuk.lexercpp.token.Token;
import com.yehorpolishchuk.lexercpp.token.TokenMetadata;
import com.yehorpolishchuk.lexercpp.token.TokenNameAllowed;
import templatereader.TemplateProperties;
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


    public BlankLinesHandler(TemplateProperties templateProperties, ArrayList<Token> tokens){
        this.templateProperties = templateProperties;
        this.inputTokens = tokens;
        this.valueBuffer = new StringBuilder("");
        this.commentHandlerTokens = new ArrayList<>();
        this.state = 0;
        this.curInd = 0;
        this.tokensBuffer = new ArrayList<>();
        this.tokenMetadata = null;
    }

    public ArrayList<Token> handleBlankLines(){
        ArrayList<Token> tokensHanled = new ArrayList<>();

        int nestedLevel = 0;
        ArrayList<Boolean> toRemove = new ArrayList<>(), toAddAfterCurrent = new ArrayList<>();
        for (int i=0; i<inputTokens.size(); i++){
            toRemove.add(false);
            toAddAfterCurrent.add(false);
        }

        int curInd = 0;
        Token curToken = null, prevToken = null;
        while (curInd < inputTokens.size()){
            curToken = inputTokens.get(curInd);
            if (prevToken == null){
                tokensHanled.add(curToken);
                curInd++;
                continue;
            }

            if (curToken.isIncludeDirective()){
                if (prevToken.isIncludeDirective()){
                    tokensHanled.add(curToken);
                } else if (prevToken.isComment()){

                }
            } else {
                tokensHanled.add(curToken);
            }

            curInd++;
        }

        return tokensHanled;
    }

    public ArrayList<Token> getTokensWithHandledComments(){
        this.handleComments();
        return this.commentHandlerTokens;
    }

    private void handleComments(){
        this.commentHandlerTokens = new ArrayList<>();
        this.valueBuffer = new StringBuilder("");
        this.state = 0;
        this.curInd = 0;

        Token curToken = null;

        while (curInd < inputTokens.size()){
            curToken = this.inputTokens.get(curInd);
            switch (state){
                case 0: startState(curToken); break;
                case 1: blankLine1(curToken); break;
                case 2: comment2(curToken); break;
            }

            curInd++;
        }
    }

    private void startState(Token t){
        if (t.isBlankLine()){
            moveAndAddToBuffer(t, 1);
        } else if (t.isComment()){
            moveAndAddToBuffer(t,2);
        } else {
            addTokenAndClearBuffer(t, 0);
        }
    }

    private void blankLine1(Token t){
        if (t.isBlankLine()){
            moveAndAddToBuffer(t, 1);
        } else if (t.isComment()) {
            moveAndAddToBuffer(t, 2);
        } else {
            addTokensFromBufferAndClear(0);
            this.curInd--;
        }
    }

    private void comment2(Token t){
        if (t.isBlankLine()){
            moveAndAddToBuffer(t, 2);
        } else {
            Token newToken = new Token(TokenNameAllowed.COMMENT, this.valueBuffer.toString());
            newToken.setMeta(this.tokenMetadata);
            addTokenAndClearBuffer(newToken, 0);
            this.curInd--;
        }
    }

    private void moveAndAddToBuffer(Token t, int newState){
        this.tokensBuffer.add(t);
        this.valueBuffer.append(t.getValue().getValue());
        if (t.isComment()){
            this.tokenMetadata = t.getMeta();
        }
        this.state = newState;
    }

    private void addTokenAndClearBuffer(Token token, int newState){
        this.commentHandlerTokens.add(token);
        this.valueBuffer = new StringBuilder("");
        this.tokensBuffer = new ArrayList<>();
        this.tokenMetadata = null;
        this.state = newState;
    }

    private void addTokensFromBufferAndClear(int newState){
        this.commentHandlerTokens.addAll(this.tokensBuffer);
        this.valueBuffer = new StringBuilder("");
        this.tokensBuffer = new ArrayList<>();
        this.tokenMetadata = null;
        this.state = newState;
    }
}
