package com.android.airjoy.app.pcfile.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import android.util.Log;

import com.android.airjoy.constant.Config;

/**
 * socket访问pc公共类
 *
 * @author 贾豆
 *
 */
public class SocketClient
{
    /* 连接超时时间*/
    private static final int TIMEOUT_CONNECTION = 1000;

    /* 重新操作次数*/
    private static final int RETRY_TIME = 1;

    /* 获取socket*/
    private static Socket getSocket()
    {
        Socket socket = null;
        try
        {
            socket = new Socket(Config.Ip, Config.Port);
            socket.setKeepAlive(true);
            socket.setSoTimeout(TIMEOUT_CONNECTION);
        }
        catch (UnknownHostException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return socket;
    }

    /* socket连接方法*/
    private static String socketConnect(String method)
    {
        DataInputStream dis = null;
        DataOutputStream dos = null;
        Socket socket = null;
        String result = "";
        int time = 0;
        byte[] buffer1=new byte[1000];
        socket = getSocket();
        do {
            try {
                dos = new DataOutputStream(socket.getOutputStream());
                byte[] buffer= method.getBytes();
                dos.write(buffer);

                dis = new DataInputStream(socket.getInputStream());
                int len = 0;
                while ((len = dis.read(buffer1)) != -1) {
                    result=  result+new String(buffer1,0,len,"UTF-8");
                    Log.v("result", result+"\r\n");
                }

                break;
            } catch (IOException e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(500);
                        continue;
                    } catch (InterruptedException e1) {
                    }
                }
                e.printStackTrace();
            } finally {
                try {
                    if (dis != null) {
                        dis.close();
                    }
                    if (dos != null) {
                        dos.close();
                    }
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } while (time < RETRY_TIME);
        return result;
    }

    /* 获取盘符信息*/
    public static String getDiskInfo()
    {
        return socketConnect("GETDISK|jiadou|");
    }

    /* 获取盘符下的文件信息*/
    public static String getFileInfo(String diskPath)
    {
        return socketConnect("GETFILE|" + diskPath+"|");
    }

    /* 打开文件；*/
    public static void openFile(String diskPath){
        socketConnect("OPENFILE|" + diskPath+"|");
    }
}
