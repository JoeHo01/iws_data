package com.xj.iws.server.receive;

import com.xj.iws.common.util.ByteUtil;
import com.xj.iws.http.dao.redis.RedisBase;
import com.xj.iws.server.data.DataFormat;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class SerialThread implements Runnable {

    RedisBase redisBase;

    private Socket socket;//socket客户端连接
    private String port;//端口地址
    private List<Command> commands;//命令类, 存有端口下全部的指令
    String data;//数据存取变量
    private boolean read;//读取控制开关
    private boolean open;//线程控制开关
    private Date readTime;//管理通讯异常时间

    private final long shutdownTime = 10;//发生异常关闭时间,单位 秒

    InputStream in = null;
    DataInputStream dis = null;
    OutputStream out = null;
    DataOutputStream dos = null;

    public SerialThread() {
    }

    public SerialThread(String port, List<Command> commands, RedisBase redisBase) {
        this.port = port;
        this.commands = commands;
        this.redisBase = redisBase;
        this.readTime = null;
    }

    @Override
    public void run() {
        open = true;
        while (open) {
            auto();
        }
    }

    /**
     * 获取socket连接, 建立IO流
     *
     * @return
     */
    private boolean connection() {
        Socket oldSocket = socket;
        Socket newSocket = SerialServer.getClient(port);
        try {
            if (newSocket == null || newSocket.isClosed()) {
                if (socket != null) {
                    socket.close();
                    socket = null;
                }
//                System.out.println(port + " Client is closed");
                return false;
            } else if (newSocket.equals(oldSocket)) {
                return true;
            } else {
                socket = newSocket;
                socket.setSoTimeout(500);
                socket.setKeepAlive(true);
                in = socket.getInputStream();
                dis = new DataInputStream(in);
                out = socket.getOutputStream();
                dos = new DataOutputStream(out);
                return true;
            }
        } catch (SocketException e) {
//            e.printStackTrace();
            return false;
        } catch (IOException e) {
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * 自动读取
     */
    private void auto() {
        if (!connection()) return;
        Command command;
        String code;
        long startTime;
        long runTime;

        //轮询Command
        command:
        for (int i = 0; i < commands.size(); i++) {
            if (socket == null) break;
            command = commands.get(i);
            code = command.getCode();
            data = new String();
            read = true;
            // 发送
            if (!sendOrder(socket, code)) continue command;
            //若设定时间内取不到值,则跳过此指令
            startTime = System.currentTimeMillis();
            read:
            while (read) {
                runTime = System.currentTimeMillis();
                if (runTime - startTime > 500) {
                    System.out.println("读取超时");
                    //异常处理
                    readTimeout(new Date(), false);
                    read = false;
                    break;
                }
                // 读取
                if (!readData(socket, command, code)) {
                    continue command;
                }
            }
            //设定下发指令休眠
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //数据写入redis缓存
            write(command, data);
        }
    }

    /**
     * 手动读写
     *
     * @param socket
     * @param command
     * @return
     */
    public String manual(Socket socket, Command command) {
        data = new String();

        String code = command.getCode();
        read = true;
        sendOrder(socket, code);

        long startTime = System.currentTimeMillis();
        while (read) {
            long runTime = System.currentTimeMillis();
            if (runTime - startTime > 500) {
                read = false;
                break;
            }
            // 读取
            readData(socket, command, code);
        }
        String result = null;
        if (!"".equals(data)) result = data;
        return result;
    }

    /**
     * 下发指令
     *
     * @param socket
     * @param code
     */
    private boolean sendOrder(Socket socket, String code) {
        // 向客户端回复信息
        String checkCode = CRC16.checkCode(code);
        byte[] order = ByteUtil.hexStr2Byte(code + checkCode);

        try {
            dos.write(order);
            dos.flush();
//            System.out.println(socket.getInetAddress() + "#" + socket.getPort() + "  下发:  " + code + checkCode);
            return true;
        } catch (IOException e) {
            if (null != socket) System.out.println(socket.getInetAddress() + "#" + socket.getPort() + "下发异常");
            try {
                Thread.sleep(50);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return false;
//            e.printStackTrace();
        }
    }

    /**
     * 读取数据
     *
     * @param socket
     * @param command
     */
    private boolean readData(Socket socket, Command command, String code) {
        // 确定返回值的字节数
        int byteCount = Integer.parseInt(code.substring(8, 12), 16) * 2 + 5;

        byte[] dataArray = new byte[byteCount];
        try {
            dis.readFully(dataArray);

            //格式化
            String data = ByteUtil.byteArrayToHexString(dataArray, false, 0);

            //数据校验
            boolean flag = CRC16.checkout(data);

            if (flag) {
                System.out.println(port + "#" + command.getNumber() + " 返回:  " + data);
                System.out.println(Thread.activeCount());

                setData(command, data);
                readTimeout(null, true);
                this.read = false;
            }
            return true;
        } catch (IOException e) {
            System.out.println(socket.getInetAddress() + "#" + socket.getPort() + " 读取异常");
            readTimeout(new Date(), false);
            return false;
//            e.printStackTrace();
        }
    }

    /**
     * 关闭线程
     */
    public void close() {
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        open = false;
        SerialServer.removeThread(port);
    }

    /**
     * @param command
     * @param data
     */
    private void setData(Command command, String data) {
        //
        //数据截掉前6位属性位 和后4位校验位
        //type == 1 表示单片机数据(4位1数), 其他类型与单片机相同
        //type == 2 表示电仪表数据(8位1数), 需将前后两数倒置
        //
        String temp = DataFormat.preData(data, 6, 4);
        switch (command.getType()) {
            case 1:
                this.data = temp;
                break;
            case 2:
                String[] arrayData = DataFormat.subData(temp, 8);
                List<Integer> target = command.getTarget();
                StringBuffer strBuf = new StringBuffer();
                for (int i : target) {
                    String front = arrayData[i].substring(4, 8);
                    String back = arrayData[i].substring(0, 4);
                    strBuf.append(front + back);
                }
                this.data = strBuf.toString();
                break;
            default:
                this.data = temp;
                break;
        }
    }

    private void write(Command command, String data) {
        int length;
        //
        //type == 1 表示单片机数据(4位1数), 其他类型与单片机相同
        //type == 2 表示电仪表数据(8位1数)
        //
        switch (command.getType()) {
            case 1:
                length = command.getCount() * 4;
                break;
            case 2:
                length = command.getCount() * 8;
                break;
            default:
                length = command.getCount() * 4;
                break;
        }
        if (data.length() == length) {
            redisBase.valueOps().set("temp_" + port + "#" + command.getNumber(), data);
        }
    }

    /**
     * @param date   异常出现时间
     * @param status true读取正常,刷新时间;false出现异常
     */
    private void readTimeout(Date date, boolean status) {
        //刷新readTime
        if (status) {
            readTime = null;
            return;
        }
        //判定超时
        if (readTime == null) {
            readTime = date;
        } else {
            if ((date.getTime() - readTime.getTime()) / 1000 > shutdownTime) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
                System.out.println("Shutdown "+port);
            }
        }
    }
}
