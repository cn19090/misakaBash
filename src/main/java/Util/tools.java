package Util;

import Err.Data.DataBaseNotExist;
import Err.Data.ModelErr;
import Err.Data.TableNotExist;
import bean.USER;
import cn.hutool.core.lang.Snowflake;
import data.ISerializableCache;
import data.Mounter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/11/17 8:44
 */
public class tools {
    private static Snowflake snowflake=new Snowflake();

    public static boolean containsFromStrs(String[] strs,String str){
        for (int i = 0; i < strs.length; i++) {
            if(strs[i].equals(str)){
                return true;
            }
        }
        return false;
    }

    public static USER userLogin(String username, String passwd){
        ISerializableCache instance = ISerializableCache.getInstance();
        USER res=null;
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            if(passwd!=null){
                params.put("passwd",passwd);
            }
            List<String[]> select = instance.select("sys", "user",params );

            if(select.size()==1){
                String[] strings = select.get(0);
                res=new USER(Long.parseLong(strings[0]),strings[1],strings[2],strings[3],strings[4],strings[5]);
            }
        } catch (DataBaseNotExist e) {
            e.printStackTrace();
        } catch (TableNotExist e) {
            e.printStackTrace();
        } catch (ModelErr e) {
            e.printStackTrace();
        }finally {
            return res;
        }
    }

    public static long nextId(){
        return snowflake.nextId();
    }

    public static String getAbsolutePath(String path,USER user){
        StringBuilder stringBuilder=new StringBuilder(path);
        if(stringBuilder.charAt(0)=='/'){
            stringBuilder.insert(0, Mounter.getMounter().getRoot_path());
            path=stringBuilder.toString();
        }else{
            if(stringBuilder.length()>=2&&stringBuilder.substring(0,2).equals("./")){
                stringBuilder.delete(0,2);
            }
            stringBuilder.insert(0, user.getNow_path().getPath()+"/");
            path=stringBuilder.toString();
        }
        String[] split = path.split("/|\\\\");
        String absolute="";
        for (int i = 0; i < split.length; i++) {
            if(split[i].equals("..")){
                absolute=absolute.substring(0,absolute.lastIndexOf("/",absolute.length()-2)+1);
            }else{
                absolute+=split[i];
                if(i!=split.length-1){
                    absolute+="/";
                }
            }
        }
        return absolute;
    }

    public static Map zip(List a,List b){
        Map<Object,Object> map=new HashMap<>();
        if(a.size()==b.size()){
            for (int i = 0; i < a.size(); i++) {
                map.put(a.get(i),b.get(i));
            }
            return map;
        }else{
            return null;
        }
    }

}
