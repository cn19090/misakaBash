package application.Impl;

import Util.tools;
import application.IProxy;
import bean.Command;
import bean.Result;
import bean.USER;

import java.util.*;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/12/17 8:24
 */
public class Viewer extends Thread {
    private static final long viewer_id=tools.nextId();
    public static USER now_user;
    private static final Scanner sc=new Scanner(System.in);
    private static ClientProxy proxy;
    @Override
    public void run() {
        try {
            proxy=new ClientProxy("localhost",19090,viewer_id);
            Result login_root_root = proxy.run(new Command("login root root",null,viewer_id));
            if(login_root_root.getUser()!=null)
            now_user=login_root_root.getUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print(now_user.getUsername()+"@"+now_user.getNow_path().getName()+">");
        while(true){

            String input=sc.nextLine();

            try {
                Command command=new Command(input,now_user,viewer_id);
                Result run = proxy.run(command);
                if(run.getUser()!=null)
                now_user=run.getUser();
                if(run.getContains().size()>0){
                    if(run.getPrinting().equals("processStart")){
                        List<String> strings = (List<String>) (run.getContains().get(0));
                        System.out.println();
                        List<String[]> bufs=new LinkedList<>();
                        String[] buf={"","","","",""};//操作 当前进程号 程序计数器 申请资源  内存状况
                        for (String string : strings) {
                            //System.out.println(string);
                            String[] s = string.split("-");

                            buf[0]=s[0];
                            switch (s[0]){
                                case "load":
                                    buf[1]=s[1];
                                    String[] s1 = s[2].split(" ");
                                    buf[3]=s1[0]+" (";
                                    for (int i = 0; i < (s1.length-1)/2; i++) {
                                        buf[3]+=s1[2*i+1]+"=>"+s1[2*i+2];
                                        if(i!=(s1.length-1)/2-1){
                                            buf[3]+=",";
                                        }else{
                                            buf[3]+=")";
                                        }
                                    }
                                    break;
                                case "memoryGet":
                                    System.out.println(string);
                                    System.out.println(Arrays.toString(s));
                                    buf[3]="addr "+s[4];
                                    buf[1]=s[1];
                                    buf[4]=s[2]+" "+s[3]+" "+s[4];
                                    break;
                                case "resourceV":
                                    buf[1]=s[1];
                                    buf[2]=s[2];
                                    buf[3]="V ("+s[3]+"<="+s[4]+")";
                                    break;
                                case "resourceP":
                                    buf[1]=s[1];
                                    buf[2]=s[2];
                                    buf[3]="P ("+s[3]+"=>"+s[4]+")";
                                    break;
                                case "run":
                                    buf[1]=s[1];
                                    buf[2]=s[2];
                                    buf[3]="";
                                    break;
                            }
                            String[] curbuf=new String[5];
                            System.arraycopy(buf,0,curbuf,0,5);
                            bufs.add(curbuf);

                        }
                        int[] maxlen=new int[5];
                        for (int i = 0; i <bufs.size() ; i++) {
                            String[] strings1 = bufs.get(i);
                            for (int j = 0; j < 5; j++) {
                                maxlen[j]=Math.max(maxlen[j],strings1[j].length());
                            }
                        }
                        String[] titles={"操作","当前进程编号","程序计数","申请资源","内存状况"};
                        for (int i = 0; i < 5; i++) {
                            System.out.print(titles[i]);
                            maxlen[i]=Math.max(maxlen[i],titles[i].length()*3/2);
                            for (int j = 0; j < Math.floor(maxlen[i]-titles[i].length()*3/2); j++) {
                                System.out.print(" ");
                            }System.out.print("   |   ");
                        }
                        System.out.println();
                        for (int i = 0; i < 5; i++) {
                            for (int j = 0; j < maxlen[i]; j++) {
                                System.out.print("-");
                            }
                            System.out.print("---+---");

                        }
                        System.out.println();
                        for (String[] y : bufs) {

                            for (int i = 0; i < 5; i++) {
                                System.out.print(y[i]);
                                for (int j = 0; j < maxlen[i]-y[i].length(); j++) {
                                    System.out.print(" ");
                                }
                                System.out.print("   |   ");

                            }
                            System.out.println();

                            for (int i = 0; i < 5; i++) {
                                for (int j = 0; j <maxlen[i]; j++) {
                                    System.out.print("-");
                                }
                                System.out.print("---+---");

                            }
                            System.out.println();
                        }

                    }
                }else if(!run.getPrinting().equals("")) {
                    System.out.print(now_user.getUsername() + "@" + now_user.getNow_path().getName() + ">");
                    System.out.println(run.getPrinting());
                }
                System.out.print(now_user.getUsername()+"@"+now_user.getNow_path().getName()+">");
                if(input.equals("shutdown")){
                    break;
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.print(now_user.getUsername()+"@"+now_user.getNow_path().getName()+">");
            }

        }
    }
}
