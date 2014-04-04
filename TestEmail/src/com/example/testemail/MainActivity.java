package com.example.testemail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import com.example.testemail.model.MailAccount;
import com.example.testemail.util.MailServerUtil;
import com.example.testemail.util.ThreadPool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	
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
				Store store = (Store) msg.obj;
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
		setContentView(R.layout.activity_main);
		
		etAddress = (AutoCompleteTextView) findViewById(R.id.et_emaill_address);
		etPassword = (EditText) findViewById(R.id.et_password);
		btnAdd = (Button) findViewById(R.id.btn_add);
		
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
				Store store = null;
				Message msg = handler.obtainMessage();
				try {
					store = login(account);
					pDialog.dismiss();
					if(store != null) {	//登录成功
						msg.obj = store;
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
	
	private Store login(MailAccount account) throws MessagingException {
		Session session = MailServerUtil.validateAccount(account);
		if(session == null) {
			return null;
		}
		Store store = session.getStore();
		store.connect();
		if(!store.isConnected()) {
			store = null;
		}
		return store;
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
