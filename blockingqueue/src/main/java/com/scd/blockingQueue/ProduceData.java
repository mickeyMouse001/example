package com.scd.blockingQueue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * 
 * 模拟10000个请求，生产数据（0-100的随机数）
 * @author mickeyMouse001
 * @data 2017-09-04
 */
public class ProduceData {
	private static BlockingQueue<String> queue=new LinkedBlockingQueue<String>(1000);
	private static File file=new File("d://tempData.txt");
	/**
	 * 生产者生产数据
	 */
	static void produceData(){
		long startTime=System.currentTimeMillis();
		Random r=new Random();
		
			for(int i=0;i<10000;i++){
//				if(t==50000)
//					break;
				//0-99随机整数
				int tem=r.nextInt(100);
				String userName="lisa"+(i+1);
				String data=userName+":"+tem;
				//存入消息队列
				try {
					queue.put(data);
				} catch (InterruptedException e) {
					//e.printStackTrace();
					try {
						Thread.sleep(100);
						queue.put(data);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
						System.out.println("用户量过多，请稍后再试");
					}
				}
				System.out.println("恭喜中奖： "+data);
				
				
			}
			System.out.println("响应用户costTime: "+(System.currentTimeMillis()-startTime));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args){

		new Thread(){

			@Override
			public void run() {
				produceData();
			}
			
		}.start();
		
		saveDataToFile();
		FileLisence f=new FileLisence();
		FileObserver fo=new FileObserver(file, f);
		while(true){
			
			f.checkFile(file);
		}
		
	}
	/**
	 * 消费者
	 * 将数据存入文件
	 */
	private static void saveDataToFile() {
		Thread writer=new Thread(){
			long startTime=System.currentTimeMillis();
			boolean isEnable=true;
			@Override
			public void run() {
				
				while(isEnable){
					try{
						if(queue.isEmpty()){
							Thread.sleep(1000);
							if(queue.isEmpty()){
								isEnable=false;
								//所有数据入栈时间
								System.out.println("入栈时间： "+(System.currentTimeMillis()-startTime-1000));
							}
							continue;
						}
						//发现队列中有数据
						PrintWriter out=
							new PrintWriter(
							new FileOutputStream(file, true)); 
						while(! queue.isEmpty()){
//							System.out.println("队列大小： "+queue.size());
							String str=queue.poll();//此处如果用take会自动阻塞
							out.println(str);
						}
						out.close();
//						throw new Exception("故意抛出异常");
					}catch(Exception e){
						e.printStackTrace(); 
					}
					}
				}
			
		};
		writer.start();
	}
}
