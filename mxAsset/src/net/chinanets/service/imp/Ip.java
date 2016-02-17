package net.chinanets.service.imp;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.util.HashMap;

public class Ip {
	public HashMap map; // ping 后的结果集

	public HashMap getMap() { // 用来得到ping后的结果集
		return map;
	}

	// 当前线程的数量, 防止过多线程摧毁电脑
	static int threadCount = 0;

	public Ip() {
		map = new HashMap();
	}

	public void Ping(String ip) throws Exception {
		// 最多30个线程
		while (threadCount > 30)
			Thread.sleep(50);
		    threadCount += 1;
		    PingIp p = new PingIp(ip);
		    p.start();
	}

	public void PingAll() throws Exception {
		//首先得到本机的IP，得到网段 
		InetAddress host = InetAddress.getLocalHost(); 
		String hostAddress = host.getHostAddress(); 
		int k=0; 
		k=hostAddress.lastIndexOf("."); 
		String ss = hostAddress.substring(0,k+1); 
		for(int i=1;i <=255;i++){  //对所有局域网Ip 
			String iip=ss+i; 
			Ping(iip); 
		} 
		//等着所有Ping结束 
		while(threadCount>0) 
		Thread.sleep(50); 
	}

	// 内部类
	class PingIp extends Thread {
		public String ip; // IP

		public PingIp(String ip) {
			this.ip = ip;
		}

		public void run() {
			try {
				Process p = Runtime.getRuntime().exec("ping " + ip );
				InputStreamReader ir = new InputStreamReader(p.getInputStream());
				LineNumberReader input = new LineNumberReader(ir);
				// 读取结果行
				for (int i = 1; i < 7; i++) {
					input.readLine();
					String line = input.readLine();
					if (line.length() < 17 || line.substring(8, 17).equals("timed out")) {
					    //map.put(ip, "false");
					} else {
						map.put(ip, "true");
					}
					threadCount -= 1;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
