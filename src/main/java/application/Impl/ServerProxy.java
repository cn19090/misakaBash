package application.Impl;

import application.IActuator;
import application.IProxy;
import bean.Command;
import bean.Result;
import com.google.gson.Gson;
import data.ICommandQueue;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/12/18 0:37
 */
public class ServerProxy implements IProxy {
    private static Map<Long,Producer> threadMap =new HashMap<>();
    private static Map<Long,OutputStream> outMap=new HashMap<>();
    private static ICommandQueue commQue=ICommandQueue.getInstance();
    private static ServerSocket serverSocket;
    private static Gson gson=new Gson();
    public void run() {
        try {
            serverSocket=new ServerSocket(19090);
            Consumer consumer = new Consumer();
            consumer.start();
            while (true) {
                Socket accept = serverSocket.accept();
                Producer producer = new Producer(accept);
                long l = new DataInputStream(accept.getInputStream()).readLong();
                threadMap.put(l,producer);
                outMap.put(l,accept.getOutputStream());
                producer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Producer extends Thread{
        private InputStream in;
        @Override
        public void run() {
            while(true){
                        try {
                            Command command = getCommand(in);
                            commQue.add(command);
                        } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

        }

        public Command getCommand(InputStream in)throws Exception{
            ByteArrayOutputStream o=new ByteArrayOutputStream();
            byte[] buffer=new byte[1024];
            int len=new DataInputStream(in).readInt();
            int recLen=0;
            while(recLen<len){
                int rec=in.read(buffer);
                o.write(buffer,0,rec);
                recLen+=rec;
            }
            return gson.fromJson(o.toString(),Command.class);

        }


        public Producer(Socket socket){
            try {
                this.in=socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Consumer extends Thread{
        private IActuator act=IActuator.getInstance();
        @Override
        public void run() {
            while(true){
                if(commQue.size()>0){
                    Command command = commQue.getAndDel();
                    try {
                        Method method = act.getClass().getMethod(command.getCommand(),command.getClass());
                        Result result= (Result)method.invoke(act, new Object[]{command});
                        sendResult(result,command);
                    } catch (InvocationTargetException e) {
                        sendResult(new Result(e,null),command);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }else{
                    Thread.yield();
                }
            }
        }
        private void sendResult(Result result,Command command){
            String s = gson.toJson(result);
            byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
            OutputStream out=outMap.get(command.getView_id());
            try {
                new DataOutputStream(out).writeInt(bytes.length);
                out.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
