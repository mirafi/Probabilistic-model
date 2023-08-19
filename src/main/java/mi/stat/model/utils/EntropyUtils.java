package mi.stat.model.utils;

import mi.stat.model.entropy.core.DataTable;
import mi.stat.model.entropy.core.Entropy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntropyUtils {
    public static DataTable getSubTable(DataTable dataTable, List<Map<Entropy.Lable,String>> titleValues){

        String[] titles = titleValues.stream().map(map->map.get(Entropy.Lable.TITLE)).toArray(String[]::new);
        String[] attrValues = titleValues.stream().map(map->map.get(Entropy.Lable.VALUE)).toArray(String[]::new);

        List<String[]> subRows = new ArrayList<>(dataTable.rows.length/2);
        List<String> result = new ArrayList<>(dataTable.rows.length/2);
        String[] newTitle = ArrayUtils.minus(dataTable.titles,titles);


        for(int i=0;i<dataTable.rows.length;i++){

            String[] rows = dataTable.rows[i];

            if(!ArrayUtils.containsAll(rows,attrValues))continue;

            String[] subSet = ArrayUtils.minus(rows,attrValues);

            subRows.add(subSet);
            result.add(dataTable.result[i]);
        }

        DataTable subDataTable = new DataTable(subRows.size(),
                dataTable.titles.length - attrValues.length,
                dataTable.positiveResultName,
                dataTable.negativeResultName);

        subDataTable.addTitleValue(newTitle);

        for(int i=0;i<subRows.size();i++){
            subDataTable.addValue(result.get(i),i,subRows.get(i));
        }

        return subDataTable;

    }
}
