package formatter.treehandler;

import parser.tree.Node;
import parser.tree.Tree;

import java.util.ArrayList;

/**
 * Can write each step
 * or generate stream of tokens with metadata to write later
 */
public class TreeTraverser<T> {
    private TreeFileWriter formattedCodeFileWriter;
    private ArrayList<Node<T>> nodesOrdered;
    private Tree<T> tree;

    public TreeTraverser(Tree<T> tree) {
        this.formattedCodeFileWriter = new TreeFileWriter("");
        this.nodesOrdered = new ArrayList<>();
        this.tree = tree;
    }

    /**
     * Making tree traverse and writing into file or into array
     * */
    public void traverse(Node<T> node) {
        ArrayList<Node<T>> curNodeChildren = node.getChildren();

        if (curNodeChildren.isEmpty()) {
            // write into array or into file
            this.write(node);
            return;
        }

        for (int i = 0; i < curNodeChildren.size(); i++) {
            traverse(curNodeChildren.get(i));
        }
    }

    /**
     * Dummy.
     * Write
     * a) into file
     * b) into array to that it can be returned later
     * */
    private void write(Node<T> node){
        // use filewriter class
    }

}
