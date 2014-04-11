package com.example.testemail.model;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;

import android.os.Parcel;
import android.os.Parcelable;

public class Mail implements Parcelable {
	private int mailNumber;	//邮件序号
	private long mailSize;	//邮件大小,B单位
	private String subject;	//主题
	private String fromAddress;	//发件人地址
	private String fromName;	//发件人名称
	private String receiveAddress;	//收件人地址
	private String ccAddress;	//抄送人地址
	private String bccAddress;	//暗送人地址
	private String sendDate;	//发送日期
	private String priority;	//邮件优先级
	private String content;	//邮件正文
	private String emailAddress;
	private boolean isSeen;	//是否已读
	private boolean isReplySign;	//是否需要回执
	private boolean isContainerAttachment;	//是否包含附件
	private List<Attachment> attachments = new ArrayList<Attachment>();
	private Message message;
	
	public Mail() {
	}
	
	public int getMailNumber() {
		return mailNumber;
	}

	public void setMailNumber(int mailNumber) {
		this.mailNumber = mailNumber;
	}

	public long getMailSize() {
		return mailSize;
	}

	public void setMailSize(long mailSize) {
		this.mailSize = mailSize;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public boolean isSeen() {
		return isSeen;
	}

	public void setSeen(boolean isSeen) {
		this.isSeen = isSeen;
	}

	public boolean isReplySign() {
		return isReplySign;
	}

	public void setReplySign(boolean isReplySign) {
		this.isReplySign = isReplySign;
	}

	public boolean isContainerAttachment() {
		return isContainerAttachment;
	}

	public void setContainerAttachment(boolean isContainerAttachment) {
		this.isContainerAttachment = isContainerAttachment;
	}

	public String getReceiveAddress() {
		return receiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getCcAddress() {
		return ccAddress;
	}

	public void setCcAddress(String ccAddress) {
		this.ccAddress = ccAddress;
	}

	public String getBccAddress() {
		return bccAddress;
	}

	public void setBccAddress(String bccAddress) {
		this.bccAddress = bccAddress;
	}

	public Mail(Parcel source) {
		mailNumber = source.readInt();
		mailSize = source.readLong();
		subject = source.readString();
		fromAddress = source.readString();
		fromName = source.readString();
		receiveAddress = source.readString();
		ccAddress = source.readString();
		bccAddress = source.readString();
		sendDate = source.readString();
		priority = source.readString();
		content = source.readString();
		emailAddress = source.readString();
		isSeen = source.readByte() != 0;
		isReplySign = source.readByte() != 0;
		isContainerAttachment = source.readByte() != 0;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mailNumber);
		dest.writeLong(mailSize);
		dest.writeString(subject);
		dest.writeString(fromAddress);
		dest.writeString(fromName);
		dest.writeString(receiveAddress);
		dest.writeString(ccAddress);
		dest.writeString(bccAddress);
		dest.writeString(sendDate);
		dest.writeString(priority);
		dest.writeString(content);
		dest.writeString(emailAddress);
		dest.writeByte((byte) (isSeen ? 1 : 0));
		dest.writeByte((byte) (isReplySign ? 1 : 0));
		dest.writeByte((byte) (isContainerAttachment ? 1 : 0));
	}
	
	public static final Parcelable.Creator<Mail> CREATOR = new Creator<Mail>() {
		
		@Override
		public Mail[] newArray(int size) {
			return new Mail[size];
		}
		
		@Override
		public Mail createFromParcel(Parcel source) {
			return new Mail(source);
		}
	};
}
