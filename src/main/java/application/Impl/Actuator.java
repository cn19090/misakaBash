package application.Impl;

import Err.CommandInputErr;
import Err.Data.LackMemory;
import Err.File.FileExists;
import Err.File.PathInvalid;
import Err.User.UserExist;
import Err.User.UserHasLogin;
import Err.User.UserNotFound;
import Util.tools;
import application.IActuator;
import bean.Command;
import bean.Pcb;
import bean.Result;
import bean.USER;
import data.ISerializableCache;
import data.Mounter;
import data.ICache;
import data.dta.PcbQueue;
import data.dta.UserMemory;
import data.dta.UserResources;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/12/17 8:25
 */
public class Actuator implements IActuator {
    private static ICache cache = ICache.getInstance();
    private static ISerializableCache iDatabase = ISerializableCache.getInstance();
    private static Stack<USER> getUserStack(Command cmd){
        return cache.getStack(cmd.getView_id()+"_"+"userStack");
    }

    @Override
    public Result useradd(Command cmd) {
        Map<String,String> params=new HashMap<>();
        params.put("user_id",tools.nextId()+"");
        params.put("username",cmd.getInputs()[0]);
        params.put("root_path","./"+cmd.getInputs()[0]);
        try {
            Map<String,String> map=new HashMap<>();
            map.put("username",cmd.getInputs()[0]);
            List<String[]> select = iDatabase.select("sys", "user", map);
            if(select.size()>0){
                throw new UserExist();
            }
            iDatabase.insert("sys","user",params);
            File file= new File(Mounter.getMounter().getRoot_path().getPath()+"/"+cmd.getInputs()[0]);
            file.mkdirs();
        } catch (Exception e) {
            return new Result(e.getClass().getName(),null);
        }
        return new Result("useradd complete!",null);
    }

    @Override
    public Result usermod(Command cmd) {
        return null;
    }

    @Override
    public Result userdel(Command cmd) {
        return null;
    }

    @Override
    public Result passwd(Command cmd) {
        return null;
    }

    @Override
    public Result chmod(Command cmd) {
        return null;
    }

    @Override
    public Result chown(Command cmd) {
        return null;
    }

    @Override
    public Result groupadd(Command cmd) {
        return null;
    }

    @Override
    public Result groupdel(Command cmd) {
        return null;
    }

    @Override
    public Result ls(Command cmd) {
        Stack<USER> stack = getUserStack(cmd);
        File[] files = stack.peek().getNow_path().listFiles();
        String[] list =new String[files.length];
        for (int i = 0; i < list.length; i++) {
            list[i]=(files[i].isFile()?"\033[34m":"\033[37m")+files[i].getName();
        }
        String s = Arrays.toString(list);
        return new Result(s.substring(1,s.length()-1)+"\033[0m",stack.peek());
    }

    @Override
    public Result ll(Command cmd) {
        return null;
    }

    @Override
    public Result mkdir(Command cmd) throws FileExists,PathInvalid{
        Stack<USER>stack= getUserStack(cmd);
        String input = cmd.getInputs()[0];
        input=tools.getAbsolutePath(input,stack.peek());
        File file=new File(input);
        if(file.exists()){
            throw new FileExists();
        }
        char[] params = cmd.getParams();
        int paramLength=cmd.getParamsLength();
        for (int i = 0; i < paramLength; i++) {
            if(params[i]=='p'){
                file.mkdirs();
                return new Result("",stack.peek());
            }
        }
        boolean mkdir = file.mkdir();
        if(!mkdir){
            throw new PathInvalid();
        }

        return new Result("",stack.peek());
    }

    @Override
    public Result touch(Command cmd) {
        Stack<USER> uSerStack = getUserStack(cmd);
        try {
            String path = uSerStack.peek().getNow_path().getPath();
            new File(path+"/"+cmd.getInputs()[0]).createNewFile();
            return new Result("create a file at "+path+"/"+cmd.getInputs()[0],uSerStack.peek());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Result rm(Command cmd) {
        char[] params = cmd.getParams();
        int paramsLength = cmd.getParamsLength();
        Stack<USER> stack= getUserStack(cmd);
        String absolutePath = tools.getAbsolutePath(cmd.getInputs()[0], stack.peek());
        boolean flagR=false;
        for (int i = 0; i < paramsLength; i++) {
            if(params[i]=='r'){
                flagR=true;
            }
        }
        if(flagR){
            Stack<File> b=new Stack<>();
            Stack<Integer> signal=new Stack<>();
            b.push(new File(absolutePath));
            signal.push(0);
            while(!b.isEmpty()){
                File peek = b.peek();
                if(peek.isDirectory()){
                    if(signal.peek()<b.peek().list().length){
                        b.push(new File(b.peek().getPath()+"/"+b.peek().list()[signal.peek()]));
                        signal.push(signal.pop()+1);
                        signal.push(0);
                    }else{
                        b.pop().delete();
                        signal.pop();
                    }


                }else{
                    peek.delete();
                    b.pop();
                }
            }
        }else{
            new File(absolutePath).delete();
        }
        return new Result("",stack.peek());
    }

    @Override
    public Result cat(Command cmd) {
        Stack<USER> stack= getUserStack(cmd);
        String absolutePath = stack.peek().getNow_path().getAbsolutePath()+"/"+cmd.getInputs()[0];
        File file = new File(absolutePath);
        ByteArrayOutputStream ou=new ByteArrayOutputStream();
        try {
            InputStream in=new FileInputStream(file);

            int len=-1;
            byte[] buf=new byte[1024];
            while((len=in.read(buf))!=-1){
                ou.write(buf,0,len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result("\n"+ou.toString(),stack.peek());
    }

    @Override
    public Result grep(Command cmd) {
        return null;
    }

    @Override
    public Result nohup(Command cmd) {
        return null;
    }

    @Override
    public Result tar(Command cmd) {
        return null;
    }

    @Override
    public Result untar(Command cmd) {
        return null;
    }

    @Override
    public Result zip(Command cmd) {
        return null;
    }

    @Override
    public Result unzip(Command cmd) {
        return null;
    }

    @Override
    public Result rar(Command cmd) {
        return null;
    }

    @Override
    public Result unrar(Command cmd) {
        return null;
    }

    @Override
    public Result netstat(Command cmd) {
        return null;
    }

    @Override
    public Result wget(Command cmd) {
        Stack<USER> stack= getUserStack(cmd);
        try {
            URL url=new URL(cmd.getInputs()[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream in=con.getInputStream();
            File file=new File(stack.peek().getNow_path().getPath()+"/"+cmd.getInputs()[0].substring(cmd.getInputs()[0].lastIndexOf("/")));
            OutputStream out=new FileOutputStream(file);
            int len=-1;
            byte[] buf=new byte[1024];
            while((len=in.read(buf))!=-1){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result("",stack.peek());
    }

    @Override
    public Result ps(Command cmd) {
        return null;
    }

    @Override
    public Result kill(Command cmd) {
        return null;
    }
    @Override
    public Result jobs(Command cmd) {
        return null;
    }

    @Override
    public Result login(Command cmd)throws UserHasLogin,UserNotFound,CommandInputErr {
            Result result=new Result("",null);
            String[] inputs = cmd.getInputs();
            int len=cmd.getInputLength();
            if(len==2||len==1){
                USER user=tools.userLogin(inputs[0],len==2?inputs[1]:null);
                user.setNow_path(user.getRoot_path());
                if(!user.getRoot_path().exists()){
                    user.getRoot_path().mkdir();
                }
               if(user!=null){
                   Stack<USER> stack= getUserStack(cmd);
                   if(stack!=null){
                       if(stack.contains(user)){
                           throw new UserHasLogin();
                       }
                   }else{
                       cache.put(cmd.getView_id()+"_"+"userStack",new Stack<USER>());
                   }
                   //登陆成功
                   cache.updateStack(cmd.getView_id()+"_"+"userStack",user);
                   result.setUser(user);

               }else{
                   throw new UserNotFound();
               }
            }
            else{
                throw new CommandInputErr();
            }


        return result;
    }

    @Override
    public Result pwd(Command cmd) {
            Stack<USER> stack= getUserStack(cmd);
            return new Result(stack.peek().getNow_path().getPath(),stack.peek());
    }

    @Override
    public Result cd(Command cmd) throws Exception {
        Stack<USER> stack= getUserStack(cmd);
        stack.peek().setNow_path(new File(tools.getAbsolutePath(cmd.getInputs()[0],stack.peek())));
        return new Result("",stack.peek());
    }



    @Override
    public Result resourceDestribute(Command cmd) throws Exception {
        Stack<USER> users = getUserStack(cmd);
        USER user = users.peek();
        long user_id = user.getUser_id();

        UserResources map=new UserResources();
        String[] inputs = cmd.getInputs();
        for (int i = 0; i < cmd.getInputLength(); i+=2) {
            map.put(inputs[i],Integer.parseInt(inputs[i+1]));
        }
        cache.put("user_resource_"+user_id,map);
        return new Result("complete",null);
    }

    @Override
    public Result memoryDestribute(Command cmd)throws Exception{
        Stack<USER> users = getUserStack(cmd);
        USER user = users.peek();
        long user_id = user.getUser_id();
        UserMemory memory=new UserMemory(cmd.getInputs());
        cache.put("user_memory_"+user_id,memory);
        return new Result("complete",null);
    }
    @Override
    public Result processcreate(Command cmd) throws Exception {
        Stack<USER> users = getUserStack(cmd);
        USER user = users.peek();
        long user_id = user.getUser_id();

        String[] inputs = cmd.getInputs();
        if(cache.containsKey("user_pcblist_"+user_id)){

        }else{
            cache.put("user_pcblist_"+user_id,new PcbQueue());
        }
        PcbQueue list = (PcbQueue)cache.get("user_pcblist_" + user_id);
        for (int i = 0; i < Integer.parseInt(inputs[0]); i++) {
            Pcb pcb=new Pcb();
            pcb.setProcess_id(Integer.parseInt(inputs[i*5+1]));
            pcb.setSequence(Integer.parseInt(inputs[i*5+2]));
            pcb.setResourceNeeded(inputs[i*5+3]);
            pcb.setStorageNeeded(Integer.parseInt(inputs[i*5+4]));
            pcb.setProcessList(inputs[i*5+5]);
            list.addPcb(pcb);
        }
        return new Result("complete",null);
    }

    @Override
    public Result processStart(Command cmd) throws Exception {
        Stack<USER> userStack = getUserStack(cmd);
        long user_id=userStack.peek().getUser_id();
        String[] inputs = cmd.getInputs();

        PcbQueue pcbs=(PcbQueue) cache.get("user_pcblist_" + user_id);
        UserResources resource=(UserResources)cache.get("user_resource_"+user_id);
        UserMemory memory=(UserMemory)cache.get("user_memory_"+user_id);

        switch (inputs[0]){
            case "FCFS"://先来先服务
                while(!pcbs.isEmpty()||!pcbs.isAviEmpty()){
                    Pcb now=null;
                    if(pcbs.isAviEmpty()){
                        now = pcbs.getFirst();
                    }else{
                        now=pcbs.getAvi();

                    }
                    int time=0;//时间戳
                    String next=null;
                    int pos=memory.applyMem(now.getStorageNeeded());
                    if(pos==-1){
                        Pcb.resultQueue.add("createFail_lackMemory-"+now.getProcess_id()+"-"+now.getStorageNeeded());
                        pcbs.addPcb(now);
                        continue;
                    }
                    l:while((next=now.next())!=null){
                        time++;
                        String[] s = next.split(" ");
                        int i=1;
                        int count=now.getPos();//获得进程的计数器
                        //申请资源
                        while(i+1<s.length-1){
                            memory.getAddr(Integer.parseInt(s[s.length-1]));
                            Pcb.resultQueue.add("memoryGet-"+now.getProcess_id()+"-"+memory.toString()+"-"+s[s.length-1]);

                            if(s[0].toUpperCase().equals("P")){
                                if(resource.P(s[i],Integer.parseInt(s[i+1]))){
                                    Pcb.resultQueue.add("resourceP-"+now.getProcess_id()+"-"+count+"-"+s[i]+"-"+s[i+1]);
                                }else{
                                    //计数器回退
                                    now.setPos(now.getPos()-1);
                                    //资源不足，转阻塞，进不足资源的等待队列
                                    Pcb.resultQueue.add("block_lackResource-"+now.getProcess_id()+"-"+count+"-"+s[i]+"-"+s[i+1]);

                                    pcbs.addBlock(now);
                                    resource.waitFor(now.getProcess_id(), s[i]);
                                    break l;
                                }
                                i+=2;
                                //资源申请成功，开始运行
                                Pcb.resultQueue.add("run-"+now.getProcess_id()+"-"+count);
                                //运行完毕，释放资源
                                i=1;
                                while(i+1<s.length-1){
                                    resource.V(s[i],Integer.parseInt(s[i+1]));
                                    pcbs.notifyNext(s[i],resource);
                                    i+=2;
                                }
                            }else{
                                Pcb.resultQueue.add("resourceV-"+now.getProcess_id()+"-"+count+"-"+s[i]+"-"+s[i+1]);
                                //资源申请成功，开始运行
                                Pcb.resultQueue.add("run-"+now.getProcess_id()+"-"+count);
                                i=1;
                                while(i+1<s.length-1){
                                    resource.V(s[i],Integer.parseInt(s[i+1]));
                                    pcbs.notifyNext(s[i],resource);
                                    i+=2;
                                }
                            }

                        }


                    }
                    memory.freeMem(pos);
                }
                break;
            case "SJF"://短作业优先
                while(!pcbs.isEmpty()||!pcbs.isAviEmpty()){
                    Pcb now=null;
                    if(pcbs.isAviEmpty()){
                        now = pcbs.getShortest();
                    }else{
                        now=pcbs.getAvi();

                    }
                    int time=0;//时间戳
                    String next=null;
                    int pos=memory.applyMem(now.getStorageNeeded());
                    if(pos==-1){
                        Pcb.resultQueue.add("createFail_lackMemory-"+now.getProcess_id()+"-"+now.getStorageNeeded());
                        pcbs.addPcb(now);
                        continue;
                    }
                    l:while((next=now.next())!=null){
                        time++;
                        String[] s = next.split(" ");
                        int i=1;
                        int count=now.getPos();//获得进程的计数器
                        //申请资源
                        while(i+1<s.length-1){
                            memory.getAddr(Integer.parseInt(s[s.length-1]));
                            Pcb.resultQueue.add("memoryGet-"+now.getProcess_id()+"-"+memory.toString()+"-"+s[s.length-1]);

                            if(s[0].toUpperCase().equals("P")){
                                if(resource.P(s[i],Integer.parseInt(s[i+1]))){
                                    Pcb.resultQueue.add("resourceP-"+now.getProcess_id()+"-"+count+"-"+s[i]+"-"+s[i+1]);
                                }else{
                                    //计数器回退
                                    now.setPos(now.getPos()-1);
                                    //资源不足，转阻塞，进不足资源的等待队列
                                    Pcb.resultQueue.add("block_lackResource-"+now.getProcess_id()+"-"+count+"-"+s[i]+"-"+s[i+1]);

                                    pcbs.addBlock(now);
                                    resource.waitFor(now.getProcess_id(), s[i]);
                                    break l;
                                }
                                i+=2;
                                //资源申请成功，开始运行
                                Pcb.resultQueue.add("run-"+now.getProcess_id()+"-"+count);
                                //运行完毕，释放资源
                                i=1;
                                while(i+1<s.length-1){
                                    resource.V(s[i],Integer.parseInt(s[i+1]));
                                    pcbs.notifyNext(s[i],resource);
                                    i+=2;
                                }
                            }else{
                                Pcb.resultQueue.add("resourceV-"+now.getProcess_id()+"-"+count+"-"+s[i]+"-"+s[i+1]);
                                //资源申请成功，开始运行
                                Pcb.resultQueue.add("run-"+now.getProcess_id()+"-"+count);
                                i=1;
                                while(i+1<s.length-1){
                                    resource.V(s[i],Integer.parseInt(s[i+1]));
                                    pcbs.notifyNext(s[i],resource);
                                    i+=2;
                                }
                            }

                        }


                    }

                }
                break;
        }

        Result result = new Result(Pcb.resultQueue, null);
        result.setPrinting("processStart");
        return result;
    }

    @Override
    public Result shutdown(Command cmd) throws Exception {
        ISerializableCache.getInstance().commit();
        return new Result("shutdown!",null);
    }
}
