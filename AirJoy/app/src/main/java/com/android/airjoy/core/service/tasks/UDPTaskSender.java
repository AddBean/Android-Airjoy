package com.android.airjoy.core.service.tasks;

import com.addbean.autils.utils.ALog;
import com.android.airjoy.constant.Config;
import com.android.airjoy.core.service.core.TaskBase;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by AddBean on 2016/3/27.
 */
public class UDPTaskSender extends TaskBase {
    public UDPTaskSender(String taskName, String cmd) {
        super(taskName, cmd);
    }

    @Override
    public void RunTask() {
        sendString((String) getmData());
    }

    public void sendString(String message) {
        ALog.e(message);
        message = (message == null ? "message is null" : message);
        int server_port = Config.Port;
        DatagramSocket s = null;
        try {
            s = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        InetAddress local = null;
        try {
            local = InetAddress.getByName(Config.Ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        int msg_length = message.length();
        byte[] messageByte = message.getBytes();
        DatagramPacket p = new DatagramPacket(messageByte, msg_length,
                local, server_port);
        try {
            s.send(p);
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
