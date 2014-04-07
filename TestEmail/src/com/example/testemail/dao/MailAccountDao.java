package com.example.testemail.dao;

import java.util.ArrayList;
import java.util.List;

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
		String sql = "insert into t_mail_account (username, password, emailAddress, mailType, resId, unReadCount) values (?, ?, ?, ?, ?, ?)";
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			Object[] args = {account.getUsername(), account.getPassword(), account.getEmailAddress(), account.getMailType(), account.getResId(), account.getUnReadCount()};
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
		String sql = "delete from t_mail_account where emailAddress = ?";
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
		String sql = "update t_mail_account set password = ?, unReadCount = ?";
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			String[] args = {account.getPassword(), String.valueOf(account.getUnReadCount())};
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
		String sql = "select username, password, mailType, resId, unReadCount from t_mail_account where emailAddress = ?";
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
				ma.setResId(cursor.getInt(cursor.getColumnIndex("resId")));
				ma.setUnReadCount(cursor.getInt(cursor.getColumnIndex("unReadCount")));
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
	
	public List<MailAccount> listAccount() {
		String sql = "select username, password, emailAddress, mailType, resId, unReadCount from t_mail_account";
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<MailAccount> accounts = null;
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			accounts = new ArrayList<MailAccount>();
			while(cursor.moveToNext()) {
				MailAccount account = new MailAccount();
				account.setUsername(cursor.getString(cursor.getColumnIndex("username")));
				account.setPassword(cursor.getString(cursor.getColumnIndex("password")));
				account.setMailType(cursor.getString(cursor.getColumnIndex("mailType")));
				account.setEmailAddress(cursor.getString(cursor.getColumnIndex("emailAddress")));
				account.setResId(cursor.getInt(cursor.getColumnIndex("resId")));
				account.setUnReadCount(cursor.getInt(cursor.getColumnIndex("unReadCount")));
				accounts.add(account);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null) {
				cursor.close();
			}
			db.close();
		}
		return accounts;
	}
	
	public boolean hasMailAccount() {
		String sql = "select count(_id) from t_mail_account";
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			int count = 0;
			if(cursor.moveToFirst()) {
				count = cursor.getInt(0);
			}
			if(count > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null) {
				cursor.close();
			}
			db.close();
		}
		return false;
	}
}
