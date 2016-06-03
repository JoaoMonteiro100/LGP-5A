package com.lgp5.fw.controllers;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadInterface implements Runnable{
	private updateInterface updateInterface;
	BlockingQueue queue = new ArrayBlockingQueue<Double[][]>(100);
	public String delta = "0.0";
	public String theta= "0.0";
	public String alpha1= "0.0";
	public String alpha2= "0.0";
	public String beta1= "0.0";
	public String beta2= "0.0";
	public String gamma1= "0.0";
	public String gamma2= "0.0";
	public String attention= "0.0";
	public String meditation= "0.0";
	public String signal= "0.0";
	
	public ThreadInterface(BlockingQueue queue,updateInterface updateInterface) {
		this.queue=queue;
		this.updateInterface=updateInterface;
	}

	@Override
	public void run() {
		try {
			while(true)
				if(queue.peek() !=null){
					if(updateInterface != null) {
						updateInterface.update((Double[][]) queue.take());						
					}
				}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
