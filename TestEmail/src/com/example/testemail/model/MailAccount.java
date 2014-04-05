package com.example.testemail.model;

import com.example.testemail.util.MailServerUtil;

public class MailAccount {
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
}
