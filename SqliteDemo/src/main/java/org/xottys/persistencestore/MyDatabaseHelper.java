/**
 *
 */
package org.xottys.persistencestore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;


public class MyDatabaseHelper extends SQLiteOpenHelper
{
	final String CREATE_TABLE_SQL =
		"create table student(_id integer primary " +
		"key autoincrement,name,age,class)";
	public MyDatabaseHelper(Context context, String name, int version)
	{
		super(context, name, null, version);
	}
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		//第一次使用数据库时自动建表
		db.execSQL(CREATE_TABLE_SQL);
		Log.i(TAG, "onCreate: ");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db
		, int oldVersion, int newVersion)
	{
		System.out.println("--------onUpdate Called--------"
			+ oldVersion + "--->" + newVersion);
	}
}
