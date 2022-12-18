package bean;

import Util.tools;
import cn.hutool.core.lang.Snowflake;
import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/12/18 21:26
 */
@Data
public class Result {
    private String type="result";
    private USER user;
    private long res_id= tools.nextId();
    private long timestep=System.currentTimeMillis();
    private String printing;
    private List<Object> contains=new LinkedList<>();
    public Result(Object inp,USER user){
        this.user=user;
        if(inp instanceof InvocationTargetException){
            this.type="err";
            this.printing= ((InvocationTargetException)inp).getTargetException().getClass().getName().replaceAll("(Exception)|([\\w]*?\\.)","");
        }else if(inp instanceof String){
            this.type="show";
            this.printing=(String) inp;
        }else{
            this.type="contain";
            this.contains.add(inp);
        }

    }
}
