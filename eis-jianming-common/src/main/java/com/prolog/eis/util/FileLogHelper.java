package com.prolog.eis.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileLogHelper {
	public static void main(String[] args) {
		FileLogHelper.WriteLog("123", "aaa", "bbb", "ccc");
		FileLogHelper.WriteLog("123", "aaa", "bbb", "ccc");
		FileLogHelper.WriteLog("123", "aaa", "bbb", "ccc");
	}

	public static void WriteLog(String fileName, String... logs) {
		String str = "";
		for (String log : logs) {
			str += log + "\r\n";
		}
		str += "-----------------------";

		FileLogHelper.WriteLogToFile(fileName, str);
 	}

	private static void WriteLogToFile(String fileName, String log) {
		try {
			Date date = new Date();
			String strDate = new SimpleDateFormat("yyyyMMdd").format(date);
			String currentPath = System.getProperty("user.dir");
			String path = currentPath + "\\" + "FileLog\\" + strDate + "\\" + fileName + ".txt";
			File file = new File(path);

			// 获取父目录
			File fileParent = file.getParentFile();
			// 判断是否存在
			if (!fileParent.exists()) {
				// 创建父目录文件
				fileParent.mkdirs();
			}

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fileWritter = new FileWriter(path, true);
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String nowString = dateFormat.format(now);

			fileWritter.write(nowString + "\r\n" + log + "\r\n");
			fileWritter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
