package formatter;

import com.yehorpolishchuk.lexercpp.lexer.Lexer;
import com.yehorpolishchuk.lexercpp.token.Token;
import parser.parser.TreeBuilder;
import parser.tree.Node;
import parser.tree.Tree;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Implementation of C++ code formatter.
 * Entry point for the whole application.
 * */
public class Formatter {
    /**
     *
     * @param fileFullName The full path to C++ file (.cpp) OR RELATIVE??!!?!?
     * */
    public void format(String fileFullName) throws FileNotFoundException {
        Lexer lexer = new Lexer (fileFullName);
        lexer.parse();

        ArrayList<Token> tokens = lexer.getTokens();
        TreeBuilder<Node<Token>> treeBuilder = new TreeBuilder<>(tokens);
        Tree<Node<Token>> tree = treeBuilder.build(); // getting 'AST' tree


    }

    public static void main(String[] args) {

    }
}
