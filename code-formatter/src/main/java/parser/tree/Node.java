package parser.tree;

import parser.parser.grammar.GrammarUnitType;
import parser.parser.grammar.Nonterminal;
import parser.parser.grammar.Terminal;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    GrammarUnitType type;
    /**
     * Mutually exclusive cases: terminal XOR nonterminal, not both together
     * */
    private Terminal terminalType;
    private Nonterminal nonterminalType;

    public int indentInSpaces; // relative to previous level

    private T data = null;
    private ArrayList<Node<T>> children;
    private T ancestor;

    public Node (T data, GrammarUnitType type, Terminal terminal){
        this.data = data;
        this.children = new ArrayList<>();
        this.ancestor = null;
        this.type = type;

        this.terminalType = terminal;
    }

    public Node (T data, GrammarUnitType type, Nonterminal nonterminal){
        this.data = data;
        this.children = new ArrayList<>();
        this.ancestor = null;
        this.type = type;

        this.nonterminalType = nonterminal;
    }

    public boolean addChild(Node<T> child){
        if (child != null){
            this.children.add(child);
            return true;
        } else {
            return false;
        }
    }

    public T getData() {
        return data;
    }

    public T getAncestor() {
        return ancestor;
    }

    public Terminal getTerminalType() {
        return terminalType;
    }

    public Nonterminal getNonterminalType() {
        return nonterminalType;
    }

    public GrammarUnitType getType() {
        return type;
    }

    public ArrayList<Node<T>> getChildren() {
        return children;
    }
}
