package com.example.testemail.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import com.example.testemail.R;
import com.example.testemail.model.MailAccount;
import com.example.testemail.model.MailInfo;

public class MailServerUtil {
	public static final int POP3_PORT = 110;	//默认接收邮件的端口，pop3
	public static final int IMAP_PORT = 143;	//默认接收邮件的端口，imap
	public static final int SMTP_PORT = 25;	//默认发送邮件的端口，smtp
	
	public static final String MAIL_SINA = "sina";
	public static final String MAIL_SINA_VIP = "sina_vip";
	public static final String MAIL_SOHU = "sohu";
	public static final String MAIL_126 = "126";
	public static final String MAIL_163 = "163";
	public static final String MAIL_QQ = "qq";
	public static final String MAIL_QQ_ENTERPRISE = "qq_enterprise";
	public static final String MAIL_YAHOO = "yahoo";
	public static final String MAIL_HOTMAIL = "hotmail";
	public static final String MAIL_GMAIL = "gmail";
	
	public static Map<String, MailInfo> mailMap = null;
	private static Map<String, String> typeMap = null;
	
	private static Map<String, Integer> resMap = null;
	
	public static final String INBOX = "INBOX";
	
	public static final int PAGE_SIZE = 20;
	
	static {
		mailMap = new HashMap<String, MailInfo>();
		//sina mail pop3
//		MailInfo mailInfo = new MailInfo("smtp.sina.com", "pop.sina.com", MailInfo.RECEIVE_TYPE_POP3, false);
//		mailMap.put(MAIL_SINA, mailInfo);
		MailInfo mailInfo = new MailInfo("smtp.sina.com", "imap.sina.com", MailInfo.RECEIVE_TYPE_IMAP, false);
		mailMap.put(MAIL_SINA, mailInfo);
		
		//sina vip mail imap
		mailInfo = new MailInfo("smtp.vip.sina.com", "imap.vip.sina.com", MailInfo.RECEIVE_TYPE_IMAP, false);
		mailMap.put(MAIL_SINA_VIP, mailInfo);
		
		//sohu mail pop3
		mailInfo = new MailInfo("smtp.sohu.com", "pop3.sohu.com", MailInfo.RECEIVE_TYPE_POP3, false);
		mailMap.put(MAIL_SOHU, mailInfo);
//		mailInfo = new MailInfo("mail.sohu.com", "mail.sohu.com", MailInfo.RECEIVE_TYPE_IMAP, false);
//		mailMap.put(MAIL_SOHU, mailInfo);
		
//		mailInfo = new MailInfo("smtp.sohu.com", IMAP_PORT, IMAP, "imap.sohu.com");
//		mailInfo.setReceiveType(MailInfo.RECEIVE_TYPE_IMAP);
//		mailMap.put(MAIL_SOHU, mailInfo);
		
		//126 mail imap
		mailInfo = new MailInfo("smtp.126.com", "imap.126.com", MailInfo.RECEIVE_TYPE_IMAP, false);
		mailMap.put(MAIL_126, mailInfo);
		
		//163 mail imap
		mailInfo = new MailInfo("smtp.163.com", "imap.163.com", MailInfo.RECEIVE_TYPE_IMAP, false);
		mailMap.put(MAIL_163, mailInfo);
		
//		mailInfo = new MailInfo("smtp.163.com", POP3_PORT, POP3, "pop.163.com");
//		mailInfo.setReceiveType(MailInfo.RECEIVE_TYPE_POP3);
//		mailMap.put(MAIL_163, mailInfo);
		
		//qq mail pop3
//		mailInfo = new MailInfo("smtp.qq.com", POP3_PORT, POP3, "pop.qq.com");
//		mailInfo.setReceiveType(MailInfo.RECEIVE_TYPE_POP3);
//		mailMap.put(MAIL_QQ, mailInfo);
		
//		mailInfo = new MailInfo("smtp.qq.com", "imap.qq.com", MailInfo.RECEIVE_TYPE_IMAP, true);
//		mailMap.put(MAIL_QQ, mailInfo);
		mailInfo = new MailInfo("smtp.qq.com", "pop.qq.com", MailInfo.RECEIVE_TYPE_POP3, false);
		mailMap.put(MAIL_QQ, mailInfo);
		
		//qq enterprise mail pop3
		mailInfo = new MailInfo("smtp.exmail.qq.com", "pop.exmail.qq.com", MailInfo.RECEIVE_TYPE_POP3, false);
		mailMap.put(MAIL_QQ_ENTERPRISE, mailInfo);
		
		//yahoo mail pop3
//		mailInfo = new MailInfo("smtp.mail.yahoo.com", "pop.mail.yahoo.com", MailInfo.RECEIVE_TYPE_POP3, false);
//		mailMap.put(MAIL_YAHOO, mailInfo);
		mailInfo = new MailInfo("smtp.mail.yahoo.com", "imap.mail.yahoo.com", MailInfo.RECEIVE_TYPE_IMAP, true);
		mailMap.put(MAIL_YAHOO, mailInfo);
		
		//hotmail mail pop3
//		mailInfo = new MailInfo("smtp.live.com", POP3_PORT, POP3, "pop3.live.com");
//		mailInfo.setReceiveType(MailInfo.RECEIVE_TYPE_POP3);
//		mailMap.put(MAIL_HOTMAIL, mailInfo);
		mailInfo = new MailInfo("smtp.live.com", "imap-mail.outlook.com", MailInfo.RECEIVE_TYPE_IMAP, true);
		mailMap.put(MAIL_HOTMAIL, mailInfo);
//		mailInfo = new MailInfo("smtp.live.com", "pop3.live.com", MailInfo.RECEIVE_TYPE_POP3, false);
//		mailMap.put(MAIL_HOTMAIL, mailInfo);
		
		//gmail mail pop3
//		mailInfo = new MailInfo("smtp.gmail.com", POP3_PORT, POP3, "pop.gmail.com");
//		mailInfo.setReceiveType(MailInfo.RECEIVE_TYPE_POP3);
//		mailMap.put(MAIL_GMAIL, mailInfo);
		
//		mailInfo = new MailInfo("smtp.gmail.com", "pop.gmail.com", MailInfo.RECEIVE_TYPE_POP3, false);
//		mailMap.put(MAIL_GMAIL, mailInfo);
		
		mailInfo = new MailInfo("smtp.gmail.com", "imap.gmail.com", MailInfo.RECEIVE_TYPE_IMAP, true);
		mailMap.put(MAIL_GMAIL, mailInfo);
		
		typeMap = new HashMap<String, String>();
		typeMap.put("sina.com", MAIL_SINA);
		typeMap.put("vip.sina.com", MAIL_SINA_VIP);
		typeMap.put("sohu.com", MAIL_SOHU);
		typeMap.put("126.com", MAIL_126);
		typeMap.put("163.com", MAIL_163);
		typeMap.put("qq.com", MAIL_QQ);
		typeMap.put("yahoo.com", MAIL_YAHOO);
		typeMap.put("hotmail.com", MAIL_HOTMAIL);
		typeMap.put("gmail.com", MAIL_GMAIL);
		
		resMap = new HashMap<String, Integer>();
		resMap.put(MAIL_SINA, R.drawable.icon_sina);
		resMap.put(MAIL_SINA_VIP, R.drawable.icon_sina);
		resMap.put(MAIL_SOHU, R.drawable.icon_sohu);
		resMap.put(MAIL_126, R.drawable.icon_163);
		resMap.put(MAIL_163, R.drawable.icon_163);
		resMap.put(MAIL_QQ, R.drawable.icon_qq);
		resMap.put(MAIL_YAHOO, R.drawable.icon_yahoo);
		resMap.put(MAIL_HOTMAIL, R.drawable.icon_hotmail);
		resMap.put(MAIL_GMAIL, R.drawable.icon_gmail);
	}
	
	public static Map<String, String> getTypeMap() {
		return typeMap;
	}

	public static MailInfo getMailInfo(String mailType) {
		return mailMap.get(mailType);
	}
	
	public static int getResId(String mailType) {
		int resId = R.drawable.icon_email;
		if(mailType != null) {
			resId = resMap.get(mailType);
		}
		if(resId == 0) {
			resId = R.drawable.icon_email;
		}
		return resId;
	}
	
	public static String getMailType(String emailAddress) {
		String mailType = null;
		String subfix = emailAddress.substring(emailAddress.lastIndexOf("@") + 1);
		if(getTypeMap().containsKey(subfix)) {
			mailType = (String) getTypeMap().get(subfix);
		}
		return mailType;
	}
	
	/**
	 * 登录
	 * @param account
	 * @return
	 * @throws MessagingException
	 */
	public static Store login(MailAccount account) throws MessagingException {
		Session session = validateAccount(account);
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
	
	/**
     * 验证账号
     * @param account
     * @return
     */
	public static Session validateAccount(MailAccount account) {
		MailInfo mailInfo = MailServerUtil.getMailInfo(account.getMailType());
		Session session = null;
		if(mailInfo != null) {
			Properties props = new Properties();
			props.setProperty("mail.smtp.host", mailInfo.getSendHost());
			props.setProperty("mail.smtp.timeout", "15000");
			if(mailInfo.isSSL()) {
				final String SSL_FACTORY = "com.example.testemail.util.DummySSLSocketFactory";
				props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
				props.setProperty("mail.smtp.socketFactory.fallback", "false");
				props.setProperty("mail.smtp.secure.enable", "true");
				props.setProperty("mail.smtp.ssl.enable", "true");
				props.setProperty("mail.smtp.socketFactory.port", String.valueOf(mailInfo.getSendPort()));
				if(mailInfo.getReceiveType() == MailInfo.RECEIVE_TYPE_IMAP) {
					props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
					props.setProperty("mail.imap.socketFactory.fallback", "false");
					props.setProperty("mail.imap.secure.enable","true");
					props.setProperty("mail.imap.ssl.enable", "true");
					props.setProperty("mail.imap.socketFactory.port", String.valueOf(mailInfo.getReceivePort()));
				} else if(mailInfo.getReceiveType() == MailInfo.RECEIVE_TYPE_POP3) {
					props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
					props.setProperty("mail.pop3.socketFactory.fallback", "false");
					props.setProperty("mail.pop3.secure.enable","true");
					props.setProperty("mail.pop3.ssl.enable", "true");
					props.setProperty("mail.pop3.socketFactory.port", String.valueOf(mailInfo.getReceivePort()));
				}
				
			}
			props.setProperty("mail.store.protocol", mailInfo.getProtocol());
			props.setProperty("mail.smtp.port", String.valueOf(mailInfo.getSendPort()));
			props.setProperty("mail.smtp.auth", "true");
			if(mailInfo.getReceiveType() == MailInfo.RECEIVE_TYPE_IMAP) {
				props.setProperty("mail.imap.host", mailInfo.getReceiveHost());
				props.setProperty("mail.imap.timeout", "15000");
				props.setProperty("mail.imap.port", String.valueOf(mailInfo.getReceivePort()));
			} else if(mailInfo.getReceiveType() == MailInfo.RECEIVE_TYPE_POP3) {
				props.setProperty("mail.pop3.timeout", "15000");
				props.setProperty("mail.pop3.host", mailInfo.getReceiveHost());
				props.setProperty("mail.pop3.port", String.valueOf(mailInfo.getReceivePort()));
			}
			session = Session.getInstance(props, new MyAuthenticator(account.getUsername(), account.getPassword()));
			/*try {
				Store store = session.getStore(mailInfo.getProtocol());
				store.connect();
				if(store.isConnected()) {
					return store;
				}
				return null;
			} catch (NoSuchProviderException e) {
//				try {
//					System.err.println(new String(e.getMessage().getBytes("iso-8859-1"), "gbk"));
//				} catch (UnsupportedEncodingException e1) {
//					e1.printStackTrace();
//				}
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
//				try {
//					System.err.println(new String(e.getMessage().getBytes("iso-8859-1"), "gbk"));
//				} catch (UnsupportedEncodingException e1) {
//					e1.printStackTrace();
//				}
			}*/
		}
		return session;
	}
	
}

class MyAuthenticator extends Authenticator {
	private String username;
	private String password;
	
	public MyAuthenticator(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}
}
