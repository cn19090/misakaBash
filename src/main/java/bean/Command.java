package bean;

import Err.CommandNotFound;
import Util.tools;
import application.IActuator;
import lombok.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/12/17 8:26
 */
@Data
public class Command {
    private String command ="";
    private long view_id;
    private char[]params=new char[128];
    int paramsLength=0;
    private String[] inputs=new String[1024];
    int inputLength=0;

    public Command(String inp,USER user,long viewq_id)throws CommandNotFound {
        this.view_id=viewq_id;
        inp=inp.trim();
        command =(inp+(" ")).substring(0,(inp+(" ")).indexOf(' '));
        if(command.equals("processcreate")){
            String[] s=new String[2048];
            String[] s1 = inp.split(" ");
            String inptype = s1[1];
            Scanner sc=new Scanner(System.in);
            switch (inptype){
                case "FILE":
                    File file=new File(tools.getAbsolutePath(s1[2],user));
                    try {
                        Scanner scanner = new Scanner(file);
                        int count=0;
                        while(scanner.hasNext()){
                            String ts = scanner.nextLine().trim();
                            if(!(ts.charAt(0)=='#')){
                                s[count++]=ts;
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    int count=0;
                    int i = Integer.parseInt(s[count++]);
                    inputLength=i*5+1;

                    inputs[0]=String.valueOf(i);
                    int c=0;
                    while (c<i){
                        String[] buffer=new String[5];
                        buffer[0]=String.valueOf(s[count++]);
                        buffer[1]=String.valueOf(s[count++]);
                        buffer[2]=s[count++];
                        buffer[3]=s[count++];
                        buffer[4]=s[count++];
                        System.arraycopy(buffer,0,inputs,1+c*5,5);
                        c++;
                    }
                    break;
                case "CONSOLE":
                    int count1=0;
                    System.out.println("输入进程数:");

                    int i1 = sc.nextInt();
                    inputLength=i1*5+1;

                    inputs[0]=String.valueOf(i1);
                    int c1=0;
                    while (c1<i1){
                        String[] buffer=new String[5];
                        System.out.println("输入进程属性:");
                        System.out.println("进程编号:");
                        buffer[0]=String.valueOf(sc.nextInt());

                        System.out.println("进程到达顺序:");
                        buffer[1]=String.valueOf(sc.nextInt());

                        System.out.println("进程所需资源情况://格式: (资源名 数量-)+");

                        sc.nextLine();
                        buffer[2]=sc.nextLine();

                        System.out.println("进程所需内存:");
                        buffer[3]=sc.nextLine();

                        System.out.println("进程执行过程://格式: ((资源名 数量)+ 运行所需时间 内存地址-)+");

                        buffer[3]=sc.nextLine();

                        System.arraycopy(buffer,0,inputs,1+c1*5,5);
                        c1++;
                    }
                    break;
            }

        }else{
            boolean flag=true;
            for (Method method : IActuator.getInstance().getClass().getMethods()) {
                if (method.getName().equals(command)) {
                    flag=false;
                    Pattern pattern=Pattern.compile("-[\\w]*? ");
                    Matcher matcher=pattern.matcher(inp);
                    String buf=inp;
                    while(matcher.find()){
                        String group = matcher.group();
                        buf=buf.replaceAll(group,"");
                        String substring = group.substring(1, group.length() - 1);
                        for (int i = 0; i < substring.length(); i++) {
                            params[paramsLength++]=substring.charAt(i);
                        }
                    }
                    inp=buf;
                    String[] s = inp.replaceAll(command, "").trim().split("[ ]{1,}");
                    inputLength=s.length;
                    for (int i = 0; i < s.length; i++) {
                        inputs[i]=s[i];
                    }

                }
            }
            if(flag){
                throw new CommandNotFound();
            }
        }




    }

}
