package cn.hy.netfiletool.net;

import android.support.annotation.IntegerRes;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络配置信息
 */
public class NetWorkInfo {

    private int ip;//本地ip地址
    private int scanPort;//扫描消息接收端口
    private int broadcastAddr;// 广播地址
    private List<HostInfo> hostIp = new ArrayList<>();// 主机列表
    private int gateWay;// 网关IP
    private int netmask;//子网掩码

    public int getBroadcastAddr() {
        return broadcastAddr;
    }

    public void setBroadcastAddr(int broadcastAddr) {
        this.broadcastAddr = broadcastAddr;
    }

    public void setBroadcastAddr(int netmask,int hostIp) {
        this.broadcastAddr = (~netmask)|hostIp;
    }

    public int getGateWay() {
        return gateWay;
    }

    public void setGateWay(int gateWay) {
        this.gateWay = gateWay;
    }

    public int getNetmask() {
        return netmask;
    }

    public void setNetmask(int netmask) {
        this.netmask = netmask;
    }

    public int getScanPort() {
        return scanPort;
    }

    public void setScanPort(int scanPort) {
        this.scanPort = scanPort;
    }

    public int getIp() {
        return ip;
    }

    public void setIp(int ip) {
        this.ip = ip;
    }


    public List<HostInfo> getHostIp() {
        return hostIp;
    }

    public void setHostIp(List<HostInfo> hostIp) {
        this.hostIp = hostIp;
    }
}
