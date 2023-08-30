package mi.stat.model.probabilistic;

import mi.stat.model.entropy.core.Entropy;
import mi.stat.model.entropy.tree.DecisionsTree;
import mi.stat.model.entropy.tree.Entity;
import mi.stat.model.entropy.tree.Node;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static mi.stat.model.entropy.constant.CommonConstant._ROOT_;

public class EntropyTest {

    @Test
    public void testAll() {
        Entropy entropy = new Entropy(_ROOT_, DataTableTestData.getTennisData());
        Node node = entropy.initAndBuildTree();
        DecisionsTree decisionsTree = new DecisionsTree(node);
        decisionsTree.print();
        List<Entity> entities = new ArrayList<>();

        entities.add(new Entity("outlook","sunny"));
        entities.add(new Entity("humid","normal"));

        Node d = decisionsTree.traverse(decisionsTree.getRoot(),entities);

        assertNotNull(d);
        assertEquals("yes",d.getGoalValue());
    }
}
