package mi.stat.model.entropy.tree;

import java.util.List;

public class DecisionsTree {
    Node root;

    public Node getRoot(){
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public Node traverse(Node node, List<Entity> entities){
        System.out.println(node);
        while(node!=null){

            Node tempNode = null;
            for (Entity entity: entities) {
                tempNode =  findNode(node,entity.getValue());

                if(tempNode!=null){
                    entities.remove(entity);
                    break;
                }
            }
            System.out.println(tempNode);
            if(tempNode==null)return null;

            node = tempNode;

            if(node.getGoalValue()!=null)return node;

        }
        return null;
    }
    public Node findNode(Node start,String value){
        Edge edge =  findEdges(start,value);


        return (edge!=null)?edge.getNode():null;
    }
    public Edge findEdges(Node start,String value){
        return  start.getEdges().stream().filter(edg->value.equals(edg.value)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return "DecisionsTree{" +
                "root=" + root +
                '}';
    }
    public void print(){
        this.print("root",this.root);
    }
    public void print(String parentTitle , Node node){
        parentTitle+= " ->  "+node.getTitle();

        if(node.getEdges().isEmpty())System.out.println(parentTitle );

        for (Edge edge :node.getEdges()) {
            System.out.println(parentTitle +" -> "+edge.getValue() + " " + node.getGoalValue());
            this.print(parentTitle,edge.getNode());
        }
    }


}