package mi.stat.model.utils;

import mi.stat.model.entropy.tree.Edge;
import mi.stat.model.entropy.tree.Node;

public class TreeHelper {
    public static Node makeGoalNode(String title, String goalValue){
        Node goalNode = new Node();
        goalNode.setGoalValue(goalValue);

        return goalNode;
    }
    public static Node makeNode(String title){
        Node node = new Node();
        node.setTitle(title);
        return node;
    }

    public static Node addEdge( Node node ,String value ){
        Edge edge = new Edge();
        edge.setValue(value);
        node.getEdges().add(edge);
        return node;
    }

    public static Node connectNodeWithEdgeByValue(Node node , String value , Node childNode ){
        Edge edge = findEdges(node,value);

        if(edge.getNode()!=null){
            throw new RuntimeException("Edge node not empty");
        }
        edge.setNode(childNode);

        return node;
    }

    public static Edge findEdges(Node node,String value){
        return  node.getEdges().stream().filter(edg->value.equals(edg.getValue())).findFirst().orElse(null);
    }
}
