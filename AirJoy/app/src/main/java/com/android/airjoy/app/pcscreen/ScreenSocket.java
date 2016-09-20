package com.android.airjoy.app.pcscreen;

import com.addbean.autils.utils.ALog;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * ��Ļsocket�ࣻ
 *
 * @author �ֶ�
 */
public class ScreenSocket {
    private String ip;
    private int port;
    private Socket socket = null;
    DataOutputStream out = null;
    DataInputStream getDownloadStream = null;

    public ScreenSocket(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    public void createConnection() {
        try {
            closeSocket();
            socket = new Socket(ip, port);
            ALog.e("成功建立连接："+ip+":"+port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            closeSocket();
        } catch (IOException e) {
            e.printStackTrace();
            closeSocket();
        } finally {
        }
    }

    public void closeSocket(){
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
    public Socket getSocket() {
        return socket;
    }


    public void sendFileInf() {
        try {
            if(socket==null)createConnection();
            out = new DataOutputStream(socket.getOutputStream());
            byte[] buffer = "SYNPCVIEW|jiadou|".getBytes();
            out.write(buffer);
            ALog.e("发送同步信息："+"SYNPCVIEW|jiadou|");
            return;
        } catch (IOException e) {
            e.printStackTrace();
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /* ��ȡ�����ļ���*/
    public DataInputStream getDownloadStream() {
        try {
            getDownloadStream = new DataInputStream(new BufferedInputStream(
                    socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            if (getDownloadStream != null) {
                try {
                    getDownloadStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return getDownloadStream;
    }

    /* �رյ�ǰ����*/
    public void shutDownConnection() {
        try {
            if (out != null) {
                out.close();
            }
            if (getDownloadStream != null) {
                getDownloadStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ���ֽ������ȡͼƬ��С */
    public static int getLengthFromByte(byte[] lb) {
        String s = new String(lb);
        int imgLength = 0;
        try {
            imgLength = Integer.valueOf(s).intValue();
        } catch (Exception error) {
            return 0;
        }
        return imgLength;
    }
}
