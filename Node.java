import java.util.ArrayList;
import java.util.List;

public class Node {
    String label;
    List<Node> children = new ArrayList<>();

    public Node(String label) {
        this.label = label;
    }

    public Node(Token token) {
        this.label = token.getType() + "('" + token.getValue() + "')";
    }

    public void addChild(Node child) {
        children.add(child);
    }

    @Override
    public String toString() {
        return toString(0);
    }

    private String toString(int indent) {
        StringBuilder sb = new StringBuilder("  ".repeat(indent) + label + "\n");
        for (Node child : children) {
            sb.append(child.toString(indent + 1));
        }
        return sb.toString();
    }
}
