package mi.stat.probabilistic.model;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Entropy {

    DataTable dataTable;

    //TODO group it
    // replace type by Map<String, Map<String,Values> >
    Map<String, Map<String,Values> > sValue;
    Values globalSValue;
    double globalEntropy;
    Map<String,Map<String,Double>> entropy;

    public Entropy() {
        this.sValue = new HashMap<>();
        this.entropy = new HashMap<>();
        this.globalSValue  = new Values();
    }

    public Entropy( DataTable dataTable) {
        this();
     this.dataTable = dataTable;
    }

    public double getEntropy(Values v){
        double sValuePositiveRatio =  v.getPositiveValueRatio();
        double sValueNegativeRatio = v.getNegativeValueRatio();
        double entropy =  - ( sValuePositiveRatio * log2(sValuePositiveRatio))
                - ( sValueNegativeRatio * log2(sValueNegativeRatio) );

        return entropy;
    }
    public void calculateEntropy(){

        double globalEntropy =  getEntropy(this.globalSValue);

        System.out.println("GlobalEntropy "+globalEntropy);


        for(Map.Entry<String,Map<String,Values>> entryMap : this.sValue.entrySet()){
            this.entropy.putIfAbsent(entryMap.getKey(),new HashMap<>());

            for(Map.Entry<String,Values> entry : entryMap.getValue().entrySet()) {
                double entropy = getEntropy(entry.getValue());

                this.entropy.get(entryMap.getKey()).put(entry.getKey(), entropy);
            }
        }

        System.out.println(new TreeMap<>(this.entropy));
    }

    public void init(){
        for(int i =0; i<this.dataTable.rows.length;i++){
            String[] values = this.dataTable.rows[i];
            for(int  j=0;j<values.length;j++){

                String rValue =  this.dataTable.result[i];
                String key =  this.dataTable.rows[i][j];
                String title =  this.dataTable.titles[j];

                if(this.dataTable.getPositiveResultName().equals(rValue)) {
                    this.globalSValue.increasePositiveValue();
                }else if(this.dataTable.getNegativeResultName().equals(rValue)) {
                    this.globalSValue.increaseNegativeValue();
                }




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



        Entropy entropy =  new Entropy(dt);
        entropy.init();
        entropy.calculateEntropy();
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
}