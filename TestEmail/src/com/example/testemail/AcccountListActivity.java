package com.example.testemail;

import java.util.ArrayList;
import java.util.List;

import com.example.testemail.model.MailAccount;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AcccountListActivity extends Activity {
	private ListView lvAccounts;
	private List<MailAccount> accounts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_list);
		
		lvAccounts = (ListView) findViewById(R.id.lv_account);
		
		accounts = new ArrayList<MailAccount>();
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
			String count = String.format(getString(R.string.email_count), 100);
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
