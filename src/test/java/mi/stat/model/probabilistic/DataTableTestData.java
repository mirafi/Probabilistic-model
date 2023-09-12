package mi.stat.model.probabilistic;

import mi.stat.model.entropy.core.DataTable;

import java.util.concurrent.ThreadLocalRandom;
//TODO need refactoring randomDataPlayTennis
public class DataTableTestData {

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


    private static DataTable randomDataPlayTennis(int n) {
        DataTable dt = new DataTable(n, 3, "yes", "no");


        dt.addTitleValue("outlook", "temp", "humidity");
        for (int i = 0; i < n; i++) {
            dt.addValue(getRandomR(), i, getRandomStr(0), getRandomStr(1), getRandomStr(2));
        }
        return dt;

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
