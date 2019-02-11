package cn.hy.netfiletool.net.pojo;

import java.io.Serializable;

public class FileMsg implements Serializable {
    public String name;//文件名
    public String path;//路径
    public long size;//大小
    public boolean isDir;//是否是目录

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }
}