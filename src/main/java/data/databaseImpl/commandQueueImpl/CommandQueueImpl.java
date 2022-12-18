package data.databaseImpl.commandQueueImpl;

import bean.Command;
import data.ICommandQueue;

import java.util.LinkedList;

/**
 * @author 齐鲁工业大学_数据科学20-2_夏增荃
 * @version 1.0
 * @date 2022/12/11 19:39
 */
public class CommandQueueImpl implements ICommandQueue {
    private CommandQueueImpl(){}
    private static final CommandQueueImpl impl=new CommandQueueImpl();
    private LinkedList<Command> queue=new LinkedList<>();

    @Override
    public synchronized void add(Command command) {
        queue.add(command);
    }

    @Override
    public synchronized Command getAndDel() {
        Command first = queue.getFirst();
        queue.remove(first);
        return first;
    }

    @Override
    public synchronized Command getHead() {
        return queue.getFirst();
    }

    @Override
    public synchronized int size() {
        return queue.size();
    }

    public static CommandQueueImpl getImpl(){
        return impl;
    }
}
