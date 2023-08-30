package mi.stat.model.utils;

import java.util.*;

public class TimeMeasure {
    static class S implements Comparable<S> {
        long s;
        long e;

        @Override
        public int compareTo(S o) {
            long d1 = e -s;
            long d2 = o.e - o.s;
            if(d1==d2)
                return 0;
            else if(d1>d2)
                return 1;
            else
                return -1;
        }

    }
    private static  Map<String,S> timeMap = new HashMap<>();


    public static void start(String tag){
        S s = new S();
        s.s = System.currentTimeMillis();
        timeMap.put(tag,s);
    }

    public static void  end(String tag){
        S s = timeMap.get(tag);
        s.e = System.currentTimeMillis();
        timeMap.put(tag, s);
    }

    public static void print(){
        SortedSet<Map.Entry<String,S>>  sorted  =entriesSortedByValues(timeMap);
        for(Map.Entry<String,S> e : sorted){
            System.out.println(e.getKey()+" "+(e.getValue().e - e.getValue().s)+" ms");
        }
    }



    static <K,V extends Comparable<? super V>>
    SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res : 1;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }
}
