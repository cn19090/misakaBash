package application;

import Err.CommandInputErr;
import Err.User.UserHasLogin;
import Err.User.UserNotFound;
import application.Impl.Actuator;
import bean.Command;
import bean.Result;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/11/17 8:28
 */
public interface IActuator {

    public static IActuator getInstance(){
        return new Actuator();
    }

    public Result  useradd(Command cmd)throws Exception;
    public Result  usermod(Command cmd)throws Exception;
    public Result  userdel(Command cmd)throws Exception;
    public Result  passwd(Command cmd)throws Exception;
    public Result  chmod(Command cmd)throws Exception;
    public Result  chown(Command cmd)throws Exception;
    public Result  groupadd(Command cmd)throws Exception;
    public Result  groupdel(Command cmd)throws Exception;
    public Result  ls(Command cmd)throws Exception;
    public Result  ll(Command cmd)throws Exception;
    public Result  mkdir(Command cmd)throws Exception;
    public Result  touch(Command cmd)throws Exception;
    public Result  rm(Command cmd)throws Exception;
    public Result  cat(Command cmd)throws Exception;
    public Result  grep(Command cmd)throws Exception;
    public Result  nohup(Command cmd)throws Exception;
    public Result  tar(Command cmd)throws Exception;
    public Result  untar(Command cmd)throws Exception;
    public Result  zip(Command cmd)throws Exception;
    public Result  unzip(Command cmd)throws Exception;
    public Result  rar(Command cmd)throws Exception;
    public Result  unrar(Command cmd)throws Exception;
    public Result  netstat(Command cmd)throws Exception;
    public Result  wget(Command cmd)throws Exception;
    public Result  ps(Command cmd)throws Exception;
    public Result  kill(Command cmd)throws Exception;
    public Result  jobs(Command cmd)throws Exception;
    public Result  login(Command cmd)throws Exception;
    public Result  pwd(Command cmd)throws Exception;
    public Result  cd(Command cmd)throws Exception;

    /**
     * 输入格式:resourceDestribute (资源名 数量)+
     * @param cmd
     * @return
     * @throws Exception
     */
    public Result  resourceDestribute(Command cmd)throws Exception;

    /**
     * 输入格式:memoryDestribute 结构 算法 (大小 )+
     * @param cmd
     * @return
     * @throws Exception
     */
    public Result  memoryDestribute(Command cmd)throws Exception;
    public Result  processcreate(Command cmd)throws Exception;
    public Result  processStart(Command cmd)throws Exception;

    public Result  shutdown(Command cmd)throws Exception;
}
