package com.xj.iws.server.receive;

import com.xj.iws.http.dao.redis.RedisBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

@Component
public class SerialServer {

    @Autowired
    RedisBase redisBase;

    public String IP;
    public int PORT;

    private static boolean close;

    private static Map<String, SerialThread> threadMap = new HashMap<>();
    private static Map<String, Socket> clientMap = new HashMap<>();
    private static Map<String, List<Command>> commandMap = new HashMap<>();
    private static Map<String, Timer> readerMap = new HashMap<>();

//    private ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(10);

    public SerialServer() {
    }

    public void setParam(String IP, int PORT) {
        this.IP = IP;
        this.PORT = PORT;
    }

    public static Socket getClient(String port) {
        return clientMap.get(port);
    }

    public static void setCommand(String port, List<Command> commands) {
        SerialServer.commandMap.put(port, commands);
    }

    public static void removeCommand(String port) {
        SerialServer.commandMap.remove(port);
    }

    public static void removeThread(String port) {
        SerialServer.threadMap.remove(port);
    }

    public static Map<String, Timer> getReader() {
        return readerMap;
    }

    public static void setReader(String port, Timer timer) {
        SerialServer.readerMap.put(port, timer);
    }

    public static void removeReader(String port) {
        SerialServer.readerMap.remove(port);
    }

    @SuppressWarnings("resource")
    public void init() {
        close = true;
        try {
            final ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("服务器启动......");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (close) {

                        Socket client = null;
                        try {
                            //获取客户端连接
                            client = serverSocket.accept();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //获取客户端端口IP
                        String port = client.getInetAddress().toString();
                        port = port.substring(1, port.length());
                        //写入客户端管理器
                        clientMap.put(port, client);

                        System.out.println(port + "#" + client.getPort() + " 已连接");
                    }
                }
            }).start();

        } catch (Exception e) {
            System.out.println("服务器异常: " + e.getMessage());
        }
    }

    public String manual(String port, Command command) {
        Socket client = clientMap.get(port);
        if (null == client || null == command) return null;
        return new SerialThread().manual(client, command);
    }

    public void startPort(String port) {

        //获取命令符
        List<Command> commands = commandMap.get(port);
        if (commands == null) return;

        //若该线程已存在,则终止此线程
        if (null != threadMap.get(port)) {
            //关闭线程内的IO流
            threadMap.get(port).close();
        }

        //创建线程并启动
        SerialThread thread = new SerialThread(port, commands, redisBase);
        new Thread(thread).start();

        //写入线程管理器
        threadMap.put(port, thread);
    }

    public void closePort(String port) {

        if (null != threadMap.get(port)) {
            //关闭线程内的IO流
            threadMap.get(port).close();
            threadMap.remove(port);
        }
        if (null != readerMap.get(port)) {
            //关闭读取
            readerMap.get(port).cancel();
            readerMap.remove(port);
        }
        if (null != commandMap.get(port)) {
            commandMap.remove(port);
        }
    }

    public void close() {
        SerialServer.close = false;
    }
}