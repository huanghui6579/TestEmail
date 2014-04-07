package com.example.testemail;

import java.util.ArrayList;
import java.util.List;

import com.example.testemail.dao.MailDao;
import com.example.testemail.model.Mail;
import com.example.testemail.util.MailServerUtil;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

public class MailListActivity extends Activity {
	private Context mContext;
	
	private ListView lvMails;
	private int pageNum = 1;
	
	private List<Mail> mails;
	
	private MailDao mailDao = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		mContext = this;
		
		mailDao = new MailDao(this);
		
		lvMails = (ListView) findViewById(R.id.listview);
		
		mails = new ArrayList<Mail>();
	}
	
	class LoadMailTask extends AsyncTask<Integer, Mail, List<Mail>> {

		@Override
		protected List<Mail> doInBackground(Integer... params) {
			if(params != null && params.length > 0) {
				return mailDao.getMailsWithPage(params[0], MailServerUtil.PAGE_SIZE);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(List<Mail> result) {
			if(result != null && result.size() > 0) {
				mails.addAll(result);
			}
			super.onPostExecute(result);
		}
	}
}
