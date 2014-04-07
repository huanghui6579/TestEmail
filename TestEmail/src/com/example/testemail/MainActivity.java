package com.example.testemail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import com.example.testemail.dao.MailAccountDao;
import com.example.testemail.model.MailAccount;
import com.example.testemail.service.ReceiveMailService;
import com.example.testemail.util.MailServerUtil;
import com.example.testemail.util.ThreadPool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private Context mContext;

	public static final String ACTION_FLAG = "action_flag";
	public static final int ACTION_FLAG_LIST = 100;
	public static final int ACTION_FLAG_ADD = 101;
	
	private AutoCompleteTextView etAddress;
	private EditText etPassword;
	private Button btnAdd;
	private ArrayAdapter<String> adapter;
	private static String[] maileTypes = {
		"126.com", "163.com", "sina.com", "yahoo.com", "hotmail.com", "gmail.com", "qq.com", "sohu.com"
	};
	
	private String emailAddress;
	private String password;
	
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:	//登录成功
				makeShortToast("登录成功");
				Intent intent = new Intent(mContext, AcccountListActivity.class);
				startActivity(intent);
				finish();
				break;
			case -1:
				String error = msg.getData().getString("error");
				makeShortToast(error);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MailAccountDao accountDao = new MailAccountDao(this);
		Intent intent = getIntent();
		int flag = intent.getIntExtra(ACTION_FLAG, ACTION_FLAG_LIST);
		if(flag == ACTION_FLAG_LIST && accountDao.hasMailAccount()) {
			intent = new Intent(this, AcccountListActivity.class);
			startActivity(intent);
			finish();
		} else {
			setContentView(R.layout.activity_main);
			
			etAddress = (AutoCompleteTextView) findViewById(R.id.et_emaill_address);
			etPassword = (EditText) findViewById(R.id.et_password);
			btnAdd = (Button) findViewById(R.id.btn_add);
			
			mContext = this;
			
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
			etAddress.setAdapter(adapter);
			
			etAddress.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(s != null && s.length() > 0) {
						StringBuilder sb = new StringBuilder(s);
						if(sb.indexOf("@") == -1) {	//还没有输入“@”
							sb.append("@");
							adapter.clear();
							for(String type : maileTypes) {
								sb.append(type);
								adapter.add(sb.toString());
								sb.delete(sb.length() - type.length(), sb.length());
							}
							adapter.notifyDataSetChanged();
						}
					}
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					String pwd = etPassword.getText().toString();
					String address = s.toString();
					if(pwd == null || "".equals(pwd)) {
						btnAdd.setEnabled(false);
					} else if(address == null || "".equals(address)) {
						btnAdd.setEnabled(false);
					} else if(!validateEmailAddress(address)){
						btnAdd.setEnabled(false);
					} else {
						btnAdd.setEnabled(true);
					}
					emailAddress = address;
				}
			});
			
			etPassword.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					String pwd = s.toString();
					String address = etAddress.getText().toString();
					if(pwd == null || "".equals(pwd)) {
						btnAdd.setEnabled(false);
					} else if(address == null || "".equals(address)) {
						btnAdd.setEnabled(false);
					} else {
						btnAdd.setEnabled(true);
					}
					password = pwd;
				}
			});
		}
		
	}
	
	private boolean validateEmailAddress(String address) {
		String emailreg = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(emailreg);
		Matcher matcher = pattern.matcher(address);
		return matcher.matches();
	}
	
	private void makeShortToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	
	public void add(View view) {
		final ProgressDialog pDialog = ProgressDialog.show(this, null, "正在验证，请稍后...");
		ThreadPool.getCachedPool().execute(new Runnable() {
			
			@Override
			public void run() {
				MailAccount account = new MailAccount();
				account.setEmailAddress(emailAddress);
				account.setPassword(password);
				account.setUsername(emailAddress);
				account.setResId(MailServerUtil.getResId(account.getMailType()));
				Store store = null;
				Message msg = handler.obtainMessage();
				try {
					store = MailServerUtil.login(account);
					pDialog.dismiss();
					if(store != null) {	//登录成功
						MailAccountDao accountDao = new MailAccountDao(mContext);
						Folder folder = store.getFolder(MailServerUtil.INBOX);
						account.setUnReadCount(folder.getUnreadMessageCount());
						accountDao.add(account);
						Intent service = new Intent(mContext, ReceiveMailService.class);
						service.putExtra("mailAccount", account);
						service.putExtra("pageNum", 1);
						service.putExtra("pageSize", MailServerUtil.PAGE_SIZE);
						startService(service);
						msg.what = 1;
						handler.sendMessage(msg);
					} else {
						msg.getData().putString("error", "登录失败，请重试！");
						msg.what = -1;
						handler.sendMessage(msg);
					}
				} catch (MessagingException e) {
					e.printStackTrace();
					pDialog.dismiss();
					msg.getData().putString("error", "登录失败，请重试！");
					msg.what = -1;
					handler.sendMessage(msg);
				}
				
			}
		});
	}
	
}
