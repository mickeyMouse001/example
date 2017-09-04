package com.scd.blockingQueue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.activation.DataSource;

/**
 * jdbc(此处用连接池会比较理想，但此demo重点不在这里。)
 * @author mickeyMouse001
 * @data 2017-09-04
 */
public class DSConnection {

	private static final String DBDRIVER = "org.gjt.mm.mysql.Driver"; // 定义MySQL的数据库驱动程序
	private static final String DBURL = "jdbc:mysql://localhost:3306/prawns_db?characterSetResults=UTF-8&zeroDateTimeBehavior=convertToNull"; // 定义MySQL数据库的连接地址
	private static final String DBUSER = "root";
	private static final String DBPASS = "root";
	public Connection con;
	
	public DSConnection() {
		try {
			Class.forName(DBDRIVER);
			this.con=DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void batchPS() throws SQLException{
		String sql="INSERT INTO test (data) VALUES (?)";
		int[] i=null;
		PreparedStatement ps=con.prepareStatement(sql);
		try {
			con.setAutoCommit(false);
			for(int k=0;k<100;k++){
				ps.setString(1, "str"+k);
				ps.addBatch();
				
			}
			i=ps.executeBatch();
			con.commit();
			for(int t:i){
			System.out.println(t);
			}
			
		} catch (SQLException e) {
			try {
				con.rollback();
				con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		DSConnection dsc=new DSConnection();
		try {
			dsc.batchPS();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
