package cn.hy.netfiletool.net;

public class HostInfo {

    private long hostIp;// 主机IP
    private int hostPort;//  主机服务端口

    public long getHostIp() {
        return hostIp;
    }

    public void setHostIp(long hostIp) {
        this.hostIp = hostIp;
    }

    public int getHostPort() {
        return hostPort;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }
}
