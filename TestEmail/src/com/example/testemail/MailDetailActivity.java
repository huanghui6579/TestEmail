package com.example.testemail;

import com.example.testemail.model.Mail;
import com.example.testemail.util.StringUtil;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class MailDetailActivity extends Activity {
	private TextView tvSubject;
	private TextView tvSendDate;
	private TextView tvFromPerson;
	private TextView tvMailReceiver;
	private TextView tvMailCC;
	private TextView tvMailBCC;	//抄送人
	private ImageView ivAttFlag;	//密送人
	private WebView wbContent;
	
	private Mail mail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail_detail);
		
		initView();
		
		mail = getIntent().getParcelableExtra("mail");
	}
	
	private void initView() {
		tvSubject = (TextView) findViewById(R.id.tv_subject);
		tvSendDate = (TextView) findViewById(R.id.tv_sendDate);
		tvFromPerson = (TextView) findViewById(R.id.tv_fromPerson);
		tvMailReceiver = (TextView) findViewById(R.id.tv_mail_receiver);
		tvMailCC = (TextView) findViewById(R.id.tv_mail_cc);
		tvMailBCC = (TextView) findViewById(R.id.tv_mail_bcc);
		ivAttFlag = (ImageView) findViewById(R.id.iv_att_flag);
		wbContent = (WebView) findViewById(R.id.wb_content);
		
		WebSettings settings = wbContent.getSettings();
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
	}
	
	private void initData(Mail mail) {
		if(mail != null) {
			tvSubject.setText(mail.getSubject());
			tvSendDate.setText(StringUtil.parseDateStr(mail.getSendDate(), null, "yyyy-MM-dd HH:mm"));
			String from = mail.getFromName();
			if(StringUtil.isEmpty(from)) {
				from = mail.getEmailAddress();
			}
			tvFromPerson.setText(from);
			tvMailReceiver.setText(mail.getReceiveAddress());
		}
	}
}
