package mi.stat.model.entropy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DecisionsTree {
    Node root;

    public Node getRoot(){
        return root;
    }
    public Node traverse(Node node,List<Entity> entities){
        System.out.println(node);
        while(node!=null){

            Node tempNode = null;
            for (Entity entity: entities) {
                tempNode =  findNode(node,entity.getTitle(),entity.getValue());

                if(tempNode!=null){
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
    public Node findNode(Node start,String title,String value){
        List<Edge> edges =  findEdges(start,value);
        Edge edge =  edges.stream().filter(edg->{
                                                Node node = edg.getNode();
                                                String t = (node!=null)?node.getTitle():null;
                                                String goalValue = (node!=null)?node.getGoalValue():null;
                                                return title.equals(t) || goalValue!=null;
                                            }).findFirst().orElse(null);

        return (edge!=null)?edge.getNode():null;
    }
    public List<Edge> findEdges(Node start,String value){
        return  start.getEdges().stream().filter(edg->value.equals(edg.value)).collect(Collectors.toList());

    }

    @Override
    public String toString() {
        return "DecisionsTree{" +
                "root=" + root +
                '}';
    }
    public static void start(DecisionsTree dt) {
        List<Entity> entities = new ArrayList<>();

     //   entities.add(new Entity("outlook","overcast"));

        entities.add(new Entity("outlook","sunny"));
        entities.add(new Entity("humid","high"));
//
//        entities.add(new Entity("outlook","sunny"));
//        entities.add(new Entity("humid","normal"));

//        entities.add(new Entity("",""));
//        entities.add(new Entity("",""));
//        entities.add(new Entity("",""));
        Node d = dt.traverse(dt.root,entities);
        System.out.println(d);
    }
    public static void main(String[] args) {
        DecisionsTree dt =  new DecisionsTree();

        Node yes = new Node();
        yes.setGoalValue("yes");

        Node no = new Node();
        no.setGoalValue("no");

        Node outlook = new Node();
        dt.root = outlook;

        outlook.setTitle("outlook");

        Edge sunny = new Edge();
        sunny.setValue("sunny");

        Edge overcast = new Edge();
        overcast.setValue("overcast");

        overcast.setNode(yes);

        Edge rain = new Edge();
        rain.setValue("rain");


        outlook.getEdges().add(sunny);
        outlook.getEdges().add(overcast);
        outlook.getEdges().add(rain);



        Node humid = new Node();
        humid.setTitle("humid");

        sunny.setNode(humid);

        Edge high = new Edge();
        high.setValue("high");
        high.setNode(yes);

        Edge normal = new Edge();
        normal.setValue("normal");
        normal.setNode(no);


        humid.getEdges().add(high);
        humid.getEdges().add(normal);

        start(dt);



    }
}
class Node{
    String title;
    List<Edge> edges = new ArrayList<>();
    String goalValue;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public String getGoalValue() {
        return goalValue;
    }

    public void setGoalValue(String goalValue) {
        this.goalValue = goalValue;
    }

    @Override
    public String toString() {
        return "Node{" +
                "title='" + title + '\'' +
                ", edges=" + edges +
                ", goalValue='" + goalValue + '\'' +
                '}';
    }
}
class Edge{
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
class Entity{
    String title;
    String value;

    public Entity(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(title, entity.title) && Objects.equals(value, entity.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, value);
    }

    @Override
    public String toString() {
        return "Entity{" +
                "title='" + title + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}