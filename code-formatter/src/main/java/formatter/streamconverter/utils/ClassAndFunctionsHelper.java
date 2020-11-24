package formatter.streamconverter.utils;

import com.yehorpolishchuk.lexercpp.token.Token;
import formatter.streamconverter.StackUnit;
import javafx.util.Pair;

import java.util.ArrayList;

public class ClassAndFunctionsHelper {
    public ArrayList<StackUnit> units;
    public ArrayList<int[]> unitStartEnd;
    public ArrayList<Token> tokens;

    public ClassAndFunctionsHelper(ArrayList<Token> tokens) {
        this.tokens = tokens;
        this.units = new ArrayList<>();
        this.unitStartEnd = new ArrayList<>();
    }

    public void markup() {
        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            if (t.isKeyword() && t.getValue().getValue().equals("class")) {
                // todo
            } else if (false) {

            }
        }
    }

    /**
     * Returns -1 if "class" used in template context (eg template<CLASS T>)
     * Returns index>=0 where the class declaration (or definition) starts
     * The method do 2 works:
     * 1) classify class context ()
     * 2) finds the index it starts
     */
    public static int classStartsFromIndex(ArrayList<Token> tokens, int currentIndex) {
        int index = -1;
        Token t = null;
        // case 1
        for (int i = currentIndex - 1; i >= 0; i--) {
            t = tokens.get(i);
            if (t.isBlankLine()) { // or whitespaces?
                continue;
            } else if (t.isOperator() && (t.getValue().getValue().equals(">") || t.getValue().getValue().equals(">>"))) {
                continue;
            } else if (t.isKeyword() && t.getValue().getValue().equals("template")) {
                index = i;
                break;
            } else {
                break;
            }
        }

        // case 2
        if (index == -1) {
            t = null;
            for (int i = currentIndex; i >= 0; i--) {
                t = tokens.get(i);
                if (t.isBlankLine()) { // or whitespaces?
                    continue;
                } else if (t.isPunctuator() && (t.getValue().getValue().equals("}")
                        || t.getValue().getValue().equals("{") || t.getValue().getValue().equals(";"))) {
                    index = currentIndex;
                    break;
                } else {
                    break;
                }
            }
        }

        return index;
    }
}
