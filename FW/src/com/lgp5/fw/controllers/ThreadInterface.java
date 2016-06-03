package com.lgp5.fw.controllers;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadInterface implements Runnable{
	private updateInterface updateInterface;
	BlockingQueue queue = new ArrayBlockingQueue<Double[][]>(100);
	BlockingQueue queue2 = new ArrayBlockingQueue<Double[]>(100);
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

	public ThreadInterface(BlockingQueue queue,BlockingQueue queue2,updateInterface updateInterface) {
		this.queue=queue;
		this.queue2=queue2;
		this.updateInterface=updateInterface;
	}

	@Override
	public void run() {
		try {
			int i=0;
			while(true)				
			{				
				if(queue.peek() !=null){
					if(updateInterface != null) {
						updateInterface.update((Double[][]) queue.take());						
					}
				}
				if(queue2.peek() !=null){
					if(updateInterface != null) {
						if(i==1000){
							updateInterface.update2((Double[]) queue2.take());
							i=0;
						}
						else
						{
							 queue2.take();
						}
						i++;
					}
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
