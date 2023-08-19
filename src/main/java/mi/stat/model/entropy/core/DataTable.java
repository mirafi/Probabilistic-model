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
        System.out.println();
        for(String title:titles){
            System.out.print(title+" ");
        }
        System.out.println();
        for (int i = 0; i < this.rows.length; i++) {
            String[] row = this.rows[i];

            for (String v : row) {
                System.out.print(v + " ");
            }
            System.out.print(result[i]);
            System.out.println();
        }
    }
}
