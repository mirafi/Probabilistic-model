package mi.stat.model.probabilistic;


import mi.stat.model.entropy.tree.DecisionsTree;
import mi.stat.model.entropy.tree.Entity;
import mi.stat.model.entropy.tree.Node;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.*;
//TODO separate entity test data
// rename test method

public class DecisionTreeTest {

    DecisionsTree decisionTreeTestData = DecisionTreeTestData.decisionTreeTestData();

    @Test
    public void A(){
        List<Entity> entities = new ArrayList<>();

        entities.add(new Entity("humid","normal"));
        entities.add(new Entity("outlook","sunny"));

        Node d = decisionTreeTestData.traverse(decisionTreeTestData.getRoot(),entities);
        assertNotNull(d);
        assertEquals("no",d.getGoalValue());
    }

    @Test
    public void B(){
        List<Entity> entities = new ArrayList<>();

        //   entities.add(new Entity("outlook","overcast"));

        entities.add(new Entity("outlook","sunny"));
        entities.add(new Entity("humid","high"));

        Node d = decisionTreeTestData.traverse(decisionTreeTestData.getRoot(),entities);
        assertNotNull(d);
        assertEquals("yes",d.getGoalValue());
    }

    @Test
    public void C(){
        List<Entity> entities = new ArrayList<>();

        entities.add(new Entity("outlook","overcast"));


        Node d = decisionTreeTestData.traverse(decisionTreeTestData.getRoot(),entities);
        assertNotNull(d);
        assertEquals("yes",d.getGoalValue());
    }

    @Test
    public void D(){
        List<Entity> entities = new ArrayList<>();

        entities.add(new Entity("outlook","rainy"));
        entities.add(new Entity("humid","high"));


        Node d = decisionTreeTestData.traverse(decisionTreeTestData.getRoot(),entities);
        assertNull(d);
    }

}
