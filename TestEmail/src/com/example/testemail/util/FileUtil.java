package com.example.testemail.util;

import android.os.Environment;

public class FileUtil {
	public static boolean isSdCradAvailable() {
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED) && !state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			return true;
		}
		return false;
	}
}
