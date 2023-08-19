package mi.stat.model.utils;

import mi.stat.model.entropy.tree.Edge;
import mi.stat.model.entropy.tree.Node;

public class TreeHelper {
    public static Node makeNodeWithGoalNode(String title, String value,String goalValue){
        Node node = new Node();
        node.setTitle(title);

        Node goalNode = new Node();
        goalNode.setTitle(goalValue);

        Edge edge = new Edge();
        edge.setValue(value);
        edge.setNode(goalNode);

        node.getEdges().add(edge);

        return node;

    }

    public static void connectNode( Node node ,String value ,  Node childNode ){
        Edge edge = new Edge();
        edge.setValue(value);
        edge.setNode(childNode);

        node.getEdges().add(edge);
    }
}
