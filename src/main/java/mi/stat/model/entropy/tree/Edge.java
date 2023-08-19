package mi.stat.model.entropy.tree;

public class Edge{
    String value;
    Node node;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "value='" + value + '\'' +
                ", node=" + node +
                '}';
    }
}