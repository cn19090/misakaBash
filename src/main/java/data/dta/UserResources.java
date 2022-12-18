package data.dta;

import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/12/17 11:17
 */
@Data
public class UserResources {
    Map<String,Integer> resourcesMap=new HashMap<>();
    Map<String, LinkedList<Integer>> waitList=new HashMap<>();
    public void put(String key,Integer value){
        resourcesMap.put(key,value);
        waitList.put(key,new LinkedList());
    }
    public void put(String key){
        resourcesMap.put(key,1);
        waitList.put(key,new LinkedList());
    }

    public synchronized Integer getNextWait(String name){
        if(waitList.get(name).isEmpty()){
            return null;
        }
        LinkedList<Integer> integers = waitList.get(name);
        Integer first = integers.getFirst();
        integers.remove(first);
        return first;
    }
    public synchronized void waitFor(Integer pcb_id,String name){
        waitList.get(name).add(pcb_id);
    }


    public synchronized boolean P(String name,int num){
        Integer integer = resourcesMap.get(name);
        if(integer-num<0){
            return false;
        }else{
            resourcesMap.put(name,integer-num);
            return true;
        }
    }
    public synchronized void V(String name,int num){
        resourcesMap.put(name,resourcesMap.get(name)+num);
    }
}
