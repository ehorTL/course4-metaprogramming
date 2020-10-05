package parser.tree;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    private T data = null;
    private ArrayList<Node<T>> children;
    private T ancestor;

    public Node (T data){
        this.data = data;
        this.children = new ArrayList<>();
        this.ancestor = null;
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
