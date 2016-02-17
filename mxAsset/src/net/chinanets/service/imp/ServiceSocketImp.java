package net.chinanets.service.imp;

import net.chinanets.service.ServiceSocket;
import java.io.BufferedReader;   
import java.io.IOException;   
import java.io.InputStreamReader;   
import java.io.PrintWriter;   
import java.net.ServerSocket;   
import java.net.Socket;   
import java.util.ArrayList;   
import java.util.List;   
import java.util.concurrent.ExecutorService;   
import java.util.concurrent.Executors;   



public class ServiceSocketImp extends CommonServiceImp implements ServiceSocket{

    private static final int PORT = 8888; // 端口号   
    private static List<Socket> list = new ArrayList<Socket>(); // 保存连接对象   
    private ExecutorService exec;   
    private ServerSocket server;  
    
	public void startSocket(){
        try {   
            server = new ServerSocket(PORT);   
            exec = Executors.newCachedThreadPool();   
            Socket client = null;   
            while (true) {   
                client = server.accept(); // 接收客户连接   
                list.add(client);   
                exec.execute(new ChatTask(client));   
            }   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   
	}
	 
    static class ChatTask implements Runnable {   
    	
        private Socket socket;   
        private BufferedReader br;   
        private PrintWriter pw;   
        private String msg;   
  
        public ChatTask(Socket socket) throws IOException {   
            this.socket = socket;   
            br = new BufferedReader(new InputStreamReader(socket   
                    .getInputStream()));    
        }   
  
        public void run() {   
        	try {   
                while ((msg = br.readLine()) != null) {   
                    if (msg.trim().equals("bye")) {   
                        list.remove(socket);   
                        br.close();   
                        pw.close();   
                        socket.close();   
                        break;   
                    } else {   
                       System.out.println("        "+msg); 
                    }   
                }   
            } catch (IOException e) {   
                e.printStackTrace();   
            }   
        }   
    }   
}
