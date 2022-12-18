package data.dta;

import Util.tools;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/12/13 17:14
 */
@Data
public class Database {
    private long databaseId;
    private String databaseName;
    private Map<String,Table> tables;

    public void createTable(String tablename,List<String> model){
        this.tables.put(tablename,new Table(tools.nextId(),tablename,model));
    }

    public Table getTableByName(String tableName){
        return tables.get(tableName);
    }


    public Database(long id,String databaseName){
        this.databaseId=id;
        this.databaseName=databaseName;
        this.tables=new HashMap<>();
    }
    public Database(long id,String databaseName,Map<String,Table> tables){
        this.databaseId=id;
        this.databaseName=databaseName;
        this.tables=tables;
    }

}
