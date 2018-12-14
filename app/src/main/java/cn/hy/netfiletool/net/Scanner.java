package cn.hy.netfiletool.net;

import cn.hy.netfiletool.common.MyGson;
import cn.hy.netfiletool.common.WifiUtil;
import cn.hy.netfiletool.pojo.BaseMsg;
import cn.hy.netfiletool.pojo.IpMsg;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by hanyu on 2017/11/15 0015.
 */
public class Scanner {

    private NetWorkInfo netWorkInfo;

    public Scanner(NetWorkInfo netWorkInfo) {
        this.netWorkInfo = netWorkInfo;
    }

    public void start(){
        //实例化一个DatagramSocket对象用来接收主机发来的消息，消息内包含主机开放服务的端口号
        //消息的地址即为主机地址
        new Thread(()->{
            try {
                DatagramSocket socket = new DatagramSocket(netWorkInfo.getScanPort());
                while (true){
                    byte[] buf = new byte[2048];
                    DatagramPacket recv = new DatagramPacket(buf, buf.length);
                    socket.receive(recv);
                    BaseMsg<IpMsg> baseMsg = (BaseMsg<IpMsg>) MyGson.getObject(
                            new String(recv.getData(), 0, recv.getLength()).trim(),
                            new TypeToken<BaseMsg<IpMsg>>() {
                            }.getType());
                    HostInfo hostInfo = new HostInfo();
                    hostInfo.setHostIp(Long.valueOf(WifiUtil.ipToInt(recv.getAddress().getHostAddress())));
                    hostInfo.setHostPort(baseMsg.msg.port);
                    netWorkInfo.getHostIp().add(hostInfo);
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    /**
     * 广播地址查询主机
     *
     * @return
     * @throws IOException
     */
    public void conn() {
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(2500);
            socket.setBroadcast(true);
            byte[] tempBuf = new byte[1];
            tempBuf[0] = 0x01;
            InetAddress addr = InetAddress.getByName(WifiUtil.long2ip(netWorkInfo.getBroadcastAddr()));
            DatagramPacket packet = new DatagramPacket(tempBuf, tempBuf.length, addr, netWorkInfo.getScanPort());
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 1-254段扫描
     *
     * @return
     * @throws Exception
     */
    public void scan() {
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(2500);
            socket.setBroadcast(true);
            socket.setSoTimeout(100);
            //根据子网掩码 和 主机ip地址 计算出 网络号
            int network = netWorkInfo.getNetmask() & netWorkInfo.getIp();
            byte[] buf = new byte[1];
            buf[0] = 0x01;
            //子网掩码取反值后 二进制格式的长度n 则2^n是最大主机数(幂) -2则是可用数量
            int total = (int) Math.pow(2,Integer.toBinaryString((~netWorkInfo.getNetmask())).length())-2;

            for (int i = 0; i < total; i++) {
                //从网络号开始 有total数量的主机数
                String host = WifiUtil.intToIp(network+i);
                if (netWorkInfo.getBroadcastAddr()==network+i||network+i==netWorkInfo.getIp())
                    continue;//如果是广播地址 or 自身ip则跳过
                InetAddress addr = InetAddress.getByName(host);
                DatagramPacket tempPacket = new DatagramPacket(buf, buf.length, addr, netWorkInfo.getScanPort());
                socket.send(tempPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
