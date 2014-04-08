package com.example.testemail.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.testemail.model.Attachment;
import com.example.testemail.model.Mail;
import com.example.testemail.util.DbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AttachmentDao {
	private Context context;
	private DbHelper dbHelper;
	
	public AttachmentDao(Context context) {
		dbHelper = new DbHelper(context);
	}
	
	public void add(Attachment att) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (!isAttEsists(db, att)) {
			db.beginTransaction();
			try {
				ContentValues values = new ContentValues();
				values.put("fileSize", att.getFileSize());
				values.put("emailNumber", att.getEmailNumber());
				values.put("emailAddress", att.getEmailAddress());
				values.put("fileName", att.getFileName());
				db.replace("t_attachment", null, values);
				db.setTransactionSuccessful();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
			}
		}
	}
	
	public boolean isAttEsists(SQLiteDatabase db, Attachment att) {
		String sql = "select count(_id) from t_attachment where emailAddress = ? and emailNumber = ? and fileName = ?";
		String[] args = {att.getEmailAddress(), String.valueOf(att.getEmailNumber()), att.getFileName()};
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, args);
			cursor.moveToFirst();
			int count = cursor.getInt(0);
			if(count > 0) {	//已经存在
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null) {
				cursor.close();
			}
		}
		return false;
	}
	
	public List<Attachment> getAttachments(Mail mail) {
		/*
		 * sql = "insert into t_attachment (fileSize, emailNumber, emailAddress, fileName) values (?, ?, ?, ?)";
		 */
		String sql = "select * from t_attachment where emailAddress = ? and emailNumber = ?";
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<Attachment> list = null;
		Cursor cursor = null;
		try {
			list = new ArrayList<Attachment>();
			String[] args = {mail.getEmailAddress(), String.valueOf(mail.getMailNumber())};
			cursor = db.rawQuery(sql, args);
			while(cursor.moveToNext()) {
				Attachment att = new Attachment();
				att.setFileSize(cursor.getLong(cursor.getColumnIndex("fileSize")));
				att.setEmailNumber(cursor.getInt(cursor.getColumnIndex("emailNumber")));
				att.setEmailAddress(cursor.getString(cursor.getColumnIndex("emailAddress")));
				att.setFileName(cursor.getString(cursor.getColumnIndex("fileName")));
				list.add(att);
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
