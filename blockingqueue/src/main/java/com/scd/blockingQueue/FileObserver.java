package com.scd.blockingQueue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

/**
 * 订阅者
 * @author mickeyMouse001
 * @data 2017-09-04
 */
public class FileObserver implements Observer{

	private File file;
	private File objFile=new File("d://tempData1.txt");
	DSConnection dsc=new DSConnection();
	public FileObserver(File file,FileLisence f) {
		super();
		this.file = file;
		f.addObserver(this);
	}

	public void update(Observable o, Object arg) {
//		System.out.println("文件更新了");
//		从文件读出数据并写入数据库
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Scanner sc = new Scanner(file);
	    	PrintWriter out=new PrintWriter(new FileOutputStream(objFile, true)); 
	    	String sql="INSERT INTO test (data) VALUES (?)";
			PreparedStatement ps=dsc.con.prepareStatement(sql);
			dsc.con.setAutoCommit(false);
			int i=0;
			long startTime=System.currentTimeMillis();
	    	while(sc.hasNext()){
	    		i++;
				String s=sc.next();
				ps.setString(1, s);
				ps.addBatch();
				if(i%1000==0){
					ps.executeBatch();
					dsc.con.commit();
					ps.clearBatch();
				}
	    	}
	    	ps.executeBatch();
			dsc.con.commit();
	    	ps.close();
			System.out.println("插入数据库完成");
			System.out.println("批处理10000条数据的时间： "+(System.currentTimeMillis()-startTime));
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				dsc.con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	    	}
	}

