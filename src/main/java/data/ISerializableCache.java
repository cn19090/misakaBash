package data;

import Err.Data.DataBaseExist;
import Err.Data.DataBaseNotExist;
import Err.Data.ModelErr;
import Err.Data.TableNotExist;
import data.databaseImpl.serializableCacheImpl.SerializableCache;

import java.util.List;
import java.util.Map;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/11/20 12:18
 */
public interface ISerializableCache {

    public static ISerializableCache getInstance(){
        if(!SerializableCache.isInited()){
            SerializableCache.init();
        }
        return new SerializableCache();
    }

    public void createDatabase(String name)throws DataBaseExist;

    /**
     *
     * @param databasename
     * @return
     */
    public void createTable(String databasename,String tablename,List<String> model)throws DataBaseNotExist;

    /**
     * 返回根据条件查询到的结果表
     * 外层list存元组，内层list存元素
     * @param table
     * @param inp
     * @return
     */
    public List<String[]> select(String databasename,String table,Map<String,String> inp)throws DataBaseNotExist, TableNotExist, ModelErr;

    /**
     *
     * @param table
     * @param params
     * @return
     */
    public void insert(String databasename,String table,Map<String,String> params)throws DataBaseNotExist,TableNotExist, ModelErr;

    /**
     *
     * @param table
     * @param inp
     * @return
     */
    public void delLine(String databasename, String table, Map<String,String> inp)throws DataBaseNotExist,TableNotExist,ModelErr ;

    /**
     *
     * @param table
     * @param inp
     * @param params
     * @return
     */
    public void update(String databasename,String table,Map<String,String> inp, Map<String,String> params)throws TableNotExist,DataBaseNotExist;

    public void delTable(String databasename,String tablename)throws TableNotExist,DataBaseNotExist;

    public void delDatabase(String databasename)throws DataBaseNotExist;

    public void commit();
}
