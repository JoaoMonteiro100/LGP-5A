package com.lgp5.patient.controllers;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by cenas on 16/06/2016.
 */
public class ThreadInterface implements Runnable{


    private updateInterface updateInterface;
    BlockingQueue queue = new ArrayBlockingQueue<Double[][]>(100);
    BlockingQueue queue2 = new ArrayBlockingQueue<Double[][]>(100);
    private int device;


    public ThreadInterface(BlockingQueue queue, BlockingQueue queue2, updateInterface updateInterface, int device) {
        this.queue = queue;
        this.queue2 = queue2;
        this.updateInterface = updateInterface;
        this.device = device;
    }

    public void run() {
        try {
            int i = 0;
            while (true) {
                if (queue.peek() != null) {
                    if (updateInterface != null) {
                        updateInterface.update((Double[][]) queue.take());
                    }
                }
                if (device == 1) {
                    if (queue2.peek() != null) {
                        if (updateInterface != null) {
                            if (i == 1000) {
                                updateInterface.update2((Double[][]) queue2.take());
                                i = 0;
                            } else {
                                queue2.take();
                            }
                            i++;
                        }
                    }
                } else if (device == 2) {
                    if (queue2.peek() != null) {
                        if (updateInterface != null) {
                            updateInterface.update2((Double[][]) queue2.take());
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

