package com.lgp5.fw.controllers;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadInterface implements Runnable {
    private updateInterface updateInterface;
    private SensorInterface sensorInterface;
    private AnalysisInterface analysisInterface;
    BlockingQueue queue = new ArrayBlockingQueue<Double[][]>(100);
    BlockingQueue queue2 = new ArrayBlockingQueue<Double[][]>(100);
    private int device;
    private Boolean sensor;
    private Boolean analysis;

    public ThreadInterface(BlockingQueue queue, SensorInterface t) {
        this.queue = queue;
        sensorInterface = t;
        sensor = true;
        analysis = false;
    }

    public ThreadInterface(BlockingQueue queue, AnalysisInterface t) {
        this.queue = queue;
        analysisInterface = t;
        analysis = true;
        sensor = false;
    }

    public ThreadInterface(BlockingQueue queue, BlockingQueue queue2, updateInterface updateInterface, int device) {
        this.queue = queue;
        this.queue2 = queue2;
        this.updateInterface = updateInterface;
        this.device = device;
        sensor = false;
        analysis = false;
    }

    @Override
    public void run() {
        try {
            int i = 0;
            while (true) {
                if (!sensor&&!analysis) {
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
                } else if (sensor) {
                    if (queue.peek() != null) {
                        if (sensorInterface != null) {
                            sensorInterface.update((Double[][]) queue.take());
                        }
                    }
                } else if (analysis) {
                    if (queue.peek() != null) {
                        if (analysisInterface != null) {
                            analysisInterface.update((Double[][]) queue.take());
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
