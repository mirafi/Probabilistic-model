package mi.stat.model.probabilistic;

import mi.stat.model.entropy.core.DataTable;

public class DataTableTestData {

    public static DataTable getTennisData(){
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

        return dt;
    }
}
