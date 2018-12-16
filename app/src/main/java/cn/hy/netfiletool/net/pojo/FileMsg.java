package cn.hy.netfiletool.net.pojo;

import java.io.Serializable;

public class FileMsg implements Serializable {
    public String name;//文件名
    public String path;//路径
    public long size;//大小
    public boolean isDir;//是否是目录
}