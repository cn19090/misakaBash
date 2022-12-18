package data;

import bean.Command;
import data.databaseImpl.commandQueueImpl.CommandQueueImpl;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/12/11 19:38
 */
public interface ICommandQueue {
    public static ICommandQueue getInstance(){
        return CommandQueueImpl.getImpl();
    }
    public void add(Command command);
    public Command getAndDel();
    public Command getHead();
    public int size();
}
