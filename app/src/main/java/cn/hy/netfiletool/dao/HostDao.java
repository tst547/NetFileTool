package cn.hy.netfiletool.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import cn.hy.netfiletool.common.WifiUtil;
import cn.hy.netfiletool.net.HostInfo;

import java.util.LinkedHashMap;
import java.util.Map;

public class HostDao {

    private DaoSupport daoSupport;

    private final String tableName = "host_info";

    private final String[] columns = {column.HostIp,column.HostPort,column.Type};

    private interface column{
        String HostIp = "host_ip";
        String HostPort = "host_port";
        String Type = "type";
    }

    public HostDao(Context context,String dbName,int version){
        super();
        daoSupport = new DaoSupport(context,dbName,version);
    }

    public void add(HostInfo hostInfo){
        ContentValues values = new ContentValues();
        values.put(column.HostIp,WifiUtil.long2ip(hostInfo.getHostIp()));
        values.put(column.HostPort,hostInfo.getHostPort());
        values.put(column.Type,hostInfo.getType());
        daoSupport.insert(tableName,values);
    }

    public Map<String,HostInfo> selectAll(){
        Map<String,HostInfo> results = new LinkedHashMap<>();
        Cursor cursor = daoSupport.selectAll(tableName,columns);
        while (cursor.moveToNext()){
            HostInfo hostInfo = new HostInfo();
            int hostIpColumnIndex = cursor.getColumnIndex(column.HostIp);
            String hostIp = cursor.getString(hostIpColumnIndex);
            hostInfo.setHostIp(WifiUtil.ip2long(hostIp));

            int hostPortColumnIndex = cursor.getColumnIndex(column.HostPort);
            String hostPort = cursor.getString(hostPortColumnIndex);
            hostInfo.setHostPort(Integer.parseInt(hostPort));

            int typePortColumnIndex = cursor.getColumnIndex(column.Type);
            String type = cursor.getString(typePortColumnIndex);
            hostInfo.setType(type);

            results.put(hostIp,hostInfo);
        }
        return results;
    }
}
