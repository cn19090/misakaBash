package data;

import data.databaseImpl.CacheImpl.Cache;

import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/11/22 23:34
 */
public interface ICache {
    public static ICache getInstance(){
        return new Cache();
    }
    public void put(String name, Object resou);
    public void updateStack(String name, Object resou);
    public void updateMap(String name, String key, Object value);
    public Stack getStack(String name);
    public List getList(String name);
    public Map getMap(String name);
    public Object get(String name);
    public boolean containsKey(String key);
}
