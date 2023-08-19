package mi.stat.model.entropy;


import mi.stat.model.utils.ArrayUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Entropy {

    public enum Lable{
        TITLE,VALUE
    }
    DataTable dataTable;
    final static String _ROOT_ = "_ROOT_";
    private String parentNode;
    //TODO
    static List<String> reservedKeyWords ;

    //TODO group it
    // replace type by Map<String, Map<String,Values> >
    Map<String, Map<String,Values> > sValue;
    Values globalSValue;
    double globalEntropy;
    Map<String,Double> informationGain;
    Map<String,Map<String,Double>> entropy;

    private Entropy() {
        this.sValue = new HashMap<>();
        this.entropy = new HashMap<>();
        this.globalSValue  = new Values();
        this.informationGain = new HashMap<>();
    }

    public Entropy(String parentNode, DataTable dataTable) {
        this();
        this.dataTable = dataTable;
        this.parentNode = parentNode;
    }

    public void calculateInformationGain(){


        for(Map.Entry<String,Map<String,Double>> entry :this.entropy.entrySet()){
            String key = entry.getKey();
            Map<String,Double> map = entry.getValue();

            double total = map.entrySet().stream().mapToDouble((e)->{
                Values value = sValue.get(key).get(e.getKey());
                double ratio = value.getTotal() / globalSValue.getTotal();
                return ratio * e.getValue();
            }).sum();

            this.informationGain.putIfAbsent(key,globalEntropy - total);
        }
        System.out.println(this.parentNode+" information gain");
        System.out.println(new TreeMap<>(this.informationGain));

    }
    public void retry(){
        System.out.println(this.parentNode+" retry information gain");
        LinkedHashMap<String,Double> sortedInformationGain =    this.informationGain.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


        for (Map.Entry<String,Double> mapElement : sortedInformationGain.entrySet()) {
            List<Map<Lable,String>> titleValues = new ArrayList<>();

            Map<Lable,String> map1 = new HashMap<>();
            map1.put(Lable.TITLE,mapElement.getKey());
            map1.put(Lable.VALUE,"sunny");
            System.out.println(mapElement);
        }






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

    }

    public double getEntropy(Values v){
        double sValuePositiveRatio =  v.getPositiveValueRatio();
        double sValueNegativeRatio = v.getNegativeValueRatio();
        double entropy =  - ( sValuePositiveRatio * log2(sValuePositiveRatio))
                - ( sValueNegativeRatio * log2(sValueNegativeRatio) );

        return entropy;
    }
    public void calculateEntropy(){

        this.globalEntropy =  getEntropy(this.globalSValue);

        System.out.println("Global Entropy "+globalEntropy);


        for(Map.Entry<String,Map<String,Values>> entryMap : this.sValue.entrySet()){
            this.entropy.putIfAbsent(entryMap.getKey(),new HashMap<>());

            for(Map.Entry<String,Values> entry : entryMap.getValue().entrySet()) {
                double entropy = getEntropy(entry.getValue());

                this.entropy.get(entryMap.getKey()).put(entry.getKey(), entropy);
            }
        }

        System.out.println(new TreeMap<>(this.entropy));
    }

    public void countResultValues(){
        for(int i =0; i<this.dataTable.rows.length;i++){
            String[] values = this.dataTable.rows[i];

            String rValue =  this.dataTable.result[i];
            if(this.dataTable.getPositiveResultName().equals(rValue)) {
                this.globalSValue.increasePositiveValue();
            }else if(this.dataTable.getNegativeResultName().equals(rValue)) {
                this.globalSValue.increaseNegativeValue();
            }

            for(int  j=0;j<values.length;j++){


                String key =  this.dataTable.rows[i][j];
                String title =  this.dataTable.titles[j];






                this.sValue.compute(title,(k,v)-> {
                            v = v != null ? v : new HashMap<>();

                            v.compute(key, (k1, v1) -> {
                                v1 = v1 != null ? v1 : new Values();

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

    public static double log2(double N)
    {

        // calculate log2 N indirectly
        // using log() method

        return (N==0)?0:(Math.log(N) / Math.log(2));
    }

    public static DataTable getSubTable(DataTable dataTable,List<Map<Lable,String>> titleValues){

        String[] titles = titleValues.stream().map(map->map.get(Lable.TITLE)).toArray(String[]::new);
        String[] attrValues = titleValues.stream().map(map->map.get(Lable.VALUE)).toArray(String[]::new);

        List<String[]> subRows = new ArrayList<>(dataTable.rows.length/2);
        List<String> result = new ArrayList<>(dataTable.rows.length/2);
        String[] newTitle = ArrayUtils.minus(dataTable.titles,titles);


        for(int i=0;i<dataTable.rows.length;i++){

            String[] rows = dataTable.rows[i];

            if(!ArrayUtils.containsAll(rows,attrValues))continue;

            String[] subSet = ArrayUtils.minus(rows,attrValues);

            subRows.add(subSet);
            result.add(dataTable.result[i]);
        }

        DataTable subDataTable = new DataTable(subRows.size(),
                                        dataTable.titles.length - attrValues.length,
                                                dataTable.positiveResultName,
                                                dataTable.negativeResultName);

        subDataTable.addTitleValue(newTitle);

        for(int i=0;i<subRows.size();i++){
            subDataTable.addValue(result.get(i),i,subRows.get(i));
        }

        return subDataTable;

    }


    public void calculate(String[] rows){

    }

    public static void main(String[] args) {
        DataTable dt = new DataTable(14,5,"yes","no");


        dt.addTitleValue("outlook","temp","humidity","windy","play");

        dt.addValue("no",0,"sunny","hot" ,"high","False");
        dt.addValue("no",1,"sunny","hot" ,"high","True");
        dt.addValue("yes",2,"overcast","hot" ,"high","False");
        dt.addValue("yes",3,"rainy","mild" ,"high","False");
        dt.addValue("yes",4,"rainy","cool" ,"normal","False");
        dt.addValue("no",5,"rainy","cool" ,"normal","True");
        dt.addValue("yes",6,"overcast","cool" ,"normal","True");
        dt.addValue("no",7,"sunny","mild" ,"high","False" );
        dt.addValue("yes" ,8,"sunny","cool" ,"normal","False");
        dt.addValue("yes",9,"rainy","mild" ,"normal","False" );
        dt.addValue("yes" ,10,"sunny","mild" ,"normal","True");
        dt.addValue("yes" ,11,"overcast","mild" ,"high","True");
        dt.addValue("yes",12,"overcast","hot" ,"normal","False");
        dt.addValue( "no",13,"rainy","mild" ,"high","True" );


       // System.out.print(ArrayUtils.containsAll(dt.rows[1],"sunny","hot"));


        Entropy entropy =  new Entropy(_ROOT_,dt);
        entropy.countResultValues();
        entropy.calculateEntropy();
        entropy.calculateInformationGain();



    }
}
class Values{
    int positiveValue;
    int negativeValue;

    public int getPositiveValue() {
        return positiveValue;
    }

    public void setPositiveValue(int positiveValue) {
        this.positiveValue = positiveValue;
    }

    public int getNegativeValue() {
        return negativeValue;
    }
    public double getPositiveValueRatio(){
        return ( (double) this.positiveValue/(double) ( this.negativeValue + this.positiveValue )  );
    }
    public double getNegativeValueRatio(){
        return ( (double) this.negativeValue/(double) ( this.negativeValue + this.positiveValue )  );
    }

    public double getTotal(){
        return  this.negativeValue  + this.positiveValue ;
    }

    public void setNegativeValue(int negativeValue) {
        this.negativeValue = negativeValue;
    }
    public void increasePositiveValue(){
        this.positiveValue++;
    }

    public void increaseNegativeValue(){
        this.negativeValue++;
    }

    @Override
    public String toString() {
        return "["+ positiveValue +", " + negativeValue + "]";
    }
}

class DataTable{
    String[] titles;
    String[][] rows;
    String[] result;

    String positiveResultName;
    String negativeResultName;

    public DataTable(int size,int attributeSize,String positiveResult,String negativeResult) {
        this.titles = new String[attributeSize];
        this.rows = new String[size][attributeSize];
        this.result = new String[size];

        this.positiveResultName = positiveResult;
        this.negativeResultName = negativeResult;
    }

    public String getPositiveResultName() {
        return positiveResultName;
    }

    public void setPositiveResultName(String positiveResultName) {
        this.positiveResultName = positiveResultName;
    }

    public String getNegativeResultName() {
        return negativeResultName;
    }

    public void setNegativeResultName(String negativeResultName) {
        this.negativeResultName = negativeResultName;
    }

    void addValue(String resultValue, int i, String... values){
        rows[i] = values;
        result[i] = resultValue;
    }
    void addTitleValue(String... titles){
        if(titles.length!=this.titles.length) throw new RuntimeException("Title length not equals");
        this.titles = titles;
    }


    public String getKey(int i,int j){
        return this.titles[j]+"_"+this.rows[i][j];
    }
    public String getResult(int i){
        return this.result[i];
    }

    public void print(){
        for(int i=0;i<this.rows.length;i++){
            String[] row = this.rows[i];

            for(String v : row) {
                System.out.print(v+" ");
            }
            System.out.print(result[i]);
            System.out.println();
        }
    }
}

