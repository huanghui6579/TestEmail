package com.example.testemail.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import android.content.Context;

import com.example.testemail.dao.AttachmentDao;
import com.example.testemail.dao.MailAccountDao;
import com.example.testemail.dao.MailDao;
import com.example.testemail.model.Attachment;
import com.example.testemail.model.Mail;
import com.example.testemail.model.MailAccount;

public class ReceiveMailUtil {
//	public static void main(String[] args) {
//		MailAccount account = new MailAccount();
//		account.setUsername("huanghui6579@gmail.com");
//		account.setPassword("xxxxx");
//		account.setUsername("409384897@qq.com");
//		account.setPassword("xxxxx");
//		account.setUsername("huanghui6579@sohu.com");
//		account.setPassword("xxxxx");
//		account.setUsername("huanghui6579@163.com");
//		account.setPassword("xxxxx");
//		try {
//			receive(account);
//		} catch (MessagingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	
	private static ReceiveMailUtil instance = null;
	
	private Context context;
	
	private AttachmentDao attachmentDao = null;
	private MailDao mailDao = null;
	
	private ReceiveMailUtil(Context context) {
		this.context = context;
		attachmentDao = new AttachmentDao(context);
		mailDao = new MailDao(context);
	}
	
	private ReceiveMailUtil() {}
	
	public static synchronized ReceiveMailUtil getInstance(Context context) {
		if(instance == null) {
			instance = new ReceiveMailUtil(context);
		}
		return instance;
	}
	
	/*public void receive(MailAccount account) throws MessagingException, IOException {
		Session session = MailServerUtil.validateAccount(account);
		Store store = session.getStore();
		store.connect();
		if(store.isConnected()) {
			// 获得收件箱
			Folder folder = store.getFolder("INBOX");
			 Folder.READ_ONLY：只读权限 
	         * Folder.READ_WRITE：可读可写（可以修改邮件的状态） 
	           
	        folder.open(Folder.READ_WRITE); //打开收件箱
	        // 由于POP3协议无法获知邮件的状态,所以getUnreadMessageCount得到的是收件箱的邮件总数  
	        System.out.println("未读邮件数: " + folder.getUnreadMessageCount());  
	          
	        // 由于POP3协议无法获知邮件的状态,所以下面得到的结果始终都是为0  
	        System.out.println("删除邮件数: " + folder.getDeletedMessageCount());  
	        System.out.println("新邮件: " + folder.getNewMessageCount());  
	          
	        // 获得收件箱中的邮件总数  
	        System.out.println("邮件总数: " + folder.getMessageCount());
	        // 得到收件箱中的所有邮件,并解析  
	        parseMessage(getMessagesWithPage(folder, 1, 20));  
	          
	        //释放资源  
	        folder.close(true);  
	        store.close();
		}
	}*/
	
	public void receive(MailAccount account, int pageNum, int pageSize) throws MessagingException, IOException {
		try {
			Store store = MailServerUtil.login(account);
			if(store != null & store.isConnected()) {
				Folder folder = store.getFolder(MailServerUtil.INBOX);
				folder.open(Folder.READ_ONLY);
				// 得到收件箱中的所有邮件,并解析  
				parseMessage(account, getMessagesWithPage(folder, pageNum, pageSize));  
				
				//释放资源  
				folder.close(true);  
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Message> getMessagesWithPage(Folder folder, int pageNum, int pageSize) throws MessagingException {
		Message[] messages = folder.getMessages();
		Arrays.sort(messages, new Comparator<Message>() {

			public int compare(Message o1, Message o2) {
				Message m1 = (Message)o1;
	            Message m2 = (Message)o2;
	            if(m1.getMessageNumber() > m2.getMessageNumber()) {
	            	return -1;
	            }
	            return 1;
			}

		});
		List<Message> list = Arrays.asList(messages);
		int start = (pageNum - 1) * pageSize;
		int end = pageNum * pageSize;
		return list.subList(start, end);
	}
	
	/** 
     * 解析邮件 
     * @param messages 要解析的邮件列表 
     */  
    public void parseMessage(MailAccount account, List<Message> messages) throws MessagingException, IOException {  
        if (messages == null || messages.size() < 1)   
            throw new MessagingException("未找到要解析的邮件!");  
        if(mailDao == null) {
        	mailDao = new MailDao(context);
    	}
        // 解析所有邮件  
        for (int i = 0, count = messages.size(); i < count; i++) {  
            MimeMessage msg = (MimeMessage) messages.get(i);  
            //buildEmlFile(msg, i);
            Mail mail = new Mail();
            mail.setEmailAddress(account.getEmailAddress());
            mail.setMailNumber(msg.getMessageNumber());
            mail.setSubject(getSubject(msg));
            mail.setFromAddress(getFromAddress(msg));
            mail.setFromName(getFromName(msg));
            mail.setReceiveAddress(getReceiveAddress(msg, Message.RecipientType.TO));
            mail.setCcAddress(getReceiveAddress(msg, Message.RecipientType.CC));
            mail.setBccAddress(getReceiveAddress(msg, Message.RecipientType.BCC));
            mail.setSendDate(getSentDate(msg, "yyyy-MM-dd HH:mm:ss"));
            mail.setSeen(isSeen(msg));	//邮件是否已经查看
            mail.setPriority(getPriority(msg));	//邮件的优先级
            mail.setReplySign(isReplySign(msg));//是否需要回执
            mail.setMailSize(msg.getSize());
            mail.setContainerAttachment(isContainAttachment(msg));
            mail.setMessage(msg);
           /* if (isContainerAttachment) {  
                saveAttachment(msg, "e:\\mailtmp\\" + getString(getSubject(msg))); //保存附件  
            }  */ 
            try {
            	StringBuilder content = new StringBuilder();
				getMailTextContent(msg, content);
				mail.setContent(content.toString());
			} catch (Exception e) {
				e.printStackTrace();
			} 
            mail.getAttachments().clear();
            getAttachmentList(mail, msg);
            mailDao.add(mail);
        }  
    }
    
    private static String getString(String str) {
    	String regex = "[\\/:?*<>|]";
    	Pattern p = Pattern.compile(regex);
    	Matcher m = p.matcher(str);
    	return m.replaceAll("").trim();
    }
    
    /** 
     * 获得邮件主题 
     * @param msg 邮件内容 
     * @return 解码后的邮件主题 
     */  
    public static String getSubject(MimeMessage msg) throws UnsupportedEncodingException, MessagingException {  
        return decodeText(msg.getSubject());  
    }
    
    /** 
     * 将邮件内容生成eml文件 
     * @param message 邮件内容 
     */  
    public static File buildEmlFile(Message message, int i) throws MessagingException, FileNotFoundException, IOException {  
        File file = new File("e:\\" + i + ".eml");  
//        MimeUtility.decodeText(message.getSubject()).trim()
        message.writeTo(new FileOutputStream(file));  
        return file;  
    }
    
    /** 
     * 获得邮件发件人 
     * @param msg 邮件内容 
     * @return 姓名 <Email地址> 
     * @throws MessagingException 
     * @throws UnsupportedEncodingException  
     */  
    public static String getFrom(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {  
        String from = "";  
        Address[] froms = msg.getFrom();  
        if (froms.length < 1)  
            throw new MessagingException("没有发件人!");  
          
        InternetAddress address = (InternetAddress) froms[0];  
        String person = address.getPersonal();  
        if (person != null) {  
            person = decodeText(person) + " ";  
        } else {  
            person = "";  
        }  
        from = person + "<" + address.getAddress() + ">";  
          
        return from;  
    }
    
    /** 
     * 获得邮件发件人 
     * @param msg 邮件内容 
     * @return 姓名 <Email地址> 
     * @throws MessagingException 
     * @throws UnsupportedEncodingException  
     */  
    public static String getFromName(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {  
    	Address[] froms = msg.getFrom();  
    	if (froms.length < 1)  
    		throw new MessagingException("没有发件人!");  
    	
    	InternetAddress address = (InternetAddress) froms[0];  
    	String person = address.getPersonal();  
    	if (person != null) {  
    		person = decodeText(person) + " ";  
    	}
    	return person;
    }
    
    /** 
     * 获得邮件发件人名称
     * @param msg 邮件内容 
     * @return 姓名 <Email地址> 
     * @throws MessagingException 
     * @throws UnsupportedEncodingException  
     */  
    public static String getFromAddress(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {  
    	Address[] froms = msg.getFrom();  
    	if (froms.length < 1)  
    		throw new MessagingException("没有发件人!");  
    	
    	InternetAddress address = (InternetAddress) froms[0];  
    	return address.getAddress();
    }
    
    /** 
     * 根据收件人类型，获取邮件收件人、抄送和密送地址。如果收件人类型为空，则获得所有的收件人 
     * <p>Message.RecipientType.TO  收件人</p> 
     * <p>Message.RecipientType.CC  抄送</p> 
     * <p>Message.RecipientType.BCC 密送</p> 
     * @param msg 邮件内容 
     * @param type 收件人类型 
     * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ... 
     * @throws MessagingException 
     */  
    public static String getReceiveAddress(MimeMessage msg, Message.RecipientType type) throws MessagingException {  
        StringBuilder receiveAddress = new StringBuilder();  
        Address[] addresss = null;  
        if (type == null) {  
            addresss = msg.getAllRecipients(); 
        } else {  
            addresss = msg.getRecipients(type);  
        }  
        if(addresss != null && addresss.length > 0) {
	        for (Address address : addresss) {  
	            InternetAddress internetAddress = (InternetAddress)address;  
	            receiveAddress.append(internetAddress.toUnicodeString()).append(",");  
	        }  
	        receiveAddress.deleteCharAt(receiveAddress.length()-1); //删除最后一个逗号  
	        return receiveAddress.toString();  
        } else {
        	return null;
        }
          
    }
    
    /** 
     * 获得邮件发送时间 
     * @param msg 邮件内容 
     * @return yyyy年mm月dd日 星期X HH:mm 
     * @throws MessagingException 
     */  
    public static String getSentDate(MimeMessage msg, String pattern) throws MessagingException {  
        Date receivedDate = msg.getSentDate();  
        if (receivedDate == null)  
            return "";  
          
        if (pattern == null || "".equals(pattern))  
            pattern = "yyyy年MM月dd日 E HH:mm ";  
          
        return new SimpleDateFormat(pattern).format(receivedDate);  
    }
    
    /** 
     * 判断邮件中是否包含附件 
     * @param msg 邮件内容 
     * @return 邮件中存在附件返回true，不存在返回false 
     * @throws MessagingException 
     * @throws IOException 
     */  
    public static boolean isContainAttachment(Part part) throws MessagingException, IOException {  
        boolean flag = false;  
        if (part.isMimeType("multipart/*")) {  
            MimeMultipart multipart = (MimeMultipart) part.getContent();  
            int partCount = multipart.getCount();  
            for (int i = 0; i < partCount; i++) {  
                BodyPart bodyPart = multipart.getBodyPart(i);  
                String disp = bodyPart.getDisposition();  
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {  
                    flag = true;  
                } else if (bodyPart.isMimeType("multipart/*")) {  
                    flag = isContainAttachment(bodyPart);  
                } else {  
                    String contentType = bodyPart.getContentType();  
                    if (contentType.indexOf("application") != -1) {  
                        flag = true;  
                    }    
                      
                    if (contentType.indexOf("name") != -1) {  
                        flag = true;  
                    }   
                }  
                  
                if (flag) break;  
            }  
        } else if (part.isMimeType("message/rfc822")) {  
            flag = isContainAttachment((Part)part.getContent());  
        }  
        return flag;  
    }
    
    /**  
     * 判断邮件是否已读  
     * @param msg 邮件内容  
     * @return 如果邮件已读返回true,否则返回false  
     * @throws MessagingException   
     */  
    public static boolean isSeen(MimeMessage msg) throws MessagingException {  
        return msg.getFlags().contains(Flags.Flag.SEEN);  
    }
    
    /** 
     * 判断邮件是否需要阅读回执 
     * @param msg 邮件内容 
     * @return 需要回执返回true,否则返回false 
     * @throws MessagingException 
     */  
    public static boolean isReplySign(MimeMessage msg) throws MessagingException {  
        boolean replySign = false;  
        String[] headers = msg.getHeader("Disposition-Notification-To");  
        if (headers != null)  
            replySign = true;  
        return replySign;  
    }
    
    /** 
     * 获得邮件的优先级 
     * @param msg 邮件内容 
     * @return 1(High):紧急  3:普通(Normal)  5:低(Low) 
     * @throws MessagingException  
     */  
    public static String getPriority(MimeMessage msg) throws MessagingException {  
        String priority = "普通";  
        String[] headers = msg.getHeader("X-Priority");  
        if (headers != null) {  
            String headerPriority = headers[0];  
            if (headerPriority.indexOf("1") != -1 || headerPriority.indexOf("High") != -1)  
                priority = "紧急";  
            else if (headerPriority.indexOf("5") != -1 || headerPriority.indexOf("Low") != -1)  
                priority = "低";  
            else  
                priority = "普通";  
        }  
        return priority;  
    }
    
    /** 
     * 获得邮件文本内容 
     * @param part 邮件体 
     * @param content 存储邮件文本内容的字符串 
     * @throws MessagingException 
     * @throws IOException 
     */  
    public static void getMailTextContent(Part part, StringBuilder content) throws MessagingException, IOException {
    	String[] charsets = part.getHeader("Content-Transfer-Encoding");
        //如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断  
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;  
        if (part.isMimeType("text/*") && !isContainTextAttach) {
        	String encoding = null;
        	if(charsets != null && charsets.length > 0) {
        		encoding = charsets[0];
        	}
        	if(encoding != null && encoding.toLowerCase().startsWith("gb")) {
        		part.removeHeader("Content-Transfer-Encoding");
        	}
    		content.append(part.getContent().toString());  
        } else if (part.isMimeType("message/rfc822")) {   
            getMailTextContent((Part)part.getContent(),content);  
        } else if (part.isMimeType("multipart/*")) {  
            Multipart multipart = (Multipart) part.getContent();  
            int partCount = multipart.getCount();  
            for (int i = 0; i < partCount; i++) {  
                BodyPart bodyPart = multipart.getBodyPart(i);  
                getMailTextContent(bodyPart,content);  
            }  
        } 
    }
    
    /**  
     * 保存附件  
     * @param part 邮件中多个组合体中的其中一个组合体  
     * @param destDir  附件保存目录  
     * @throws UnsupportedEncodingException  
     * @throws MessagingException  
     * @throws FileNotFoundException  
     * @throws IOException  
     */  
    public static void saveAttachment(Part part, String destDir) throws UnsupportedEncodingException, MessagingException,  
            FileNotFoundException, IOException {  
        if (part.isMimeType("multipart/*")) {  
            Multipart multipart = (Multipart) part.getContent();    //复杂体邮件  
            //复杂体邮件包含多个邮件体  
            int partCount = multipart.getCount();  
            for (int i = 0; i < partCount; i++) {  
                //获得复杂体邮件中其中一个邮件体  
                BodyPart bodyPart = multipart.getBodyPart(i);  
                //某一个邮件体也有可能是由多个邮件体组成的复杂体  
                String disp = bodyPart.getDisposition();  
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    InputStream is = bodyPart.getInputStream();  
                    saveFile(is, destDir, decodeText(bodyPart.getFileName()));  
                } else if (bodyPart.isMimeType("multipart/*")) {  
                    saveAttachment(bodyPart,destDir);  
                } else {  
                    String contentType = bodyPart.getContentType();  
                    if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {  
                        saveFile(bodyPart.getInputStream(), destDir, decodeText(bodyPart.getFileName()));  
                    }  
                }  
            }  
        } else if (part.isMimeType("message/rfc822")) {  
            saveAttachment((Part) part.getContent(),destDir);  
        }  
    }
    
    /**  
     * 保存附件  
     * @param part 邮件中多个组合体中的其中一个组合体  
     * @param destDir  附件保存目录  
     * @throws UnsupportedEncodingException  
     * @throws MessagingException  
     * @throws FileNotFoundException  
     * @throws IOException  
     */  
    public void getAttachmentList(Mail mail, Part part) throws UnsupportedEncodingException, MessagingException,  
    FileNotFoundException, IOException { 
    	if(attachmentDao == null) {
    		attachmentDao = new AttachmentDao(context);
    	}
    	if (part.isMimeType("multipart/*")) {  
    		Multipart multipart = (Multipart) part.getContent();    //复杂体邮件  
    		//复杂体邮件包含多个邮件体  
    		int partCount = multipart.getCount();  
    		for (int i = 0; i < partCount; i++) {  
    			//获得复杂体邮件中其中一个邮件体  
    			BodyPart bodyPart = multipart.getBodyPart(i);  
    			//某一个邮件体也有可能是由多个邮件体组成的复杂体  
    			String disp = bodyPart.getDisposition();  
    			if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
    				Attachment att = new Attachment();
    				att.setEmailAddress(mail.getEmailAddress());
    				att.setEmailNumber(mail.getMailNumber());
    				att.setFileName(decodeText(bodyPart.getFileName()));
    				att.setFileSize(bodyPart.getSize());
    				mail.getAttachments().add(att);
    				attachmentDao.add(att);
    				//InputStream is = bodyPart.getInputStream();  
    				//saveFile(is, destDir, decodeText(bodyPart.getFileName()));  
    			} else if (bodyPart.isMimeType("multipart/*")) {  
    				getAttachmentList(mail, bodyPart);  
    			} else {  
    				String contentType = bodyPart.getContentType();  
    				if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {
    					Attachment att = new Attachment();
        				att.setEmailAddress(mail.getEmailAddress());
        				att.setEmailNumber(mail.getMailNumber());
        				att.setFileName(decodeText(bodyPart.getFileName()));
        				att.setFileSize(bodyPart.getSize());
        				mail.getAttachments().add(att);
    					//saveFile(bodyPart.getInputStream(), destDir, decodeText(bodyPart.getFileName()));  
    				}  
    			}  
    		}  
    	} else if (part.isMimeType("message/rfc822")) {  
    		getAttachmentList(mail, (Part) part.getContent());  
    	}  
    }
    
    /**  
     * 读取输入流中的数据保存至指定目录  
     * @param is 输入流  
     * @param fileName 文件名  
     * @param destDir 文件存储目录  
     * @throws FileNotFoundException  
     * @throws IOException  
     */  
    private static File saveFile(InputStream is, String destDir, String fileName) throws IOException {  
    	File dir = new File(destDir);
    	fileName = getString(fileName);
    	if(!dir.exists()) {
    		dir.mkdirs();
    	}
        BufferedInputStream bis = new BufferedInputStream(is);  
        BufferedOutputStream bos = null;
		try {
			File saveFile = new File(destDir, fileName);
			bos = new BufferedOutputStream(  
			        new FileOutputStream(saveFile));
			int len = -1;  
	        while ((len = bis.read()) != -1) {  
	            bos.write(len);  
	            bos.flush();  
	        }  
	        bos.close();  
	        bis.close();
	        return saveFile;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  
        return null;
    }
    
    /** 
     * 文本解码 
     * @param encodeText 解码MimeUtility.encodeText(String text)方法编码后的文本 
     * @return 解码后的文本 
     * @throws UnsupportedEncodingException 
     */  
    public static String decodeText(String encodeText) throws UnsupportedEncodingException {  
        if (encodeText == null || "".equals(encodeText)) {  
            return "";  
        } else {  
        	encodeText = MimeUtility.decodeText(encodeText);
            return MimeUtility.decodeText(encodeText);  
        }  
    }
}
