package com.jeff.fischman.gc;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Task implements  Runnable{
    private static final Logger _log = Logger.getLogger(Task.class);

    private int _taskNum;
    volatile MutableBoolean _runFlag;
    int _maxQueueSize;
    private int _maxPermSize;
    private int _msDelay;
    Queue<Buffer> _queue;
    List<Buffer> _permList;

    public Task(int taskNum,
                MutableBoolean runFlag,
                int maxQueueSize,
                int maxPermSize,
                int msDelay)
    {
        _taskNum = taskNum;
        _runFlag = runFlag;
        _maxQueueSize = maxQueueSize;
        _maxPermSize = maxPermSize;
        _msDelay = msDelay;
        _queue = new LinkedList<>();
        _permList = new ArrayList<>();
    }

    public void run() {
        boolean permFilled = false;
        for (int i=0; _runFlag.getValue(); i++) {
            sleep(_msDelay);
            if (_queue.size() > _maxQueueSize) {
                Buffer removed = _queue.remove();
                int removedValue = removed.getValue();
                if (removedValue< 100 ||  removedValue %100  == 0) {
                    _log.info(String.format("task %d removing buffer = %d", _taskNum, removedValue));
                }
                if (_permList.size() < _maxPermSize) {
                    _permList.add(removed);
                } else if (!permFilled) {
                    permFilled = true;
                    _log.info(String.format("task %d finished filling permList, size = %d", _taskNum, _permList.size()));
                }
            }
            Buffer newBuff = new Buffer(i);
            if (i< 100 ||  i %100  == 0) {
                _log.info(String.format("task %d added buffer = %d", _taskNum, newBuff.getValue()));
            }
            _queue.add(newBuff);
        }
        _log.info(String.format("task %d terminating", _taskNum));

    }

    public void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignore) {
        }
    }
}
