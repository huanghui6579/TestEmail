package com.example.testemail.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "mail.db";
	private static final int DB_VERSION = 1;

	public DbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table t_mail ("
				+ "_id integer parmary key autoincrement, username text, password text, emailAddress text, "
				+ "mailType text)";
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "drop table if exists t_mail";
		db.execSQL(sql);
		onCreate(db);
	}

}
