package mi.stat.model.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtils {
    public static boolean containsAll(String[] main,String... sub ){
        List<String> s1 = new ArrayList<>(Arrays.asList(main));
        List<String> s2 = new ArrayList<>(Arrays.asList(sub));

        return s1.containsAll(s2);
    }
    public static String[] minus(String[] rowValues,String... attrValue ){
        List<String> s1 = new ArrayList<>(Arrays.asList(rowValues));
        List<String> s2 = new ArrayList<>(Arrays.asList(attrValue));

        s1.removeAll(s2);

        return s1.toArray(new String[s1.size()]);

    }
}
