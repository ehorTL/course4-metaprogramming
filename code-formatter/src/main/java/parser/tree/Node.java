package parser.tree;

import parser.parser.grammar.Nonterminal;
import parser.parser.grammar.Terminal;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    NodeType type;
    /**
     * Mutually exclusive cases: terminal XOR nonterminal, not both together
     * */
    private Terminal terminalType;
    private Nonterminal nonterminalType;

    private T data = null;
    private ArrayList<Node<T>> children;
    private T ancestor;

    public Node (T data, NodeType type){
        this.data = data;
        this.children = new ArrayList<>();
        this.ancestor = null;
        this.type = type;
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
}
