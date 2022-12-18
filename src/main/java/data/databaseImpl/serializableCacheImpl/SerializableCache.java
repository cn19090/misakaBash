package data.databaseImpl.serializableCacheImpl;

import Err.Data.DataBaseExist;
import Err.Data.DataBaseNotExist;
import Err.Data.ModelErr;
import Err.Data.TableNotExist;
import Util.tools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import data.dta.Database;
import data.ISerializableCache;
import data.Mounter;
import data.dta.Table;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/11/20 12:26
 */
public class SerializableCache implements ISerializableCache {
    public static File databases_path= Mounter.getMounter().getDatabases_path();
    public static Gson gson=new Gson();
    private static Map<String, Database> databaseMap;

    public static boolean isInited(){
        return databaseMap!=null;
    }

    @Override
    public void createDatabase(String name) throws DataBaseExist{
        databaseMap.put(name,new Database(tools.nextId(),name));
    }

    @Override
    public void createTable(String databasename,String tablename,List<String> model) throws DataBaseNotExist{
        Database database = databaseMap.get(databasename);
        if(database==null){
            throw new DataBaseNotExist();
        }else{
            database.createTable(tablename,model);
        }

    }

    @Override
    public List<String[]> select(String databasename,String tablename, Map<String, String> inp)throws DataBaseNotExist,TableNotExist,ModelErr {
        List<String[]> list=new LinkedList<>();
        Table tableByName=getTable(databasename,tablename);

        List<String> fields = tableByName.getFields();
        List<String[]> tuples = tableByName.getTuples();
        Set<String> strings = inp.keySet();
        Map<String,Integer> buf=new HashMap<>();
        for (String string : strings) {
            try {
                buf.put(string,fields.indexOf(string));
            } catch (Exception e) {
                throw new ModelErr();
            }
        }
        for (int i = 0; i < tuples.size(); i++) {
            String[] tup = tuples.get(i);
            boolean flag=true;
            for (String s : buf.keySet()) {
                if(tup[buf.get(s)]==null){
                    if(!(inp.get(s).equals("")||inp.get(s)==null)){
                        flag=false;
                        break;
                    }

                }else if ((!tup[buf.get(s)].equals(inp.get(s)))) {
                    flag=false;
                    break;
                }
            }
            if (flag) {
                list.add(tup);
            }
        }
        return list;
    }

    @Override
    public void insert(String databasename,String tablename, Map<String,String> params) throws DataBaseNotExist,TableNotExist,ModelErr{
        Table table = getTable(databasename, tablename);
        table.add(params);
    }

    @Override
    public void delLine(String databasename, String tablename, Map<String, String> inp)throws DataBaseNotExist,TableNotExist,ModelErr {
        Table table = getTable(databasename, tablename);
        table.del(inp);
    }

    @Override
    public void update(String databasename,String tablename, Map<String, String> inp, Map<String,String> params)throws TableNotExist,DataBaseNotExist {
        Table table = getTable(databasename, tablename);
        table.update(inp,params);
    }


    @Override
    public void delTable(String databasename, String tablename) throws TableNotExist,DataBaseNotExist{

    }

    @Override
    public void delDatabase(String databasename)throws DataBaseNotExist {
        if (databaseMap.containsKey(databasename)) {
            databaseMap.remove(databasename);
        }else{
            throw new DataBaseNotExist();
        }
    }

    public static void init() {
        File databases_path = Mounter.getMounter().getDatabases_path();
        String path = databases_path.getPath()+"/database.save";
        File file = new File(path);
        try {
            InputStream in=new FileInputStream(file);
            int len=-1;
            ByteArrayOutputStream o=new ByteArrayOutputStream();
            byte[] buf=new byte[1024];
            while((len=in.read(buf))!=-1){
                o.write(buf,0,len);
            }
            databaseMap=gson.fromJson(o.toString(),new TypeToken<HashMap<String, Database>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void commit(){
        File databases_path = Mounter.getMounter().getDatabases_path();
        String path = databases_path.getPath()+"/database.save";
        File file = new File(path);
        try {
            OutputStream out=new FileOutputStream(file);
            out.write(gson.toJson(databaseMap).getBytes(StandardCharsets.UTF_8));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() throws DataBaseNotExist {
        try {
            createDatabase("sys");
            List list1 = gson.fromJson("[\"user_id\",\"username\",\"passwd\",\"root_path\",\"group\",\"permis\"]", List.class);
            createTable("sys","user",list1);
            List list = gson.fromJson("[\"1\",\"root\",\"root\",\"./root\",\"root\",\"admin\"]", List.class);
            insert("sys","user",tools.zip(list1,list));
            commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Table getTable(String databaseName,String tableName)throws DataBaseNotExist,TableNotExist{
        Table table=null;
        Database database = null;
        try {
            database = databaseMap.get(databaseName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (database == null) {
            throw new DataBaseNotExist();
        }else{
            Table tableByName = database.getTableByName(tableName);
            if(tableByName==null){
                throw new TableNotExist();
            }else{
                table=tableByName;
            }
        }
        return table;
    }

}
