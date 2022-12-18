package data.dta;

import Err.Data.ModelErr;
import lombok.Data;

import java.util.*;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/12/13 17:15
 */
@Data
public class Table {
    private long table_id;
    private String table_name;
    private List<String> Fields;
    private List<String[]> Tuples;

    public void add(Map<String,String> params)throws ModelErr{
        String[] tuple=new String[Fields.size()];
        try {
            for (String s : params.keySet()) {
                tuple[Fields.indexOf(s)]=params.get(s);
            }
            Tuples.add(tuple);
        } catch (Exception e) {
            throw new ModelErr();
        }
    }
    public void del(Map<String,String> params){
        Set<String> strings = params.keySet();
        for (String[] tuple : Tuples) {
            boolean flag=true;
            for (String string : strings) {
                if (!tuple[Fields.indexOf(string)].equals(params.get(string))) {
                    flag=false;
                }
            }
            if (flag) {
                Tuples.remove(tuple);
                flag=true;
            }
        }
    }
    public void update(Map<String, String> inp, Map<String,String> params){

    }

    public Table(long table_id, String table_name, List<String> fields) {
        this.table_id = table_id;
        this.table_name = table_name;
        Fields = fields;
        this.Tuples=new LinkedList<>();
    }
}
