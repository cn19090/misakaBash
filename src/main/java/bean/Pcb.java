package bean;

import lombok.Data;

import java.util.*;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/12/15 0:33
 */
@Data
public class Pcb implements Comparable{
    public static List<String> resultQueue=new LinkedList<>();
    private int process_id;
    private int Sequence;
    private int storageNeeded;
    private Map<String,Integer> resourceNeeded=new HashMap<>();
    private List<String>processList;
    private int pos=0;


    public void setResourceNeeded(String inp){
        String[] split = inp.split("-");
        for (int i = 0; i < split.length; i++) {
            String[] s = split[i].trim().split(" ");
            resourceNeeded.put(s[0],Integer.parseInt(s[1]));
        }
    }
    public void setProcessList(String inp){
        String[] split = inp.split("-");
        for (int j = 0; j < split.length; j++) {
            split[j]=split[j].trim();
        }
        processList= Arrays.asList(split);
    }

    public synchronized String next(){
        if(pos<processList.size()){
            int i = processList.get(pos).lastIndexOf(" ");
            String s = processList.get(pos);
            StringBuilder stringBuilder = new StringBuilder(s);
            stringBuilder.replace(i,i+1,"-");
            i=stringBuilder.lastIndexOf(" ",i);
            stringBuilder.replace(i,i+1,"-");
            resultQueue.add("load-"+process_id+"-"+stringBuilder.toString());
            return processList.get(pos++);
        }else{
            return null;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pcb pcb = (Pcb) o;
        return process_id == pcb.process_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(process_id);
    }

    @Override
    public int compareTo(Object o) {
        Pcb oo=(Pcb) o;
        return Sequence-oo.getSequence();
    }
}
