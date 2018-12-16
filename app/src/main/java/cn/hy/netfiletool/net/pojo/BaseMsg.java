package cn.hy.netfiletool.net.pojo;

import java.io.Serializable;

public class BaseMsg<T> implements Serializable {
    public int err;//是否报错
    public T msg;//信息
}