package cn.hy.netfiletool.dao.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库sqlite交互类
 * 
 * @author hanyu
 * 
 */
public class MyDataBase extends SQLiteOpenHelper {
	
	private static SQLiteDatabase base;

	// 建表语句
	public static final String CREATE_TABLE_TASK = "create table host_info("
			+ "host_ip varchar(30) primary key, " + "host_port varchar(30),"
			+ "type varchar(30))";

	public static final String CREATE_TABLE_FILES = "create table files("
			+ "id bigint," + "name text," + "path text," + "isdir integer,"
			+ "length integer," + "current integer)";

	public MyDataBase(Context context, String name, int version) {
		super(context, name, null, version);
	}

	/**
	 * 创建数据库的方法，如果数据库已存在则该方法不会执行
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_TASK);
		db.execSQL(CREATE_TABLE_FILES);
		Log.d("create database", "success");
	}

	/**
	 * 数据库升级的方法
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 获取可读写的数据库实例
	 */
	@Override
	public SQLiteDatabase getWritableDatabase() {
		if(null==MyDataBase.base)
			MyDataBase.base = super.getWritableDatabase();
		return MyDataBase.base;
	}

}
