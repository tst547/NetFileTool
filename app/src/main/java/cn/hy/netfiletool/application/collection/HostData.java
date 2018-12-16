package cn.hy.netfiletool.application.collection;

import cn.hy.netfiletool.bean.HostInfo;
import cn.hy.netfiletool.database.dao.HostDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 储存所有主机信息
 * @author temp547
 */
public class HostData {

    protected HostDao hostDao;

    protected Map<String, HostInfo> dataMap;

    public HostData(HostDao hostDao){
        this.hostDao = hostDao;
        this.dataMap = hostDao.selectAll();
    }

    public synchronized boolean putHost(String key,HostInfo hostInfo){
        if (!dataMap.containsKey(key)){
            dataMap.put(key,hostInfo);
            hostDao.add(hostInfo);
            return true;
        }
        return false;
    }

    public synchronized boolean deleteHost(String key){
        if (!dataMap.containsKey(key)){
            dataMap.remove(key);
            hostDao.delete(key);
            return true;
        }
        return true;
    }

    /**
     * 获取用于遍历的集合
     * 修改集合内元素不会影响到数据
     * @return
     */
    public List<HostInfo> values() {
        List<HostInfo> result = new ArrayList<>();
        for (HostInfo hostInfo:dataMap.values()){
            HostInfo temp = new HostInfo();
            temp.setHostIp(hostInfo.getHostIp());
            temp.setHostPort(hostInfo.getHostPort());
            temp.setType(hostInfo.getType());
            result.add(temp);
        }
        return result;
    }
}
