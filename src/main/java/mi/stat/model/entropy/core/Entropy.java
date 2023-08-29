package mi.stat.model.entropy.core;


import mi.stat.model.entropy.tree.DecisionsTree;
import mi.stat.model.entropy.tree.Node;
import mi.stat.model.utils.EntropyUtils;
import mi.stat.model.utils.TimeMeasure;
import mi.stat.model.utils.TreeHelper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static mi.stat.model.entropy.constant.CommonConstant._ROOT_;

public class Entropy {

    public enum Lable {
        TITLE, VALUE
    }

    DataTable dataTable;

    private String parentNode;
    //TODO
    static List<String> reservedKeyWords;

    //TODO group it
    // replace type by Map<String, Map<String,Values> >
    Map<String, Map<String, Result>> sValue;
    Result globalSValue;
    double globalEntropy;
    Map<String, Double> informationGain;
    Map<String, Map<String, Double>> entropy;

    private Entropy() {
        this.sValue = new HashMap<>();
        this.entropy = new HashMap<>();
        this.globalSValue = new Result();
        this.informationGain = new HashMap<>();
    }

    public Entropy(String parentNode, DataTable dataTable) {
        this();
        this.dataTable = dataTable;
        this.parentNode = parentNode;
    }

    public void calculateInformationGain() {
        TimeMeasure.start(parentNode + " calculateInformationGain");

        for (Map.Entry<String, Map<String, Double>> entry : this.entropy.entrySet()) {
            String key = entry.getKey();
            Map<String, Double> map = entry.getValue();

            double total = map.entrySet().stream().mapToDouble((e) -> {
                Result value = sValue.get(key).get(e.getKey());
                double ratio = value.getTotal() / globalSValue.getTotal();
                return ratio * e.getValue();
            }).sum();

            this.informationGain.putIfAbsent(key, globalEntropy - total);
        }

        TimeMeasure.end(parentNode + " calculateInformationGain");

        System.out.println(this.parentNode + " information gain");
        System.out.println(new TreeMap<>(this.informationGain));


    }

    void print(Node localRoot) {
        System.out.println("**************************************************");
        System.out.println(localRoot);
        System.out.println();
        DecisionsTree decisionsTree = new DecisionsTree(localRoot);
        System.out.println();
        decisionsTree.print();
        System.out.println("**************************************************");
    }

    private boolean ifResultHasOnlyOneGoalThenCreateAndConnectNode(Result result, Node root, String value) {
        if (!result.doesItGiveOnlyOneResult()) return false;

        String resultName = this.getResultName(result);
        TreeHelper.createGoalNodeAndConnect(root, resultName, value);

        return true;
    }



    public Node buildTree() {
        System.out.println(this.parentNode + " buildTree");
        Map.Entry<String, Double> mapElement = EntropyUtils.getMaxNode(this.informationGain);

        if (mapElement == null) {
            return null;
        }

        Node root = TreeHelper.makeNode(mapElement.getKey());
        Map<String, Result> rootAttributeValue = this.sValue.get(root.getTitle());
        System.out.println("NODE " + root);
        for (Map.Entry<String, Result> entry : rootAttributeValue.entrySet()) {
            String attributeValue = entry.getKey();
            Result result = entry.getValue();

            boolean isNodeCreated = ifResultHasOnlyOneGoalThenCreateAndConnectNode(result, root, attributeValue);
            if (isNodeCreated) continue;

            /**
             * Break down tree to find next attribute in-search of Goal
             * */

            Node childRootNode = this.buildChildTree(this.dataTable, root, attributeValue);
            connectSubChildRootNode(root, attributeValue, childRootNode);

            // print(root);
        }

        return root;

    }

    private Node connectSubChildRootNode(Node root, String attributeValue, Node subChildRootNode) {
        if (subChildRootNode != null)
            return TreeHelper.createEdgeAndConnectNodeByValue(root, attributeValue, subChildRootNode);
        return null;
    }

    private Entropy getNewEntropy(DataTable subDataTable, Node parent, String attributeValue) {

        System.out.println();
        System.out.println("TITLE " + parent.getTitle() + " value " + attributeValue);


        if (subDataTable == null) return null;
        // subDataTable.print(10);
        return new Entropy(parent.getTitle(), subDataTable);
    }

    private Node buildChildTree(DataTable parentDataTable, Node parent, String attributeValue) {
        DataTable subDataTable = EntropyUtils.getSubTable(parentDataTable, parent.getTitle(), attributeValue);
        Entropy entropy = this.getNewEntropy(subDataTable, parent, attributeValue);
        if (entropy == null) return null;
        return entropy.initAndBuildTree();
    }

    public double getEntropy(Result v) {
        double sValuePositiveRatio = v.getPositiveValueRatio();
        double sValueNegativeRatio = v.getNegativeValueRatio();
        double entropy = -(sValuePositiveRatio * log2(sValuePositiveRatio))
                - (sValueNegativeRatio * log2(sValueNegativeRatio));

        return entropy;
    }

    public void calculateEntropy() {
        TimeMeasure.start(parentNode + " calculateEntropy");
        this.globalEntropy = getEntropy(this.globalSValue);

        System.out.println("Global Entropy " + globalEntropy);


        for (Map.Entry<String, Map<String, Result>> entryMap : this.sValue.entrySet()) {
            this.entropy.putIfAbsent(entryMap.getKey(), new HashMap<>());

            for (Map.Entry<String, Result> entry : entryMap.getValue().entrySet()) {
                double entropy = getEntropy(entry.getValue());

                this.entropy.get(entryMap.getKey()).put(entry.getKey(), entropy);
            }
        }

        System.out.println(new TreeMap<>(this.entropy));

        TimeMeasure.end(parentNode + " calculateEntropy");

    }

    public void countResultValues() {
        TimeMeasure.start(parentNode + " countResultValues");

        for (int i = 0; i < this.dataTable.rows.length; i++) {
            String[] values = this.dataTable.rows[i];
            String rValue = this.dataTable.result[i];

            this.globalSValue.increaseValue(this.dataTable.getPositiveResultName(),
                                            this.dataTable.getNegativeResultName(),
                                            rValue);

            for (int j = 0; j < values.length; j++) {


                final String key = this.dataTable.rows[i][j];
                final String title = this.dataTable.titles[j];
                //TODO Compute concurrent exception
                this.compute(title, rValue, key);

            }
        }

        TimeMeasure.end(parentNode + " countResultValues");
        System.out.println(new TreeMap<>(this.sValue));
    }

    public void compute(final String title, final String rValue, final String key) {

            this.sValue.compute(title, (k, v) -> {
                        v = v != null ? v : new ConcurrentHashMap<>();

                        v.compute(key, (k1, v1) -> {
                            v1 = v1 != null ? v1 : new Result();
                            v1.increaseValue(this.dataTable.getPositiveResultName(),
                                    this.dataTable.getNegativeResultName(),
                                    rValue);

                            return v1;
                        });
                        return v;
                    }
            );

    }

    public static double log2(double N) {
        // calculate log2 N indirectly
        // using log() method
        return (N == 0) ? 0 : (Math.log(N) / Math.log(2));
    }

    public void init() {
        this.countResultValues();
        this.calculateEntropy();
        this.calculateInformationGain();
    }
    public Node initAndBuildTree() {
        this.init();
        return this.buildTree();
    }

    public static void main(String[] args) {


        //   DataTable dt =  dataSetPayTennisWhile(10000000);
        DataTable dt = dataSetPayTennis(2000000); // 20000000  2000000 200000
        //  dt.print();

        // System.out.print(ArrayUtils.containsAll(dt.rows[1],"sunny","hot"));

        TimeMeasure.start("MAIN");

        Entropy entropy = new Entropy(_ROOT_, dt);
        entropy.init();
        Node root = entropy.buildTree();
        DecisionsTree decisionsTree = new DecisionsTree(root);

        System.out.println();
        System.out.println("FINAL TREE");
        decisionsTree.print();

        TimeMeasure.end("MAIN");

        System.out.println();
        TimeMeasure.print();
    }

    public String getResultName(Result result) {
        return switch (result.getResultType()) {
            case 1 -> this.dataTable.positiveResultName;
            case -1 -> this.dataTable.negativeResultName;
            default -> throw new RuntimeException();
        };
    }


    private static DataTable dataSetPayTennis(int n) {
        int _DEFAULT_SIZE = 14;
        DataTable dt = new DataTable(_DEFAULT_SIZE * n, 4, "yes", "no");

        for (int i = 0; i < _DEFAULT_SIZE * n; ) {
            dt.addTitleValue("outlook", "temp", "humidity", "windy");

            dt.addValue("no", i++, "sunny", "hot", "high", "False");
            dt.addValue("no", i++, "sunny", "hot", "high", "True");
            dt.addValue("yes", i++, "overcast", "hot", "high", "False");
            dt.addValue("yes", i++, "rainy", "mild", "high", "False");
            dt.addValue("yes", i++, "rainy", "cool", "normal", "False");
            dt.addValue("no", i++, "rainy", "cool", "normal", "True");
            dt.addValue("yes", i++, "overcast", "cool", "normal", "True");
            dt.addValue("no", i++, "sunny", "mild", "high", "False");
            dt.addValue("yes", i++, "sunny", "cool", "normal", "False");
            dt.addValue("yes", i++, "rainy", "mild", "normal", "False");
            dt.addValue("yes", i++, "sunny", "mild", "normal", "True");
            dt.addValue("yes", i++, "overcast", "mild", "high", "True");
            dt.addValue("yes", i++, "overcast", "hot", "normal", "False");
            dt.addValue("no", i++, "rainy", "mild", "high", "True");
        }
        return dt;

    }

    private static DataTable dataSetPayTennisWhile(int n) {
        DataTable dt = new DataTable(n, 3, "yes", "no");


        dt.addTitleValue("outlook", "temp", "humidity");
        for (int i = 0; i < n; i++) {
            dt.addValue(getRandomR(), i, getRandomStr(0), getRandomStr(1), getRandomStr(2));
        }
        return dt;

    }

    static String[][] data = new String[3][3];
    static String[] dataR = new String[]{"no", "yes"};

    static {
        data[0][0] = "sunny";
        data[0][1] = "overcast";
        data[0][2] = "rainy";

        data[1][0] = "hot";
        data[1][1] = "cool";
        data[1][2] = "mild";

        data[2][0] = "high";
        data[2][1] = "normal";
        data[2][2] = "mid";

    }

    private static String getRandomStr(int i) {
        int index = ThreadLocalRandom.current().nextInt(0, 3);
        return data[i][index];
    }

    private static String getRandomR() {
        int index = ThreadLocalRandom.current().nextInt(0, 2);
        return dataR[index];
    }


}
