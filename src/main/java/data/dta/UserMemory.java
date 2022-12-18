package data.dta;

import lombok.Data;

import java.util.Arrays;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/12/18 11:14
 */
@Data
public class UserMemory {
    private int[] memorys;
    private int[] frames;
    private int[] status;
    private int len = 0;
    private int pos;
    private String arithmetic;
    private String structure;
    public UserMemory(String[] s){

        structure=s[0];
        arithmetic =s[1];
        if(structure.equals("FRAME")){
            frames=new int[Integer.parseInt(s[2])];
            status=new int[Integer.parseInt(s[2])];
            len=frames.length;
            pos=0;
        }else{
            len=s.length - 3;
            memorys=new int[len];
            pos=-1;
            for (int i = 0; i <len ; i++) {
                memorys[i]=Integer.parseInt(s[i+3]);
            }
        }

    }
    public int applyMem(int mem){
        switch (structure){
            case "BLOCK":
                switch (arithmetic){
                    case "FF"://首次适应
                        for (int i = 0; i < len; i++) {
                            if((memorys[i]>mem)&&(status[i]==0)){
                                status[i]=1;
                                return i;
                            }
                        }
                        return -1;
                    case "BF"://最佳适应
                        int min=memorys[0];
                        pos=-1;
                        for (int i = 0; i < len; i++) {
                            if((status[i]==0)&&(mem<memorys[i])&&(min>(memorys[i]-mem))){
                                min=memorys[i]-mem;
                                pos=i;
                            }
                        }
                        return pos;
                    case "WF"://最坏适应
                        int max=memorys[0];
                        pos=-1;
                        for (int i = 0; i < len; i++) {
                            if((status[i]==0)&&(mem<memorys[i])&&(memorys[i]>max)){
                                max=memorys[i];
                                pos=i;
                            }
                        }
                        return pos;
                }
                break;
            case "FRAME":

                return 0;

        }
        return -1;
    }

    public int getAddr(int addr){
        switch (arithmetic){
            case "FCFS"://先进先出
                int r=pos%len;
                frames[r]=addr;
                pos++;
                return r;
            case "LRU"://最近最少使用
                for (int i = 0; i < len; i++) {
                    if(status[i]==0){
                        status[i]=1;
                        frames[i]=addr;
                        return i;
                    }
                }

                int max=status[0];
                int j=0;
                for (int i = 0; i < len; i++) {
                    if(status[i]>max){
                        j=i;
                        max=status[i];
                    }
                    status[i]++;
                }

                frames[j]=addr;
                status[j]=1;

                return j;
        }
        return -1;
    }
    public void freeMem(int pos){
        switch (structure){
            case "BLOCK":
                status[pos]=0;
                break;
            case "FRAME":
                switch (arithmetic){
                    case "FCFS"://先进先出

                        break;
                    case "LRU"://最近最少使用
                        break;
                }
                break;
        }
    }

    @Override
    public String toString() {
       if(structure.equals("BLOCK")){
           return  "BLOCK-"+Arrays.toString(memorys);
       }else{
           return "FRAME-"+Arrays.toString(frames)+"-"+Arrays.toString(status);
       }
    }
}
