package com.example.testemail.model;

import java.io.Serializable;

public class MailInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int RECEIVE_TYPE_IMAP = 1;
	public static final int RECEIVE_TYPE_POP3 = 2;
	
	public static final String POP3 = "pop3";
	public static final String IMAP = "imap";
	
	private static final int SMTP_PORT = 25;    //默认发送邮件的端口，smtp 
	private static final int POP3_PORT = 110;   //默认接收邮件的端口，pop3  
	private static final int IMAP_PORT = 143;   //默认接收邮件的端口，imap
	
	private static final int POP3_SSL_PORT = 995;
	private static final int IMAP_SSL_PORT = 993;
	private static final int SMTP_SSL_PORT = 465;
	

	private String sendHost;
	private int sendPort = SMTP_PORT;
	private int receivePort;
	private String protocol;
	private String receiveHost;
	private int receiveType = RECEIVE_TYPE_IMAP;
	private boolean isSSL = false;

	public MailInfo() {
	}

	public MailInfo(String sendHost, String receiveHost, int receiveType, boolean isSSL) {
		super();
		this.sendHost = sendHost;
		this.receiveHost = receiveHost;
		this.receiveType = receiveType;
		this.isSSL = isSSL;
		
		initSet();
	}
	
	private void initSet() {
		if(isSSL) {
			sendPort = SMTP_SSL_PORT; 
			if(receiveType == RECEIVE_TYPE_IMAP) {
				protocol = IMAP;
				receivePort = IMAP_SSL_PORT;
			} else if(receiveType == RECEIVE_TYPE_POP3) {
				protocol = POP3;
				receivePort = POP3_SSL_PORT;
			}
		}  else {
			sendPort = SMTP_PORT;
			if(receiveType == RECEIVE_TYPE_IMAP) {
				protocol = IMAP;
				receivePort = IMAP_PORT;
			} else if(receiveType == RECEIVE_TYPE_POP3) {
				protocol = POP3;
				receivePort = POP3_PORT;
			}
		}
	}

	public int getSendPort() {
		return sendPort;
	}

	public void setSendPort(int sendPort) {
		this.sendPort = sendPort;
	}

	public int getReceivePort() {
		return receivePort;
	}

	public void setReceivePort(int receivePort) {
		this.receivePort = receivePort;
	}

	public boolean isSSL() {
		return isSSL;
	}

	public void setSSL(boolean isSSL) {
		this.isSSL = isSSL;
		
		initSet();
	}

	public String getSendHost() {
		return sendHost;
	}

	public void setSendHost(String sendHost) {
		this.sendHost = sendHost;
	}

	public String getProtocol() {
		if(receiveType == RECEIVE_TYPE_IMAP) {
			protocol = IMAP;
		} else if(receiveType == RECEIVE_TYPE_POP3) {
			protocol = POP3;
		}
		return protocol;
	}

	public String getReceiveHost() {
		return receiveHost;
	}

	public void setReceiveHost(String receiveHost) {
		this.receiveHost = receiveHost;
	}

	public int getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(int receiveType) {
		this.receiveType = receiveType;
		
		initSet();
	}
}
