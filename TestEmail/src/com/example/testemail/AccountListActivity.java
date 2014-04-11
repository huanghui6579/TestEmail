package com.example.testemail;

import java.util.ArrayList;
import java.util.List;

import com.example.testemail.dao.MailAccountDao;
import com.example.testemail.model.MailAccount;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AccountListActivity extends Activity {
	private ListView lvAccounts;
	private List<MailAccount> accounts;
	private Context mContext;
	
	private EmailAccountAdapter adapter;
	
	MailAccountDao accountDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		mContext = this;
		
		accounts = new ArrayList<MailAccount>();
		
		accountDao = new MailAccountDao(mContext);
		
		lvAccounts = (ListView) findViewById(R.id.listview);
		
		adapter = new EmailAccountAdapter(mContext, accounts);
		lvAccounts.setAdapter(adapter);
		
		lvAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MailAccount account = accounts.get(position);
				Intent intent = new Intent(mContext, MailListActivity.class);
				intent.putExtra("mailAccount", account);
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onResume() {
		initData();
		super.onResume();
	}
	
	private void initData() {
		if(!accounts.isEmpty()) {
			accounts.clear();
		}
		List<MailAccount> temp = accountDao.listAccount();
		if(temp != null && temp.size() > 0) {
			accounts.addAll(temp);
		}
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			Intent intent = new Intent(mContext, MainActivity.class);
			intent.putExtra(MainActivity.ACTION_FLAG, MainActivity.ACTION_FLAG_ADD);
			startActivity(intent);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class EmailAccountAdapter extends BaseAdapter {
		private Context context;
		private List<MailAccount> list;

		public EmailAccountAdapter(Context context, List<MailAccount> list) {
			super();
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.item_account, null);
				
				holder.ivEmailIcon = (ImageView) convertView.findViewById(R.id.iv_email_icon);
				holder.tvEmailAddress = (TextView) convertView.findViewById(R.id.tv_email_address);
				holder.tvEmailCount = (TextView) convertView.findViewById(R.id.tv_email_count);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			MailAccount account = list.get(position);
			holder.ivEmailIcon.setImageResource(account.getResId());
			holder.tvEmailAddress.setText(account.getEmailAddress());
			String count = String.format(getString(R.string.email_count), account.getUnReadCount());
			holder.tvEmailCount.setText(count);
					
			return convertView;
		}
		
	}
	
	final class ViewHolder {
		ImageView ivEmailIcon;
		TextView tvEmailAddress;
		TextView tvEmailCount;
	}
}
