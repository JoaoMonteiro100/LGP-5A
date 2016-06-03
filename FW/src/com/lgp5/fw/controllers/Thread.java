package com.lgp5.fw.controllers;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Thread implements Runnable{
	BlockingQueue queue = new ArrayBlockingQueue<Double[][]>(100);
	public static  Double[][] finalDataArray;
	public static void main(String[] args){
		// TODO Auto-generated method stub

	}
	public Thread(BlockingQueue queue,Double[][] finalDataArray) {
		this.queue=queue;
		this.finalDataArray=finalDataArray;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			if(queue.peek() !=null){
				finalDataArray=(Double[][]) queue.take();
				System.err.println(Arrays.toString(finalDataArray));
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
