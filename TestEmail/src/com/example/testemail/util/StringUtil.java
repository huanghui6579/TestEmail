package com.example.testemail.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringUtil {
	public static String parseDateStr(String dateStr, String srcPattern, String destPattern) {
		if(isEmpty(dateStr)) {
			return null;
		}
		if(isEmpty(srcPattern)) {
			srcPattern = "yyyy-MM-dd HH:mm:ss";
		}
		if(isEmpty(destPattern)) {
			destPattern = "yyyy-MM-dd";
		}
		SimpleDateFormat parseFormat = new SimpleDateFormat(srcPattern, Locale.getDefault());
		Date date = null;
		try {
			date = parseFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			date = new Date();
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(destPattern, Locale.getDefault());
		return dateFormat.format(date);
	}
	
	public static boolean isEmpty(String str) {
		if(str == null || "".equals(str)) {
			return true;
		}
		return false;
	}
}
