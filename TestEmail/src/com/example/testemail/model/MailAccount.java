package com.example.testemail.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.testemail.util.MailServerUtil;

public class MailAccount implements Parcelable {
	private String username;
	private String password;
	private String emailAddress;
	private String mailType;
	private int resId;
	private int unReadCount;
	private MailInfo mailInfo;

	public MailInfo getMailInfo() {
		if(mailInfo == null) {
			mailInfo = MailServerUtil.getMailInfo(mailType);
		}
		return mailInfo;
	}
	
	public MailAccount() {
	}

	public MailAccount(Parcel source) {
		username = source.readString();
		password = source.readString();
		emailAddress = source.readString();
		mailType = source.readString();
		resId = source.readInt();
		unReadCount = source.readInt();
	}

	public int getUnReadCount() {
		return unReadCount;
	}

	public void setUnReadCount(int unReadCount) {
		this.unReadCount = unReadCount;
	}

	public void setMailInfo(MailInfo mailInfo) {
		this.mailInfo = mailInfo;
	}

	public String getMailType() {
		return mailType;
	}

	public void setMailType(String mailType) {
		this.mailType = mailType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
		this.mailType = MailServerUtil.getMailType(emailAddress);
	}

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(username);
		dest.writeString(password);
		dest.writeString(emailAddress);
		dest.writeString(mailType);
		dest.writeInt(resId);
		dest.writeInt(unReadCount);
	}
	
	public static final Parcelable.Creator<MailAccount> CREATOR = new Creator<MailAccount>() {
		
		@Override
		public MailAccount[] newArray(int size) {
			return new MailAccount[size];
		}
		
		@Override
		public MailAccount createFromParcel(Parcel source) {
			return new MailAccount(source);
		}
	};
}
