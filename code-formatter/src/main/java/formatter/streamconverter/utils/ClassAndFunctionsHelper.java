package formatter.streamconverter.utils;

import com.yehorpolishchuk.lexercpp.token.Token;
import formatter.streamconverter.StackUnit;

import java.util.ArrayList;

public class ClassAndFunctionsHelper {
    public ArrayList<StackUnit> units;
    public ArrayList<int[]> unitStartEnd;
    public ArrayList<Token> tokens;
    public ArrayList<String[]> tokenAtPosition;

    public ClassAndFunctionsHelper(ArrayList<Token> tokens) {
        this.tokens = tokens;
        this.units = new ArrayList<>();
        this.unitStartEnd = new ArrayList<>();

        this.tokenAtPosition = new ArrayList<>();
        for (int i=0; i<this.tokens.size(); i++){
            this.tokenAtPosition.add(null);
        }
    }

    /**
     * Run just after object was created
     * */
    public void markup() {
        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            if (t.isKeyword() && t.getValue().getValue().equals("class")) {
                // todo
            } else if (t.getValue().getValue().equals("(")) {
                String[] status = functionStartsFromIndex(this.tokens, i);
                if (!status[3].equals("error")){
                    this.tokenAtPosition.set(Integer.parseInt(status[0]), status);
                }
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


    /**
     * @param currentIndex token in currentIndex position must be "("
     * @return (func_name_index, func_decl / func_def or func_call, func_decl_or_def_starts_from, error)
     * <p>
     * Does not recognize template functions and lambdas.
     * <p>
     * Check [3] to define if there was an error
     */
    public static String[] functionStartsFromIndex(ArrayList<Token> tokens, int currentIndex) {
        Token t = tokens.get(currentIndex);
        String[] status = new String[4];
        status[0] = status[1] = status[2] = "";
        status[3] = "error";
        if (!t.getValue().getValue().equals("(")) {
            return status;
        }

        // case 1 guessing it is function definition
        int parenthNestingLevel = 0; // for (
        if (currentIndex == 1) {
            return status;
        }
        boolean maybeCallOrDecl = false,
                maybeDeclaration = false,
                maybeDefinition = false,
                maybeCall = false;
        Token prevt = tokens.get((currentIndex - 1));

        // then it is function declaration, definition or calling
        if (prevt.isIdentifier()) {
            // searching for closing ) for (
            parenthNestingLevel++;
            for (int i = currentIndex + 1; i < tokens.size(); i++) {
                t = tokens.get(i);
                if (parenthNestingLevel == 0) {
                    if (t.isPunctuator() && t.getValue().getValue().equals("{")) {
                        maybeDefinition = true;
                        break;
                    } else if (t.isPunctuator() && t.getValue().getValue().equals(";")) {
                        maybeCallOrDecl = true;
                        break;
                    } else {
                        maybeCall = true;
                        break;
                    }
                } else {
                    if ( t.getValue().getValue().equals("(")) {
                        parenthNestingLevel++;
                    } else if (t.getValue().getValue().equals(")")) {
                        parenthNestingLevel--;
                    }
                }
            }
        } else {
            // maybe template function or lambda or nothing, todo
            return status;
        }

        status[0] = Integer.toString(currentIndex - 1);
        boolean isDefinition = maybeDefinition, isCall = false, isDeclaration = false;
        // now the question is if CALL or DECLARATION
        if (tokens.get(currentIndex - 2).isIdentifier() || tokens.get(currentIndex - 2).isKeyword()) {
            isDeclaration = true;
        }
        if (tokens.get(currentIndex - 2).getValue().getValue().equals("}")
                || tokens.get(currentIndex - 2).getValue().getValue().equals("{")
                || tokens.get(currentIndex - 2).getValue().getValue().equals(";")){
            isCall = true;
        }
        // define if it is call, else consider as declaration and find the start of the statement
        int i = currentIndex - 2;
        for (; i >= 0; i--) {
            t = tokens.get(i);

            if (t.getValue().getValue().equals(":")) {
                if (!isDefinition) {
                    isDeclaration = true;
                }
                break;
            } else if ((t.isOperator() && !t.getValue().getValue().equals("*") && !t.getValue().getValue().equals("&"))
                    || t.getValue().getValue().equals("(") || t.getValue().getValue().equals("[")) {
                if (!isDefinition && !isDeclaration) {
                    isCall = true;
                }
                break;
            } else if (t.getValue().getValue().equals("}") || t.getValue().getValue().equals("{")
                    || t.getValue().getValue().equals(";")) {
                if (!isDeclaration && !isCall) {
                    isDeclaration = true;
                }
                break;
            } else {
                continue;
            }
        }

        // if the file beginning was reached
        if ((i == -1) && !(isDefinition || isDeclaration || isCall)){
            isCall = true;
        }
        status[1] = isDefinition ? "func_def" : isCall ? "func_call" : isDeclaration ? "func_decl" : "";
        status[2] = Integer.toString(i + 1);
        status[3] = (isDeclaration || isDeclaration || isCall)  ? "" : "error";

        return status;
    }
}
