package parser.parser;

import com.yehorpolishchuk.lexercpp.token.Token;
import parser.parser.grammar.*;
import parser.tree.Node;
import parser.tree.Tree;

import java.util.ArrayList;

public class TreeBuilder<T> {

    private Tree<T> tree;
    private ArrayList<Token> tokensStream;
    private static final Nonterminal rootNodeType = Nonterminal.APP;

    private Node<T> curNode;
    private int curPointerToToken; /// lookahead

    public TreeBuilder(ArrayList<Token> tokensStream) {
        this.tokensStream = tokensStream;
        this.tree = new Tree<>();
        this.curNode = null;
        this.curPointerToToken = 0; // lookahead
    }

    public Tree<T> build() {
        this.tree = new Tree<>();
        this.preConfigTree();
        this.parseStream();
        return this.tree;
    }

    private void preConfigTree() {
        Node<T> root = new Node<>(null, GrammarUnitType.NONTERMINAL, TreeBuilder.rootNodeType);
        this.tree.setRoot(root);
        this.curNode = root;
    }

    private void parseStream() {
//        int currentPosition = 0;

        while (true) {

            Token curToken = this.tokensStream.get(this.curPointerToToken);

            GrammarUnitType curNodeType = this.curNode.getType();
            if (curNodeType == GrammarUnitType.NONTERMINAL) {

            } else if (curNodeType == GrammarUnitType.TERMINAL) {

            }

//            currentPosition++;
//            if (currentPosition >= this.tokensStream.size()) {
//                break;
//            }
        }
    }


    private void match(GrammarUnit lookahead) {

    }

}
