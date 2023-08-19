package mi.stat.model.probabilistic;

import mi.stat.model.entropy.tree.DecisionsTree;
import mi.stat.model.entropy.tree.Edge;
import mi.stat.model.entropy.tree.Node;

public class DecisionTreeTestData {

    public static DecisionsTree decisionTreeTestData(){

        DecisionsTree dt =  new DecisionsTree();

        Node yes = new Node();
        yes.setGoalValue("yes");

        Node no = new Node();
        no.setGoalValue("no");

        Node outlook = new Node();

        dt.setRoot(outlook);

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

        return dt;
    }
}
