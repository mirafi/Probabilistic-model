package mi.stat.model.utils;

import mi.stat.model.entropy.core.DataTable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class EntropyUtils {
    public static DataTable getSubTable(DataTable dataTable, String attrTitle,String attrValue ){
        TimeMeasure.start("Title "+attrTitle+" value "+attrValue+" getSubTable");
        List<String[]> subRows = new ArrayList<>(dataTable.rows.length/2);
        List<String> result = new ArrayList<>(dataTable.rows.length/2);
        String[] newTitle = ArrayUtils.minus(dataTable.titles,attrTitle);

        if(newTitle.length==0)return null;

        for(int i=0;i<dataTable.rows.length;i++){

            String[] rows = dataTable.rows[i];

            if(!ArrayUtils.containsAll(rows,attrValue))continue;

            String[] subSet = ArrayUtils.minus(rows,attrValue);

            subRows.add(subSet);
            result.add(dataTable.result[i]);
         }

        if(subRows.size()==0)return null;

        DataTable subDataTable = new DataTable(subRows.size(),
                dataTable.titles.length - 1,
                dataTable.positiveResultName,
                dataTable.negativeResultName);

        subDataTable.addTitleValue(newTitle);

        for(int i=0;i<subRows.size();i++){
            subDataTable.addValue(result.get(i),i,subRows.get(i));
        }

        TimeMeasure.end("Title "+attrTitle+" value "+attrValue+" getSubTable");
        return subDataTable;

    }
    public static Map.Entry<String,Double> getMaxNode(Map<String,Double> informationGain){
        return  informationGain.entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue)).orElse(null);
    }
}
