package go;

import application.Impl.ServerProxy;
import application.Impl.Viewer;
import data.Mounter;

import java.util.Scanner;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/12/17 8:14
 */
public class Starter {
    public static final Scanner sc=new Scanner(System.in);
    public static String root_path="./";
    public static Mounter mounter=Mounter.getMounter();
    public static Viewer viewer=new Viewer();
    public static void main(String[] args) throws Exception{
            new Thread(()->{
                new ServerProxy().run();
            }).start();
            viewer.start();

    }
}
