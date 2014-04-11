package com.example.testemail.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "mail.db";
	private static final int DB_VERSION = 1;

	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table t_mail_account ("
				+ "_id integer primary key autoincrement, username text, password text, emailAddress text, "
				+ "mailType text, resId integer, unReadCount integer default 0)";
		db.execSQL(sql);
		
		/*
		 * 	mailNumber;	//邮件序号                  
		 	mailSize;	//邮件大小                  
			subject;	//主题                    
			fromAddress;	//发件人  
			ccAddress;
			bccAddress            
			sendDate;	//发送日期              
			priority;	//邮件优先级             
			content;	//邮件正文                  
			emailAddress;   
			isSeen;	//是否已读                  
			isReplySign;	//是否需要回执        
			isContainerAttachment;	//是否包含附件
		 */
		sql = "create table t_mail ("
				+ "mailNumber integer, mailSize integer, subject text, fromAddress text, fromName text, receiveAddress text, ccAddress text, bccAddress text, sendDate text, "
				+ "priority text, content text, emailAddress text, isSeen integer default 0, isReplySign integer default 0, "
				+ "isContainerAttachment integer default 0, primary key(mailNumber, emailAddress))";
		db.execSQL(sql);
		
		/*
		 * 	emailAddress
			emailNumber;   
			fileName;   
			fileSize;     
		 */
		sql = "create table t_attachment (_id integer primary key autoincrement, fileSize integer, emailNumber integer, emailAddress text, fileName text)";
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "drop table if exists t_mail_account";
		db.execSQL(sql);
		
		sql = "drop table if exists t_mail";
		db.execSQL(sql);
		
		sql = "drop table if exists t_attachment";
		db.execSQL(sql);
		
		onCreate(db);
	}

}
