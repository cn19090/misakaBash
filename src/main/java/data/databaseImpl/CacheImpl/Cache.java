package data.databaseImpl.CacheImpl;

import data.ICache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/11/22 23:33
 */
public class Cache implements ICache {
    private static Map<String,Object> ram=new HashMap<>();
    @Override
    public void put(String name, Object resou){
        ram.put(name,resou);
    }
    @Override
    public void updateStack(String name, Object resou){
        Stack o = (Stack) ram.get(name);
        o.push(resou);
    }
    @Override
    public void updateMap(String name, String key, Object value){
        Map<String,Object> map=(Map<String,Object>)ram.get(name);
        map.put(key,value);
    }

    @Override
    public Stack getStack(String name) {
        if(ram.containsKey(name)){
            return (Stack) ram.get(name);
        }else{
            return null;
        }

    }

    @Override
    public List getList(String name) {
        if(ram.containsKey(name)){
            return (List) ram.get(name);
        }else{
            return null;
        }
    }

    @Override
    public Map getMap(String name) {
        if(ram.containsKey(name)){
            return (Map)ram.get(name);
        }else{
            return null;
        }
    }

    @Override
    public Object get(String name) {
        if(ram.containsKey(name)){
            return  ram.get(name);
        }else{
            return null;
        }
    }

    @Override
    public boolean containsKey(String key) {
        return ram.containsKey(key);
    }


}
