package mi.stat.model.entropy.tree;

import mi.stat.model.utils.TreeHelper;

import java.util.List;

public class DecisionsTree {
    Node root;

    public DecisionsTree(Node root) {
        this.root = root;
    }

    public Node getRoot(){
        return root;
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
        Edge edge =  TreeHelper.findEdges(start,value);


        return (edge!=null)?edge.getNode():null;
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
        parentTitle+= (node.getTitle()!=null)?" ->  "+node.getTitle():"";

        if(node.getGoalValue()!=null)
            System.out.println(parentTitle +" G "+node.getGoalValue() );
        else if(node.getEdges().isEmpty())
            System.out.println(parentTitle +" ~ "+node.getGoalValue() );

        for (Edge edge :node.getEdges()) {
            this.print(parentTitle+" ("+edge.getValue()+") ",edge.getNode());
        }
    }


}