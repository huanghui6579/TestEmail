package com.example.testemail.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.testemail.model.Mail;
import com.example.testemail.util.DbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class MailDao {
	private Context context;
	private DbHelper dbHelper;
	
	public MailDao(Context context) {
		dbHelper = new DbHelper(context);
	}
	
	public void add(Mail mail) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			ContentValues values = new ContentValues();
			values.put("mailNumber", mail.getMailNumber());
			values.put("mailSize", mail.getMailSize());
			values.put("subject", mail.getSubject());
			values.put("from", mail.getFrom());
			values.put("receiveAddress", mail.getReceiveAddress());
			values.put("sendDate", mail.getSendDate());
			values.put("priority", mail.getPriority());
			values.put("content", mail.getContent());
			values.put("emailAddress", mail.getEmailAddress());
			values.put("isSeen", mail.isSeen() ? 1 : 0);
			values.put("isReplySign", mail.isReplySign() ? 1 : 0);
			values.put("isContainerAttachment", mail.isContainerAttachment() ? 1 : 0);
			db.replace("t_mail", null, values);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
	}
	
	public List<Mail> getMailsWithPage(int pageNum, int pageSize) {
		/*
		 * sql = "create table t_mail ("
				+ "mailNumber integer, mailSize integer, subject text, from text, receiveAddress text, sendDate text, "
				+ "priority text, content text, emailAddress text, isSeen integer default 0, isReplySign integer default 0, "
				+ "isContainerAttachment integer default 0, primary key(mailNumber, emailAddress)";
		 */
		String sql = "select * from t_mail order by sendDate desc limi ?, ?";
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<Mail> list = null;
		Cursor cursor = null;
		try {
			list = new ArrayList<Mail>();
			String[] args = {String.valueOf(pageNum), String.valueOf(pageSize)};
			cursor = db.rawQuery(sql, args);
			while(cursor.moveToNext()) {
				Mail mail = new Mail();
				mail.setMailNumber(cursor.getInt(cursor.getColumnIndex("mailNumber")));
				mail.setMailSize(cursor.getLong(cursor.getColumnIndex("mailSize")));
				mail.setSubject(cursor.getString(cursor.getColumnIndex("subject")));
				mail.setFrom(cursor.getString(cursor.getColumnIndex("from")));
				mail.setReceiveAddress(cursor.getString(cursor.getColumnIndex("receiveAddress")));
				mail.setSendDate(cursor.getString(cursor.getColumnIndex("sendDate")));
				mail.setPriority(cursor.getString(cursor.getColumnIndex("priority")));
				mail.setContent(cursor.getString(cursor.getColumnIndex("content")));
				mail.setEmailAddress(cursor.getString(cursor.getColumnIndex("emailAddress")));
				mail.setSeen(cursor.getInt(cursor.getColumnIndex("isSeen")) == 1 ? true : false);
				mail.setReplySign(cursor.getInt(cursor.getColumnIndex("isReplySign")) == 1 ? true : false);
				mail.setContainerAttachment(cursor.getInt(cursor.getColumnIndex("isContainerAttachment")) == 1 ? true : false);
				list.add(mail);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(cursor != null) {
				cursor.close();
			}
			db.close();
		}
		return list;
	}
	
}
