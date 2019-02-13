package cn.hy.netfiletool.common;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.hy.netfiletool.key.ConstStrings;

public class FileUtil {


	/**
	 * 创建文件夹
	 *
	 * @param filePath
	 */
	public static File createDirs(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	/**
	 * 创建文件
	 *
	 * @param filePath
	 */
	public static File createFile(String filePath) {
		File fl = new File(filePath);
		try {
			if (!fl.exists()) {
				fl.createNewFile();
			} else {
				int num = 1;
				while (!fl.exists()) {
					fl = new File("/" + "(" + num + ")"
							+ filePath);
					num++;
				}
				fl.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return fl;
	}

	/**
	 * 获取指定文件大小(单位：字节)
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static long getFileSize(File file){
		if (file == null) {
			return 0;
		}
		long size = 0;
		if (file.exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				size = fis.available();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return size;
	}



	/**
	 * 根据Base.File对象创建
	 * 手机对应文件
	 * @param fileName
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static File createFileByBaseFile(String fileName, String path) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		File fl = new File(file.getPath() + "/" + fileName);
		if (!fl.exists()) {
			fl.createNewFile();
		} else {
			int num = 1;
			while (fl.exists()) {
				fl = new File(file.getPath() + "/" + "(" + num + ")"
						+ fileName);
				num++;
			}
			fl.createNewFile();
		}
		return fl;
	}


	public static String getFileMIME(String fileName){
		if(fileName.lastIndexOf(ConstStrings.FullStop)>0){
			String suffix = fileName
					.substring(fileName.lastIndexOf(ConstStrings.FullStop),fileName.length());
			if (MIME_Map.containsKey(suffix))
				return MIME_Map.get(suffix);
		}
		return "*/*";
	}

	private static final Map<String,String> MIME_Map =new HashMap<String, String>() {
		{
			put(".3gp",    "video/3gpp");
			put(".flv",    "video/*");
			put(".rm",    "video/*");
			put(".mkv",    "video/*");
			put(".amv",    "video/*");
			put(".apk",    "application/vnd.android.package-archive");
			put(".asf",    "video/x-ms-asf");
			put(".avi",    "video/x-msvideo");
			put(".bin",    "application/octet-stream");
			put(".c",  "text/plain");
			put(".class",  "application/octet-stream");
			put(".conf",   "text/plain");
			put(".cpp",    "text/plain");
			put(".doc",    "application/msword");
			put(".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			put(".xls",    "application/vnd.ms-excel");
			put(".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			put(".exe",    "application/octet-stream");
			put(".gtar",   "application/x-gtar");
			put(".gz", "application/x-gzip");
			put(".h",  "text/plain");
			put(".htm",    "text/html");
			put(".html",   "text/html");
			put(".jar",    "application/java-archive");
			put(".java",   "text/plain");
			put(".js", "application/x-javascript");
			put(".log",    "text/plain");
			put(".m3u",    "audio/x-mpegurl");
			put(".m4a",    "audio/mp4a-latm");
			put(".m4b",    "audio/mp4a-latm");
			put(".m4p",    "audio/mp4a-latm");
			put(".m4u",    "video/vnd.mpegurl");
			put(".m4v",    "video/x-m4v");
			put(".mov",    "video/quicktime");
			put(".mp2",    "audio/x-mpeg");
			put(".mp3",    "audio/x-mpeg");
			put(".mp4",    "video/mp4");
			put(".mpc",    "application/vnd.mpohun.certificate");
			put(".mpe",    "video/mpeg");
			put(".mpeg",   "video/mpeg");
			put(".mpg",    "video/mpeg");
			put(".mpg4",   "video/mp4");
			put(".mpga",   "audio/mpeg");
			put(".msg",    "application/vnd.ms-outlook");
			put(".ogg",    "audio/ogg");
			put(".pdf",    "application/pdf");
			put(".pps",    "application/vnd.ms-powerpoint");
			put(".ppt",    "application/vnd.ms-powerpoint");
			put(".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation");
			put(".prop",   "text/plain");
			put(".properties",   "text/plain");
			put(".rc", "text/plain");
			put(".rmvb",   "video/*");
			put(".rtf",    "application/rtf");
			put(".sh", "text/plain");
			put(".tar",    "application/x-tar");
			put(".tgz",    "application/x-compressed");
			put(".txt",    "text/plain");
			put(".wav",    "audio/x-wav");
			put(".wma",    "video/*");
			put(".wmv",    "video/*");
			put(".wps",    "application/vnd.ms-works");
			put(".xml",    "text/plain");
			put(".z",  "application/x-compress");
			put(".zip",    "application/zip");
		}
	};

	private static String [] imageFiles = {
			".jpg",".png",".gif",".bmp",".jpeg"
	};

	public static boolean isImage(String fileName) {
		for (String image:imageFiles){
			if (fileName.toLowerCase().contains(image))
				return true;
		}
		return false;
	}

}
