package com.example.testemail.dao;

import com.example.testemail.model.MailAccount;
import com.example.testemail.util.DbHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class MailAccountDao {
	private Context mContext;
	private DbHelper dbHelper;
	
	public MailAccountDao(Context context) {
		this.mContext = context;
		dbHelper = new DbHelper(context);
	}
	
	/**
	 * 添加邮箱账号
	 */
	public void add(MailAccount account) {
		String sql = "insert into t_mail (username, password, emailAddress, mailType) values (?, ?, ?, ?)";
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			String[] args = {account.getUsername(), account.getPassword(), account.getEmailAddress(), account.getMailType()};
			db.execSQL(sql, args);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
	}
	
	/**
	 * 添加邮箱账号
	 */
	public void delete(String emailAddress) {
		String sql = "delete from t_mail where emailAddress = ?";
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			String[] args = {emailAddress};
			db.execSQL(sql, args);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
	}
	
	/**
	 * 添加邮箱账号
	 */
	public void update(MailAccount account) {
		String sql = "update t_mail set password = ?";
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			String[] args = {account.getPassword()};
			db.execSQL(sql, args);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
	}
	
	/**
	 * 添加邮箱账号
	 */
	public MailAccount getMailAccountByAddress(MailAccount account) {
		String sql = "select username, password, mailType from t_mail where emailAddress = ?";
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = null;
		try {
			String[] args = {account.getEmailAddress()};
			cursor = db.rawQuery(sql, args);
			if(cursor.moveToFirst()) {
				MailAccount ma = new MailAccount();
				ma.setUsername(cursor.getString(cursor.getColumnIndex("username")));
				ma.setPassword(cursor.getString(cursor.getColumnIndex("password")));
				ma.setMailType(cursor.getString(cursor.getColumnIndex("mailType")));
				ma.setEmailAddress(account.getEmailAddress());
				return ma;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(cursor != null) {
				cursor.close();
			}
			db.close();
		}
		return null;
	}
}
