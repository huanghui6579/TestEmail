package com.example.testemail;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testemail.dao.MailDao;
import com.example.testemail.model.Mail;
import com.example.testemail.model.MailAccount;
import com.example.testemail.service.ReceiveMailService;
import com.example.testemail.util.MailServerUtil;
import com.example.testemail.util.StringUtil;

public class MailListActivity extends Activity {
	private Context mContext;
	
	private ListView lvMails;
	private int pageNum = 1;
	
	private List<Mail> mails;
	
	private MailDao mailDao = null;
	
	private MailAdapter adapter;
	
	private MailAccount mailAccount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		mContext = this;
		
		mailDao = new MailDao(this);
		
		mailAccount = getIntent().getParcelableExtra("mailAccount");
		
		lvMails = (ListView) findViewById(R.id.listview);
		
		mails = new ArrayList<Mail>();
		
		LayoutInflater inflater = getLayoutInflater();
		View footer = inflater.inflate(R.layout.list_footer, null, false);
		lvMails.addFooterView(footer);
		footer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, "点击了底部", Toast.LENGTH_SHORT).show();
			}
		});
		
		adapter = new MailAdapter(mContext, mails);
		lvMails.setAdapter(adapter);
		
		lvMails.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Mail mail = mails.get(position);
				Intent intent = new Intent(mContext, MailDetailActivity.class);
				intent.putExtra("mail", mail);
				startActivity(intent);
			}
		});
		
		new LoadMailTask().execute(pageNum);
		
		/*Intent service = new Intent(mContext, ReceiveMailService.class);
		service.putExtra("mailAccount", mailAccount);
		service.putExtra("pageNum", 1);
		service.putExtra("pageSize", MailServerUtil.PAGE_SIZE);
		startService(service);*/
	}
	
	class LoadMailTask extends AsyncTask<Integer, Mail, List<Mail>> {

		@Override
		protected List<Mail> doInBackground(Integer... params) {
			if(params != null && params.length > 0) {
				return mailDao.getMailsWithPage(mailAccount, params[0], MailServerUtil.PAGE_SIZE);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(List<Mail> result) {
			if(result != null && result.size() > 0) {
				mails.addAll(result);
				adapter.notifyDataSetChanged();
			}
			super.onPostExecute(result);
		}
	}
	
	class MailAdapter extends BaseAdapter {
		private Context context;
		private List<Mail> list;

		public MailAdapter(Context context, List<Mail> list) {
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
				convertView = inflater.inflate(R.layout.item_mail, null);
				
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
				holder.tvSendDate = (TextView) convertView.findViewById(R.id.tv_sendDate);
				holder.tvDesc = (TextView) convertView.findViewById(R.id.tv_desc);
				holder.ivReadFlag = (ImageView) convertView.findViewById(R.id.iv_read_flag);
				holder.ivAttFlag = (ImageView) convertView.findViewById(R.id.iv_att_flag);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			Mail mail = list.get(position);
			holder.tvSendDate.setText(StringUtil.parseDateStr(mail.getSendDate(), null, null));
			String from = ">>";
			String fromName = mail.getFromName();
			if(StringUtil.isEmpty(fromName)) {
				from += mail.getFromAddress();
			} else {
				from += fromName;
			}
			Spanned spanned = Html.fromHtml(mail.getContent());
			SpannableString ss = new SpannableString(from + spanned);
			ss.setSpan(new RelativeSizeSpan(1.2f), 0, from.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			String title = mail.getSubject();
			if(!mail.isSeen()) {
				SpannableString titleSs = new SpannableString(title);
				titleSs.setSpan(new StyleSpan(Typeface.BOLD), 0, titleSs.length() - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
				holder.tvTitle.setTextColor(getResources().getColor(R.color.black));
				holder.tvTitle.setText(titleSs);
				ss.setSpan(new StyleSpan(Typeface.BOLD), 0, from.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
				ss.setSpan(new ForegroundColorSpan(Color.BLACK), 0, from.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
				holder.ivReadFlag.setBackgroundResource(R.drawable.mail_unread);
			} else {
				holder.tvTitle.setText(title);
				holder.ivReadFlag.setBackgroundResource(R.drawable.mail_readed);
				ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.mail_title_gray)), 0, from.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			}
			holder.tvDesc.setText(ss);
			if(mail.isContainerAttachment()) {
				holder.ivAttFlag.setVisibility(View.VISIBLE);
			} else {
				holder.ivAttFlag.setVisibility(View.GONE);
			}
			return convertView;
		}
		
	}
	
	class ViewHolder {
		TextView tvTitle;
		TextView tvSendDate;
		TextView tvDesc;
		ImageView ivReadFlag;
		ImageView ivAttFlag;
	}
}
