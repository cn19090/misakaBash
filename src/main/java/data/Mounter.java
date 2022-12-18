package data;

import Err.MountErr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/11/20 11:28
 */

/**
 * 单例实现
 */
public class Mounter{
    private static Mounter mounter=null;
    private static Scanner sc=new Scanner(System.in);
    static {
        try {
            mounter=new Mounter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private File root_path=null;
    private File databases_path =null;
    private Mounter(){
        try {


            File file=new File("./config.properties");
            if(!file.exists()){
                    file.createNewFile();
                    System.out.println("input mount path:");
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    String str=sc.nextLine();
                    fileOutputStream.write(("root_path:"+str).getBytes(StandardCharsets.UTF_8));
                    fileOutputStream.flush();
                    while(!(new File(str).exists()&&new File(str).isDirectory())){
                        System.out.println("path illegal!");
                        System.out.println("input mount path:");
                        fileOutputStream = new FileOutputStream(file);
                        str=sc.nextLine();
                        fileOutputStream.write(("root_path:"+str).getBytes(StandardCharsets.UTF_8));
                        fileOutputStream.flush();
                    }
                    fileOutputStream.close();
            }
            try {
                Properties properties=new Properties();
                properties.load(new FileInputStream("./config.properties"));
                properties.load(new FileInputStream(file));

                //mount root path
                String r=properties.getProperty("root_path");
                if(r==null){
                    throw new MountErr();
                }else{
                    root_path=new File(r);
                }
                if(!root_path.exists())root_path.mkdirs();

                //mount databases path
                if(r.charAt(r.length()-1)=='/'){
                    r += "data";
                }else{
                    r+= "/data";
                }
                databases_path =new File(r);
                if(!databases_path.exists())
                    databases_path.mkdirs();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("complete!");
    }
    public static Mounter getMounter(){
        return mounter;
    }

    public File getRoot_path() {
        return root_path;
    }

    public File getDatabases_path() {
        return databases_path;
    }

}
