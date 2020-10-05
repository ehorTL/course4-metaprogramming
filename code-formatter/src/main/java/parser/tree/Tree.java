package parser.tree;

public class Tree<T> {
    private Node<T> root = null;

    public Tree() {
        this.root = null;
    }

    public Tree(Node<T> root){
        this.root = root;
    }

    public boolean addChildToNode(Node<T> parentNode, Node<T> childNode){
        boolean successfullyAdded = parentNode.addChild(childNode);

        return successfullyAdded;
    }

    public Node<T> getRoot() {
        return root;
    }
}
