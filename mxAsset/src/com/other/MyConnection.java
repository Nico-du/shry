package com.other;

import java.sql.Connection;
import java.sql.DriverManager;


 
public class MyConnection {

	public static Connection getDAConnection() throws Exception {//192.168.100.210
		   Connection con = null;
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@192.168.124.1:1555:mhora10","tycx",
					"tycx");
			/*	con = DriverManager.getConnection(
					"jdbc:oracle:thin:@192.168.1.235:1521:orcl","zcgl",
					"zcgl");*/
		  return con;
	}
	

	public static void closeConn(Connection con) {
		try {
			con.close();
		} catch (Exception e) {
           e.printStackTrace();
		}
	}
	public static void main(String args[]){
		try {
			Connection conn = getDAConnection();
			if(conn == null){
				System.out.println("cannot get connection .....");
			}else{
				System.out.println("get connection success.....");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("cannot get connection .....");
			e.printStackTrace();
		}
		
	}
}