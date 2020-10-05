package parser.parser;

import com.yehorpolishchuk.lexercpp.token.Token;
import parser.parser.grammar.Nonterminal;
import parser.tree.Node;
import parser.tree.NodeType;
import parser.tree.Tree;

import java.util.ArrayList;

public class TreeBuilder<T> {

    private Tree<T> tree;
    private ArrayList<Token> tokensStream;

    public TreeBuilder(ArrayList<Token> tokensStream) {
        this.tokensStream = tokensStream;
        this.tree = new Tree<>();
    }

    public Tree<T> build() {
        this.tree = new Tree<>();
        this.preConfigTree();
        this.parseStream();
        return this.tree;
    }

    private void preConfigTree() {
        Node<T> root = new Node<>(null, NodeType.NONTERMINAL);
        this.tree.setRoot(root);
    }

    private void parseStream() {
        int currentPosition = 0;

        while (true) {


            currentPosition++;
            if (currentPosition >= this.tokensStream.size()) {
                break;
            }
        }
    }
}
