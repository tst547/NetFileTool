package cn.hy.netfiletool.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.hy.netfiletool.dao.base.MyDataBase;

public class DaoSupport extends MyDataBase {

	protected SQLiteDatabase db;

	protected long error = -1;

	public DaoSupport(Context context, String name, int version) {
		super(context, name, version);
		this.db = super.getWritableDatabase();
	}


	protected boolean insert(String tableName,ContentValues values){
		if(error==db.insert(tableName, null, values))
			return false;
		else
			return true;
	}
	
	protected boolean update(String tableName,ContentValues values,String condition,String []args){
		if(error==db.update(tableName, values, condition, args))
			return false;
		else
			return true;
	}
	
	protected boolean delete(String tableName,String condition,String []args){
		if(error==db.delete(tableName, condition, args))
			return false;
		else
			return true;
	}

	protected Cursor selectAll(String tableName, String[] columns){
		return db.query(true
				, tableName
				, columns
				,null
				,null
				,null
				,null
				,null
				,null);
	}

}
