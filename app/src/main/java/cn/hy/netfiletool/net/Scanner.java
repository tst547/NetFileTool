package cn.hy.netfiletool.net;

import cn.hy.netfiletool.application.collection.HostData;
import cn.hy.netfiletool.bean.HostInfo;
import cn.hy.netfiletool.common.MyGson;
import cn.hy.netfiletool.common.WifiUtil;
import cn.hy.netfiletool.key.ConstStrings;
import cn.hy.netfiletool.net.pojo.BaseMsg;
import cn.hy.netfiletool.net.pojo.IpMsg;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by temp547 on 2017/11/15 0015.
 */
public class Scanner {

    public void startListener(HostData hostData,int port){
        //实例化一个DatagramSocket对象用来接收主机发来的消息，消息内包含主机开放服务的端口号
        //消息的地址即为主机地址
        new Thread(()->{
            try {
                DatagramSocket socket = new DatagramSocket(port);
                while (true){
                    byte[] buf = new byte[2048];
                    DatagramPacket recv = new DatagramPacket(buf, buf.length);
                    socket.receive(recv);
                    BaseMsg<IpMsg> baseMsg = (BaseMsg<IpMsg>) MyGson.getObject(
                            new String(recv.getData(), 0, recv.getLength()).trim(),
                            new TypeToken<BaseMsg<IpMsg>>() {
                            }.getType());
                    HostInfo hostInfo = new HostInfo();
                    hostInfo.setHostIp(Long.valueOf(WifiUtil.ip2long(recv.getAddress().getHostAddress())));
                    hostInfo.setHostPort(baseMsg.msg.port);
                    hostData.putHost(recv.getAddress().getHostAddress()
                            .concat(ConstStrings.Colon)
                            .concat(String.valueOf(baseMsg.msg.port)),hostInfo);
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
    public void conn(NetWorkInfo netWorkInfo) {
        new Thread(()->{
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
        }).start();
    }

    /**
     * 1-254段扫描
     *
     * @return
     * @throws Exception
     */
    public boolean scan(NetWorkInfo netWorkInfo) {
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(2500);
            socket.setBroadcast(true);
            socket.setSoTimeout(100);

            //根据子网掩码 和 主机ip地址 计算出 网络号
            long network = netWorkInfo.getNetmask() & netWorkInfo.getIp();
            byte[] buf = new byte[1];
            buf[0] = 0x01;
            //子网掩码取反值后 二进制格式的长度n 则2^n是最大主机数(幂) -2则是可用数量
            int total = (int) Math.pow(2,Long.toBinaryString((~netWorkInfo.getNetmask())).length())-2;

            for (int i = 0; i < total; i++) {
                //从网络号开始 有total数量的主机数
                String host = WifiUtil.long2ip(network+i);
                if (netWorkInfo.getBroadcastAddr()==network+i||network+i==netWorkInfo.getIp())
                    continue;//如果是广播地址 or 自身ip则跳过
                InetAddress addr = InetAddress.getByName(host);
                DatagramPacket tempPacket = new DatagramPacket(buf, buf.length, addr, netWorkInfo.getScanPort());
                socket.send(tempPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
