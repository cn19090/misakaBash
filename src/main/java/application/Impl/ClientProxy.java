package application.Impl;

import application.IActuator;
import application.IProxy;
import bean.Command;
import bean.Result;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/11/22 22:01
 */
public class ClientProxy implements IProxy {
    private long viewer_id;
    private Socket socket;
    private static final Gson gson=new Gson();
    private OutputStream out;
    private InputStream in;
    public Result run(Command command){

        Result invoke=null;

        try {
            byte[] bytes = gson.toJson(command).getBytes(StandardCharsets.UTF_8);
            new DataOutputStream(out).writeInt(bytes.length);
            out.write(bytes);
            ByteArrayOutputStream o=new ByteArrayOutputStream();

            byte[] buffer=new byte[1024];
            int len=new DataInputStream(in).readInt();
            int recLen=0;
            while(recLen<len){
                int rec=in.read(buffer);
                o.write(buffer,0,rec);
                recLen+=rec;
            }
            invoke = gson.fromJson(o.toString(),Result.class);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return invoke;
        }

    }
    public ClientProxy(String host,int port,long viewer_id){
        try {
            this.socket=new Socket(host,port);
            this.viewer_id=viewer_id;
            this.out=socket.getOutputStream();
            this.in=socket.getInputStream();
            new DataOutputStream(this.out).writeLong(this.viewer_id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
