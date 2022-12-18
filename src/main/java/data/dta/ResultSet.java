package data.dta;

import bean.Result;
import lombok.Data;

import java.util.List;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/12/13 17:33
 */
@Data
public class ResultSet {
    private List<String[]> rows;
    private List<String> fields;

    public String[] getRow(int pos){
        return rows.get(pos);
    }

    public ResultSet(){

    }

    public ResultSet(List<String[]> rows, List<String> fields) {
        this.rows = rows;
        this.fields = fields;
    }
}
