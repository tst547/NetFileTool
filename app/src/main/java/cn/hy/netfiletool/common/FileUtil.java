package cn.hy.netfiletool.common;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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

	private static String [] videoFiles = {
			".avi",".mp4",".flv",".wmv",".mov",".rmvb",".rm",".mkv",".amv"
	};

	public static boolean isVideo(String fileName){
		for (String video:videoFiles){
			if (fileName.toLowerCase().contains(video))
				return true;
		}
		return false;
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
}
