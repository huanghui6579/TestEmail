package com.example.testemail.service;

import java.io.IOException;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.example.testemail.model.MailAccount;
import com.example.testemail.util.MailServerUtil;
import com.example.testemail.util.ReceiveMailUtil;
import com.example.testemail.util.ThreadPool;

public class ReceiveMailService extends Service {
	private Context mContext;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		mContext = this;
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		final MailAccount mailAccount = intent.getParcelableExtra("mailAccount");
		final int pageNum = intent.getIntExtra("pageNum", 1);
		final int pageSize = intent.getIntExtra("pageSize", MailServerUtil.PAGE_SIZE);
		ThreadPool.getCachedPool().execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					ReceiveMailUtil.getInstance(mContext).receive(mailAccount, pageNum, pageSize);
				} catch (MessagingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		return super.onStartCommand(intent, flags, startId);
	}

}
