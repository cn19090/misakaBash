package data.dta;

import bean.Pcb;
import lombok.Data;

import java.util.LinkedList;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/12/16 19:10
 */
@Data
public class PcbQueue {
    private LinkedList<Pcb> pcbLinkedList=new LinkedList<>();//创建
    private LinkedList<Pcb> avilable =new LinkedList<>();//就绪
    private LinkedList<Pcb> block=new LinkedList<>();//阻塞
    public void addPcb(Pcb pcb){
        if(pcbLinkedList.isEmpty()){
            pcbLinkedList.add(pcb);
        }else{
            int size = pcbLinkedList.size();
            for (int i = 0; i <size ; i++) {
                if(pcbLinkedList.get(i).getSequence()>pcb.getSequence()){
                    pcbLinkedList.add(i,pcb);
                }
            }
        }

    }
    public boolean isBlockEmpty(){
        return block.isEmpty();
    }
    public boolean isAviEmpty(){
        return avilable.isEmpty();
    }
    public boolean isEmpty(){
        return pcbLinkedList.isEmpty();
    }
    public void addAvi(Pcb pcb){
        avilable.add(pcb);
    }
    public void addBlock(Pcb pcb){
        block.add(pcb);
    }

    public Pcb getFirst(){
        Pcb first = pcbLinkedList.getFirst();
        pcbLinkedList.remove(first);
        return first;
    }

    public Pcb getShortest(){
        Pcb p=null;
        int time=-1;
        for (int i = 0; i < pcbLinkedList.size(); i++) {
            Pcb pcb = pcbLinkedList.get(i);
            int totalTime=0;
            for (String s : pcb.getProcessList()) {
                String[] s1 = s.split(" ");
                int i1 = Integer.parseInt(s1[s1.length - 1]);
                totalTime+=i1;
            }
            if((totalTime<time)||(time==-1)){
                p=pcb;
                time=totalTime;
            }
        }
        pcbLinkedList.remove(p);
        return p;
    }



    public Pcb getAvi(){
        Pcb first = avilable.getFirst();
        pcbLinkedList.remove(first);
        return first;
    }

    public Pcb getBlocked(int pcb_id){
        for (Pcb pcb : block) {
            if(pcb.getProcess_id()==pcb_id){
                block.remove(pcb);
                return pcb;
            }
        }

        return null;
    }
    public Pcb getBlockedFirst(){
        Pcb first = block.getFirst();
        block.remove(first);
        return first;
    }
    public void notifyNext(String name,UserResources resources){
        Integer nextWait = resources.getNextWait(name);
        if(nextWait!=null)
        addAvi(getBlocked(nextWait));
    }

}
