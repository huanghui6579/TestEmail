package com.example.testemail.model;

import com.example.testemail.util.MailServerUtil;

public class MailAccount {
	private String username;
	private String password;
	private String emailAddress;
	private String mailType;
	private int resId;
	private MailInfo mailInfo;

	public MailInfo getMailInfo() {
		if(mailInfo == null) {
			mailInfo = MailServerUtil.getMailInfo(mailType);
		}
		return mailInfo;
	}

	public void setMailInfo(MailInfo mailInfo) {
		this.mailInfo = mailInfo;
	}

	public String getMailType() {
		if(mailType == null || "".equals(mailType)) {
			if(getEmailAddress() == null) {
				setEmailAddress(username);
			}
			String subfix = emailAddress.substring(emailAddress.lastIndexOf("@") + 1);
			if(MailServerUtil.getTypeMap().containsKey(subfix)) {
				mailType = (String) MailServerUtil.getTypeMap().get(subfix);
			}
		}
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
		emailAddress = username;
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
		if(username == null) {
			username = emailAddress;
		}
	}

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}
}
