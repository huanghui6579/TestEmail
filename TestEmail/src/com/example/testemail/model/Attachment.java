package com.example.testemail.model;

/**
 * 附件
 * 
 * @author Administrator
 * 
 */
public class Attachment {
	private int id;
	private String emailAddress;
	private int emailNumber;
	private String fileName;
	private long fileSize;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public int getEmailNumber() {
		return emailNumber;
	}

	public void setEmailNumber(int emailNumber) {
		this.emailNumber = emailNumber;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
}
