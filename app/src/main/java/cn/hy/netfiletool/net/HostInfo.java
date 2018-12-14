package cn.hy.netfiletool.net;

import java.io.Serializable;

public class HostInfo implements Serializable {

    private long hostIp;// 主机IP
    private int hostPort;//  主机服务端口
    private String type;//连接类型信息

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HostInfo(long hostIp,int hostPort){
        this.hostIp = hostIp;
        this.hostPort = hostPort;
    }

    public HostInfo(){
    }
}
