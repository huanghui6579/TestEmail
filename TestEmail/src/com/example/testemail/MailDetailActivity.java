package com.example.testemail;

import com.example.testemail.model.Mail;
import com.example.testemail.util.StringUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MailDetailActivity extends Activity {
	private TextView tvSubject;
	private TextView tvSendDate;
	private TextView tvFromPerson;
	private TextView tvMailReceiver;
	private TextView tvMailCC;	//抄送人
	private TextView tvMailBCC;	//密送人
	private ImageView ivAttFlag;	
	private WebView wbContent;
	
	private LinearLayout linearReceiver;
	private LinearLayout linearCC;
	private LinearLayout linearBCC;
	
	private Mail mail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail_detail);

		mail = getIntent().getParcelableExtra("mail");
		initView();
		initData(mail);
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
		linearCC = (LinearLayout) findViewById(R.id.linear_cc);
		linearBCC = (LinearLayout) findViewById(R.id.linear_bcc);
		linearReceiver = (LinearLayout) findViewById(R.id.linear_receiver);
		
		WebSettings settings = wbContent.getSettings();
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		settings.setDefaultTextEncodingName("UTF-8");
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
			
			String receiver = mail.getReceiveAddress();
			if(StringUtil.isEmpty(receiver)) {
				linearReceiver.setVisibility(View.GONE);
			} else {
				tvMailReceiver.setText(receiver);
			}
			
			String ccAddress = mail.getCcAddress();
			if(StringUtil.isEmpty(ccAddress)) {
				linearCC.setVisibility(View.GONE);
			} else {
				tvMailCC.setText(ccAddress);
			}
			
			String bccAddress = mail.getBccAddress();
			if(StringUtil.isEmpty(bccAddress)) {
				linearBCC.setVisibility(View.GONE);
			} else {
				tvMailBCC.setText(bccAddress);
			}
			if(mail.isContainerAttachment()) {
				ivAttFlag.setVisibility(View.VISIBLE);
			} else {
				ivAttFlag.setVisibility(View.GONE);
			}
			
			wbContent.loadDataWithBaseURL("", mail.getContent(), "text/html", "UTF-8", "");
		}
	}
}
