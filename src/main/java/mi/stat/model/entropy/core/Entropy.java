package mi.stat.model.entropy.core;


import mi.stat.model.entropy.tree.DecisionsTree;
import mi.stat.model.entropy.tree.Node;
import mi.stat.model.utils.EntropyUtils;
import mi.stat.model.utils.TreeHelper;

import java.util.*;
import java.util.stream.Collectors;

public class Entropy {

    public enum Lable {
        TITLE, VALUE
    }

    DataTable dataTable;
    final static String _ROOT_ = "_ROOT_";
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
        System.out.println(this.parentNode + " information gain");
        System.out.println(new TreeMap<>(this.informationGain));

    }

    public Node buildTree() {
        System.out.println(this.parentNode + " buildTree");
        LinkedHashMap<String, Double> sortedInformationGain = this.informationGain.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        Node localRoot = null;

        for (Map.Entry<String, Double> mapElement : sortedInformationGain.entrySet()) {
            String nextParentTitle = mapElement.getKey();

            Map<String, Result> sValuesMap = this.sValue.get(nextParentTitle);


            for (Map.Entry<String, Result> entry : sValuesMap.entrySet()) {
                String value = entry.getKey();
                Result result = entry.getValue();

                if(result.doesItGiveOnlyOneResult() ){
                    String resultName = result.getResultName(this.dataTable.getPositiveResultName(),this.dataTable.getNegativeResultName()) ;
                    Node node  = TreeHelper.makeNodeWithGoalNode(nextParentTitle, entry.getKey(), resultName);

                    if(localRoot==null){
                        localRoot = node;
                        continue;
                    }
                    TreeHelper.connectNode(localRoot,value,node);

                    continue;
                }



                List<Map<Entropy.Lable,String>> titleValues = new ArrayList<>();
                Map<Entropy.Lable,String> map = new HashMap<>();

                map.put(Lable.TITLE,nextParentTitle);
                map.put(Lable.VALUE,value);

                titleValues.add(map);

                DataTable dataTable =  EntropyUtils.getSubTable(this.dataTable,titleValues);
                System.out.println("**************************************************");
                System.out.println("TITLE "+nextParentTitle+" value " +value);


                dataTable.print();

                Entropy entropy = new Entropy(nextParentTitle,dataTable);

                entropy.init();

                Node subChildRootNode =  entropy.buildTree();

                if(localRoot==null) {
                    localRoot = subChildRootNode;
                    continue;
                }

                TreeHelper.connectNode(localRoot,value,subChildRootNode);

            }


        }

        System.out.println(localRoot);
        System.out.println();
//        titleValues.add(map1);
//
//        DataTable subDt =  entropy.getSubTable(entropy.dataTable,titleValues);
//        subDt.print();
//
//        Entropy entropy1 =  new Entropy("outlook",subDt);
//
//        entropy1.countResultValues();
//        entropy1.calculateEntropy();
//        entropy1.calculateInformationGain();


        return localRoot;

    }

    public double getEntropy(Result v) {
        double sValuePositiveRatio = v.getPositiveValueRatio();
        double sValueNegativeRatio = v.getNegativeValueRatio();
        double entropy = -(sValuePositiveRatio * log2(sValuePositiveRatio))
                - (sValueNegativeRatio * log2(sValueNegativeRatio));

        return entropy;
    }

    public void calculateEntropy() {

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
    }

    public void countResultValues() {
        for (int i = 0; i < this.dataTable.rows.length; i++) {
            String[] values = this.dataTable.rows[i];

            String rValue = this.dataTable.result[i];
            if (this.dataTable.getPositiveResultName().equals(rValue)) {
                this.globalSValue.increasePositiveValue();
            } else if (this.dataTable.getNegativeResultName().equals(rValue)) {
                this.globalSValue.increaseNegativeValue();
            }

            for (int j = 0; j < values.length; j++) {


                String key = this.dataTable.rows[i][j];
                String title = this.dataTable.titles[j];


                this.sValue.compute(title, (k, v) -> {
                            v = v != null ? v : new HashMap<>();

                            v.compute(key, (k1, v1) -> {
                                v1 = v1 != null ? v1 : new Result();

                                if (this.dataTable.getPositiveResultName().equals(rValue)) {
                                    v1.increasePositiveValue();
                                } else if (this.dataTable.getNegativeResultName().equals(rValue)) {
                                    v1.increaseNegativeValue();
                                }

                                return v1;
                            });
                            return v;
                        }
                );
            }
        }

        System.out.println(new TreeMap<>(this.sValue));

    }

    public static double log2(double N) {
        // calculate log2 N indirectly
        // using log() method
        return (N == 0) ? 0 : (Math.log(N) / Math.log(2));
    }

    public void init(){
        this.countResultValues();
        this.calculateEntropy();
        this.calculateInformationGain();
    }

    public void calculate(String[] rows) {

    }

    public static void main(String[] args) {
        DataTable dt = new DataTable(14, 4, "yes", "no");


        dt.addTitleValue("outlook", "temp", "humidity", "windy");

        dt.addValue("no", 0, "sunny", "hot", "high", "False");
        dt.addValue("no", 1, "sunny", "hot", "high", "True");
        dt.addValue("yes", 2, "overcast", "hot", "high", "False");
        dt.addValue("yes", 3, "rainy", "mild", "high", "False");
        dt.addValue("yes", 4, "rainy", "cool", "normal", "False");
        dt.addValue("no", 5, "rainy", "cool", "normal", "True");
        dt.addValue("yes", 6, "overcast", "cool", "normal", "True");
        dt.addValue("no", 7, "sunny", "mild", "high", "False");
        dt.addValue("yes", 8, "sunny", "cool", "normal", "False");
        dt.addValue("yes", 9, "rainy", "mild", "normal", "False");
        dt.addValue("yes", 10, "sunny", "mild", "normal", "True");
        dt.addValue("yes", 11, "overcast", "mild", "high", "True");
        dt.addValue("yes", 12, "overcast", "hot", "normal", "False");
        dt.addValue("no", 13, "rainy", "mild", "high", "True");


        // System.out.print(ArrayUtils.containsAll(dt.rows[1],"sunny","hot"));


        Entropy entropy = new Entropy(_ROOT_, dt);
        entropy.init();
       Node root =  entropy.buildTree();
        DecisionsTree decisionsTree = new DecisionsTree();
        decisionsTree.setRoot(root);
        System.out.println();
        System.out.println("FINAL TREE");
        decisionsTree.print();

    }
}
