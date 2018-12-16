package cn.hy.netfiletool.net;

/**
 * 网络配置信息
 */
public class NetWorkInfo {

    private long ip;//自身ip地址
    private int scanPort;//广播扫描端口
    private long broadcastAddr;// 广播地址
    private long gateWay;// 网关IP
    private long netmask;//子网掩码


    public long getIp() {
        return ip;
    }

    public void setIp(long ip) {
        this.ip = ip;
    }

    public int getScanPort() {
        return scanPort;
    }

    public void setScanPort(int scanPort) {
        this.scanPort = scanPort;
    }

    public long getBroadcastAddr() {
        return broadcastAddr;
    }

    public void setBroadcastAddr(long netmask, long hostIp) {
        this.broadcastAddr = ~netmask|hostIp;
    }

    public long getGateWay() {
        return gateWay;
    }

    public void setGateWay(long gateWay) {
        this.gateWay = gateWay;
    }

    public long getNetmask() {
        return netmask;
    }

    public void setNetmask(long netmask) {
        this.netmask = netmask;
    }

}
