package mi.stat.model.probabilistic;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MarkovChain {
    Random r = new Random();
    List<String> dataSet = new ArrayList<>();
    Map<String,Map<String,Integer>> countingMatrix = new HashMap<>();
    Map<String,Map<String,Double>>  probabilityMatrix = new HashMap<>();
    Map<String,Integer> totalCountingMap = new HashMap<>();

    Logger logger = Logger.getLogger(String.valueOf(MarkovChain.class));
    public MarkovChain addData(String data){
        this.dataSet.add(data);
        return this;
    }
    public static MarkovChain initiate(List<String> dataSet){
        MarkovChain mc = new MarkovChain();
        for (String data: dataSet) {
            mc.addData(data);
        }
        mc.calculate();
        return mc;
    }
    public void reset(){
        this.dataSet.clear();
        this.countingMatrix.clear();
        this.probabilityMatrix.clear();
    }

    private void initiateTotalCounting(){
        for (String data: this.dataSet) {
            this.totalCountingMap.put(data,this.totalCountingMap.getOrDefault(data,0)+1);
        }



    }
    private void initiateCountingMatrix(){
        String prev = null;
        for (String current : this.dataSet) {
           if(prev == null){
               prev = current;
               continue;
           }

           Map<String,Integer> map  = this.countingMatrix.compute(prev,(k,v)->(v!=null)?v:new HashMap<>());
           map.put(current,map.getOrDefault(current,0)+1);

           prev = current;
        }

        for (String rowKey : this.countingMatrix.keySet()) {
            for (String key : this.totalCountingMap.keySet()) {
                this.countingMatrix.get(rowKey).putIfAbsent(key,0);
            }
        }
    }
    private void initiateProbabilityMatrix(){
        this.countingMatrix.forEach( (k,v)-> v.forEach((k1,v1)-> {
            Map<String,Double> map = this.probabilityMatrix.compute(k,(k2,v2)->(v2!=null)?v2:new HashMap<>());
            map.put(k1,(double)v1/(double)this.totalCountingMap.get(k));
        }));
    }
    public double getProbability(String from,String to){
        return this.probabilityMatrix.get(from).get(to);
    }
    public void calculate(){
        initiateTotalCounting();
        initiateCountingMatrix();
        initiateProbabilityMatrix();
    }

    public static void main(String[] args) {
        MarkovChain mc = new MarkovChain()
                                .addData("on time")
                                .addData("on time")
                                .addData("on time")
                                .addData("late")
                                .addData("on time")
                                .addData("on time")
                                .addData("on time")
                                .addData("very late")
                                .addData("on time")
                                .addData("very late")
                                .addData("on time")
                                .addData("cancelled")
                                .addData("on time")
                                .addData("late")
                                .addData("on time")
                                .addData("very late")
                                .addData("on time")
                                .addData("on time")
                                .addData("on time")
                                .addData("on time");





        mc.calculate();

        System.out.println("totalCountingMap ");
        System.out.println(mc.totalCountingMap);
        System.out.println("countingMatrix ");
        System.out.println(mc.countingMatrix);
        System.out.println("probabilityMatrix ");
        System.out.println(mc.probabilityMatrix);
        mc.printProbabilityMatrix();

        System.out.println(mc.getProbability("late","on time"));
    }
    public void printProbabilityMatrix() {
        String msg = this.probabilityMatrix.entrySet().stream().map(entry->String.format("%s \n %s",entry.getKey(), entry.getValue())).collect(Collectors.joining("\n"));

        logger.info(msg);
    }
    public  String sampleGer(){
        String alphabet = "ABC";
        return String.valueOf(alphabet.charAt(r.nextInt(alphabet.length())));

    }

}
