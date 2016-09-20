package com.android.airjoy.core.service.tcp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import android.util.Log;

/** 
 * udp server 
 *  
 * @author �ֶ�
 * @date 2011-2-20 ����05:05:55 
 */  
public class UdpServer implements Runnable {  
    private int port;  
    DatagramSocket dSocket;
    public UdpServer(int port) {  
        this.port = port;  
        try
        {
            dSocket=new DatagramSocket(this.port);
        }
        catch (SocketException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }  
    public void run() {  
        try{
            
            while (true) {// ���Ͻ������Կͻ��˵�����  
                byte[] buff = new byte[101];// �ļ���Ȳ�����50  
                DatagramPacket dataPacket = new DatagramPacket(buff, buff.length);  
                dSocket.receive(dataPacket);// �ȴ�������Կͻ��˵���ݰ�  
                String recv=new String(dataPacket.getData());
                if(recv!=null){//����Ϊ����
                    doWork(recv);
                }
            }  
                
        }catch(Exception e){
            e.printStackTrace();
        }
    }  
  public void doWork(String recv){
      
      Log.v("recv", recv);
  }
}  
