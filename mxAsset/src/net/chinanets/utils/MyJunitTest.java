package net.chinanets.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import net.chinanets.pojos.Users;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URI;
import java.net.UnknownHostException;


public class MyJunitTest extends  TestCase{

	public void testF(){

		try {
			//Socket socket = new Socket("www.qq.com",80);
			URL  url = new URL("http://www.qq.com");
			InputStream in=url.openConnection().getInputStream();
			FileOutputStream out=new FileOutputStream("c:\\qq.html");
			byte[] byt = new byte[1024];
			int len=0;
			while((len=in.read(byt))!= -1){
				out.write(byt, 0, len);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		/*
		StringBuilder builder=new StringBuilder();
		builder.append("张山"+",");	
		builder.append("null "+",");	
		builder.append("张山"+",");	
		builder.append("null"+",");	
		builder.append("张山"+",");	
		String s=builder.toString();
		System.out.println(s);
		System.out.println(s.replaceAll("null", "哈哈"));
		
		Calendar c = Calendar.getInstance();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		String s=fmt.format(c.getTime());
		System.out.println(s); 
	    Boolean flag=DbUtils.loadDriver("oracle.jdbc.driver.OracleDriver");
	    Connection conn=null;
	    if(flag){
			try {
				conn= DriverManager.getConnection(
						"jdbc:oracle:thin:@localhost:1521:orcl","zcgl",
						"zcgl");
				QueryRunner query = new QueryRunner();
				String sql="select * from users";
				ResultSetHandler rsh = new BeanListHandler(Users.class);
				List<Users> item=(List<Users>) query.query(conn, sql, rsh);
			    for(Users u : item){
			    	System.out.println(u.getMc());
			    }
				DbUtils.closeQuietly(conn);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	    */
	}
	
	
}
