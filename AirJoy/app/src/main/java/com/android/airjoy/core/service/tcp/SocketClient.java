package com.android.airjoy.core.service.tcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.content.Context;
/**
 * socket�ͻ���
 * 
 * @author �ֶ�
 * 
 */
public class SocketClient
{
    Socket socket = null;
    String buffer = "";
    String geted1;
    String _ipString;
    String _portString;
    Context _context;

    /*
     * ���캯��
     */
    public SocketClient(Context context, String ipString, String portSting)
    {
        this._context = context;
        this._ipString = ipString;
        this._portString = portSting;
    }

    /*
     * �������ӣ�
     */
    public void startConnect()
    {
        new RecvThread().start(); // �����߳� ����������ͺͽ�����Ϣ
    }

    /*
     * ��ʼ��Socket��
     * */
    public void InitSocket()
    {
        /* * * * * * * * * * �ͻ��� Socket ͨ���췽�����ӷ����� * * * * * * * * * */
        try
        {
         // �ͻ��� Socket ����ͨ��ָ�� IP ��ַ���������ַ�ʽ�����ӷ�������,ʵ�����ն���ͨ�� IP ��ַ�����ӷ�����
            // �½�һ��Socket��ָ����IP��ַ���˿ں�
            socket = new Socket(_ipString,Integer.parseInt(_portString));
            // �ͻ���socket�ڽ������ʱ�������ֳ�ʱ��1. ���ӷ�������ʱ�������ӳ�ʱ��2.
            // ���ӷ������ɹ��󣬽��շ�������ݳ�ʱ�������ճ�ʱ
            // ���� socket ��ȡ������ĳ�ʱʱ��
            socket.setSoTimeout(5000);
            // ������ݰ�Ĭ��Ϊ false�����ͻ��˷�����ݲ��� Nagle �㷨��
            // ���Ƕ���ʵʱ�����Ըߵĳ��򣬽������Ϊ true�����ر� Nagle
            // �㷨���ͻ���ÿ����һ����ݣ�������ݰ��С���Ὣ��Щ��ݷ��ͳ�ȥ
            socket.setTcpNoDelay(true);
            // ���ÿͻ��� socket �ر�ʱ��close() ����������ʱ�ӳ� 30 ��رգ���� 30 ���ھ�����δ���͵���ݰ��ͳ�ȥ
            socket.setSoLinger(true, 30);
            // ����������ķ��ͻ������С��Ĭ����4KB����4096�ֽ�
            socket.setSendBufferSize(4096);
            // �����������Ľ��ջ������С��Ĭ����4KB����4096�ֽ�
            socket.setReceiveBufferSize(4096);
            // ���ã�ÿ��һ��ʱ����������Ƿ��ڻ״̬�����������˳�ʱ��û��Ӧ���Զ��رտͻ���socket
            // ��ֹ����������Чʱ���ͻ��˳�ʱ�䴦������״̬
            socket.setKeepAlive(true);
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /*
     * ���͵�����Ϣ����η��������̣߳�
     */
    public void sendSingleMsg(String msg)
    {
        try
        {

            // �ͻ�����������˷�����ݣ���ȡ�ͻ�����������������
            OutputStream osSend = socket.getOutputStream();
            OutputStreamWriter osWrite = new OutputStreamWriter(osSend);
            BufferedWriter bufWrite = new BufferedWriter(osWrite);
            // ������������������˷��͵��ֽ����
            socket.setOOBInline(true);
            // ��ݲ����������������������
            socket.sendUrgentData(0x44);// "D"
            // ���������д��ݣ�д��һ��������
            // ע���˴��ַ��������\r\n\r\n�������߷�����HTTPͷ�Ѿ�������Դ�����ݣ�������������Ķ�ȡ��ݳ�������
            // ��write() �����п��Զ���������̨ƥ����ʶ����Ӧ�Ĺ��ܣ������¼Login()
            // ����������дΪwrite("Login|LeoZheng,0603 \r\n\r\n"),����̨ʶ��;
                bufWrite.write(msg);
                // ���ͻ���������� - ǰ��˵���� flush() ��Ч�������ǵ��õķ������԰ɣ�
                bufWrite.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /*
     * ������Ϣ�̣߳�
     */
    class RecvThread extends Thread
    {
        public RecvThread()
        {
        }
        @Override
        public void run()
        {
            try
            {
                InitSocket();
//                // ���ӷ����� ���������ӳ�ʱΪ5��
//                socket = new Socket();
//                socket.connect(
//                        new InetSocketAddress(_ipString,
//                                              Integer.parseInt(_portString)),
//                        5000);
               // InitSocket();
                // ��ȡ������
                BufferedReader bff = new BufferedReader(
                                                        new InputStreamReader(
                                                                              socket.getInputStream()));
                // ��ȡ������������Ϣ
                String line = null;
                buffer = "";
                while ((line = bff.readLine()) != null)
                {
                    buffer = line + buffer;
                }
                // �رո���������
                bff.close();
                socket.close();
            }
            catch (SocketTimeoutException aa)
            {
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }
}
