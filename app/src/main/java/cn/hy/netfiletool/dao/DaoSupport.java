package cn.hy.netfiletool.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class DaoSupport{

	protected SQLiteDatabase db;
	
	protected long error = -1;
	
	public DaoSupport(){
		this(null);
	}
	
	protected DaoSupport(SQLiteDatabase db) {
		this.db = db;
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
	
	
}
