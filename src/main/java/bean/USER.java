package bean;

import lombok.Data;

import java.io.File;
import java.util.Objects;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/11/17 8:30
 */
@Data
public class USER {
    private long user_id;
    private String username;
    private String passwd;
    private File root_path;
    private File now_path;
    private String permis;
    private String  group;
    public USER(long id,String username,String passwd,String root_path,String permis,String group){
        this.user_id=id;
        this.username=username;
        this.passwd=passwd;
        this.root_path=new File(root_path);
        this.permis=permis;
        this.group=group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        USER user = (USER) o;
        return user_id == user.user_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id);
    }
}
