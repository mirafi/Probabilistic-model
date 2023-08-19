package mi.stat.model.entropy.tree;

import java.util.ArrayList;
import java.util.List;

public class Node{
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
