package com.lgp5.fw.controllers;

import module.MainModule;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AnalysisInProgressController{
    private AnalysisInterface analysisInterface;
    private MainModule fw;
    private BlockingQueue queue = new ArrayBlockingQueue<Double[][]>(1);


    public AnalysisInProgressController(MainModule fw, BlockingQueue queue){
        this.fw = fw;
        this.queue=queue;
    }
    private void initialize() {
        System.out.println("ola");
        analysisInterface = new AnalysisInterface() {
            @Override
            public void update(Double[][] finalDataArray) {
                System.out.println(finalDataArray.toString());
            }
        };
        ThreadInterface t = new ThreadInterface(queue, analysisInterface);
        Thread thread = new Thread(t);
        thread.setDaemon(true);
        thread.start();

    }


}
