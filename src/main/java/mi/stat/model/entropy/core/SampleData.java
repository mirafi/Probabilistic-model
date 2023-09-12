package mi.stat.model.entropy.core;

public class SampleData {
    public static DataTable dataSetPayTennis(int n) {
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

}
