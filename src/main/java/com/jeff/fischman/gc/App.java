package com.jeff.fischman.gc;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final Logger _log = Logger.getLogger(App.class);

    volatile MutableBoolean _runFlag;

    public static void main(String[] args )
    {

        System.out.println( "Hello World!" );
        MutableBoolean runFlag = new MutableBoolean(true);
        Scanner scanner = new Scanner(System.in);
        _log.info("Hit enter to quit");

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(new Task(1, runFlag, 10, 10, 10));
        scanner.nextLine();

        _log.info("Setting runFlag to false");

        runFlag.setFalse();
        _log.info("Shutting down executorService");
        executorService.shutdown();
        boolean isDown = false;
        try {
            isDown = executorService.awaitTermination(5, TimeUnit.SECONDS);
            _log.info(String.format("ExecutorService isDown = %s", isDown));
        } catch (InterruptedException e) {
            _log.error(String.format("Caught exception from executorService.awaitTermination: %s", e.getMessage()));
        }
    }

}
