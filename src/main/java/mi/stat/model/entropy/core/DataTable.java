package mi.stat.model.entropy.core;

public class DataTable {
    public String[] titles;
    public String[][] rows;
    public String[] result;

    public String positiveResultName;
    public String negativeResultName;




    public DataTable(int size, int attributeSize, String positiveResult, String negativeResult) {
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

    public  void addValue(String resultValue, int i, String... values) {
        rows[i] = values;
        result[i] = resultValue;
    }

    public void addTitleValue(String... titles) {
        if (titles.length != this.titles.length) throw new RuntimeException("Title length not equals");
        this.titles = titles;
    }


    public String getKey(int i, int j) {
        return this.titles[j] + "_" + this.rows[i][j];
    }

    public String getResult(int i) {
        return this.result[i];
    }

    public void print() {
       print(this.rows.length);
    }

    public void print(int limit) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        for(String title:titles){
            stringBuilder.append(title+" ");
        }
        stringBuilder.append("\n");
        for (int i = 0; i < this.rows.length && i<limit ; i++) {
            String[] row = this.rows[i];

            for (String v : row) {
                stringBuilder.append(v + " ");
            }
            stringBuilder.append(result[i]);
            stringBuilder.append("\n");
        }
        if(limit<this.rows.length){
            stringBuilder.append(" . \n . \n . \n n="+this.rows.length+" \n ");
        }
        System.out.println(stringBuilder);
    }

}
