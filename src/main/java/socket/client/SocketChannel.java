//package socket.client;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.net.SocketException;
//
///**
// * @author: wangruirui
// * @date: 2017/6/6
// * @description:
// */
//public class SocketChannel {
//
//    // 连接Socket
//    public Socket m_Socket = null;
//    private int m_TimeOut = 0;
//    private static byte[] m_byBuff = new byte[256];	// Recieved data buffer
//
//    public SocketChannel() {
//    }
//
//    public SocketChannel(int v_iTimeOut) {
//        m_TimeOut = v_iTimeOut;
//    }
//
//    // 连接状态
//    public boolean isConnected{
//            if (m_Socket == null)
//                return false;
//            else
//                return m_Socket.;
//
//    }
//
//
//    // 连接主机，内部封装了代理
//    // v_sHost  主机地址
//    // v_iPort主机端口
//    public boolean connect(String v_sHost, int v_iPort) {
//        closeSocket();
//        m_Socket = null;
//        try {
//            m_Socket = new Socket(v_sHost, v_iPort);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (m_Socket != null) {
//            if (m_TimeOut > 0)
//                setScoketTimeout(m_TimeOut);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    /// <summary>
//    /// 设置socket接收操时
//    /// </summary>
//    /// <param name="v_iTimeout">操时时间（毫秒）</param>
//    private void setScoketTimeout(int v_iTimeout) {
//        try {
//            m_Socket.setSoTimeout(v_iTimeout);
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//
//    }
//    // 发送符合黄金交易二级系统接口规范的报文
//    public void sendGoldMsg(String v_sMsg, int encryptType)
//    {
//        if (v_sMsg == null || v_sMsg.length() <= 0 || m_Socket == null)
//            return;
//        //byte[] sSrcMsg = CommUtil.ConvertBytes(v_sMsg);
//        //byte[] bSendMsg = TripleDes.encryptMsg(encryptType, sSrcMsg);
//
//        //通过printWriter 来向服务器发送消息
//        PrintWriter printWriter = null;
//        try {
//            printWriter = new PrintWriter(m_Socket.getOutputStream());
//
//        System.out.println("连接已建立...");
//
//        //发送消息
//        printWriter.println(bSendMsg);
//
//        printWriter.flush();
//
//        //InputStreamReader是低层和高层串流之间的桥梁
//        //client.getInputStream()从Socket取得输入串流
//        InputStreamReader streamReader = new InputStreamReader(m_Socket.getInputStream());
//
//        //链接数据串流，建立BufferedReader来读取，将BufferReader链接到InputStreamReder
//        BufferedReader reader = new BufferedReader(streamReader);
//        String advice = reader.readLine();
//        System.out.println("接收到服务器的消息 ："+advice);
//        reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // 根据网络通讯配置，连接到交易服务器
//    public boolean connectTransServer(String host, String port)
//    {
//        if (connect(host, Integer.parseInt(port))) {
//            return true;
//        }
//        return false;
//    }
//
//
//    /// Socket连接请求设置超时时间
//    /// <param name="remoteEndPoint">网络端点</param>
//    /// <param name="timeoutMSec">超时时间</param>
//    public boolean connect(IPEndPoint remoteEndPoint, int timeoutMSec)
//    {
//        TimeoutObject.Reset();
//        m_Socket.BeginConnect(remoteEndPoint, CallBackMethod, m_Socket);
//        //阻塞当前线程
//        if (TimeoutObject.WaitOne(timeoutMSec, true)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    private void callBackMethod(IAsyncResult asyncresult)
//    {
//        //使阻塞的线程继续
//        Socket socket = asyncresult.AsyncState as Socket;
//        if (socket.Connected) {
//            socket.EndConnect(asyncresult);
//        }
//        TimeoutObject.Set();
//    }
//
//
//    /// 无代理连接远程主机
//    /// <param name="v_sHost">主机的域名或IP地址</param>
//    /// <param name="v_iPort">主机开放的端口</param>
//    private Socket ConnectByNoProxy(string v_sHost, int v_iPort) {
//        return DirectConnect(v_sHost, v_iPort);
//    }
//
//    /// 堵塞方式接收符合黄金交易二级系统接口规范的报文
//    public String RecvGoldMsg() {
//        if (m_Socket == null)
//            return "";
//
//        byte[] bLens = RecvByLen(Constant.MSG_LEN);
//
//        int len = Int32.Parse(CommUtil.ConvertString(bLens));
//        byte[] bKeys = RecvByLen(1);
//        byte[] bCcieKey = RecvByLen(4);
//        byte[] bSessionID = RecvByLen(10);
//
//        byte[] arrSession = CommUtil.ConvertBytes(CommUtil.Fill(Constant.SESSION_KEYS, ' ', Constant.SESSION_KEY_DEFAULT_Len, 'R'));
//        byte[] arrDefault = CommUtil.ConvertBytes(CommUtil.Fill(Constant.SESSION_KEY_DEFAULT, ' ', Constant.SESSION_KEY_DEFAULT_Len, 'R'));
//        byte[] arrIv = CommUtil.ConvertBytes(Constant.IV_DEFAULT);
//
//
//        byte[] bMsgs = RecvByLen(len - 1 - 4 - 10);
//        int iType = Int32.Parse(CommUtil.ConvertString(bKeys));
//        string sRecvMsg = "";
//        switch (iType)
//        {
//            //不加密，原报文返回
//            case 0:
//                sRecvMsg = CommUtil.ConvertString(bMsgs);
//                break;
//            //RSA算法加密
//            case 1:
//                sRecvMsg = TripleDes.RSADecrypt(Constant.CCIEPATH, bMsgs);
//                break;
//            //3DES加密（会话密钥）
//            case 2:
//                bMsgs = TripleDes.decrypt(arrSession, arrIv, bMsgs);
//                sRecvMsg = CommUtil.ConvertString(bMsgs);
//                break;
//            //3DES加密（默认密钥）
//            case 3:
//                bMsgs = TripleDes.decrypt(arrDefault, arrIv, bMsgs);
//                sRecvMsg = CommUtil.ConvertString(bMsgs);
//                break;
//
//            //ZIP压缩
//            case 4:
//                bMsgs = ZipUtil.unzip(bMsgs);
//                sRecvMsg = CommUtil.ConvertString(bMsgs);
//                break;
//            //先ZIP压缩后再3DES加密(会话密钥)
//            case 5:
//                bMsgs = TripleDes.decrypt(arrSession, arrIv, bMsgs);
//                bMsgs = ZipUtil.unzip(bMsgs);
//                sRecvMsg = CommUtil.ConvertString(bMsgs);
//                break;
//            //先ZIP压缩后再3DES加密(默认密密钥)
//            case 6:
//                bMsgs = TripleDes.decrypt(arrDefault, arrIv, bMsgs);
//                bMsgs = ZipUtil.unzip(bMsgs);
//                sRecvMsg = CommUtil.ConvertString(bMsgs);
//                break;
//        }
//
//        return sRecvMsg;
//    }
//
//    /// <summary>
//    /// 解压读取到的报文（没有通讯头）
//    /// </summary>
//    /// <param name="vReadBytes"></param>
//    /// <returns></returns>
//    private byte[] unzipReadBytes(byte[] vReadBytes)
//    {
//        if (vReadBytes.Length > 1
//                && vReadBytes[0] == 0x01)
//        {
//            byte[] zipBuff = new byte[vReadBytes.Length - 1];
//            for (int i = 1; i < vReadBytes.Length; i++)
//            {
//                zipBuff[i - 1] = vReadBytes[i];
//            }
//
//            byte[] unzipBuff = ZipUtil.unzip(zipBuff);
//            byte[] tmp = new byte[unzipBuff.Length - Constant.MSG_LEN];
//            for (int i = Constant.MSG_LEN; i < unzipBuff.Length; i++)
//                tmp[i - Constant.MSG_LEN] = unzipBuff[i];
//            return tmp;
//        }
//        else
//        {
//            return vReadBytes;
//        }
//    }
//
//    /// <summary>
//    /// 接收固定长度的数据
//    /// </summary>
//    /// <param name="v_iRecvLen"></param>
//    /// <returns></returns>
//    private byte[] RecvByLen(int v_iRecvLen)
//    {
//        byte[] recvBuff = new byte[v_iRecvLen];
//        int iRecvIndex = 0;
//
//        while (iRecvIndex < v_iRecvLen)
//        {
//            int iCanReadLen = v_iRecvLen - iRecvIndex;
//            if (iCanReadLen > 1024)
//                iCanReadLen = 1024;
//
//            byte[] tempBuff = new byte[iCanReadLen];
//
//            try
//            {
//                int iLen = m_Socket.Receive(tempBuff, iCanReadLen, SocketFlags.None);
//                if (iLen > 0)
//                {
//                    for (int i = 0; i < iLen; i++)
//                    {
//                        recvBuff[iRecvIndex + i] = tempBuff[i];
//                    }
//                    iRecvIndex += iLen;
//                }
//                else if (iLen == 0)
//                {
//                    throw new Exception("连接被远程主机强制关闭！");
//                }
//                else
//                {
//                    Thread.Sleep(10);
//                }
//            }
//            catch (Exception e)
//            {
//                throw new Exception(e.Message);
//            }
//        }
//
//        return recvBuff;
//    }
//
//
//    // 关闭Socket
//    public void closeSocket() {
//        try {
//            if (m_Socket != null)
//                m_Socket.close();
//            m_Socket = null;
//        } catch (Exception e) {
//        }
//    }
//
//}
//
