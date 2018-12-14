package cn.hy.netfiletool.common;

import android.util.Log;
import cn.hy.netfiletool.box.App;
import cn.hy.netfiletool.pojo.FileMsg;

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
		File file = new File(App.path.getPath() + "/" + filePath);
		if (!file.exists()) {
			file.mkdirs();
		} else {
			int num = 1;
			while (!file.exists()) {
				file = new File(App.path.getPath() + "/" + "(" + num + ")"
						+ file.getName());
				num++;
			}
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
		File fl = new File(App.path.getPath() + "/" + filePath);
		try {
			if (!fl.exists()) {
				fl.createNewFile();
			} else {
				int num = 1;
				while (!fl.exists()) {
					fl = new File(App.path.getPath() + "/" + "(" + num + ")"
							+ filePath);
					num++;
				}
				fl.createNewFile();
			}
		} catch (IOException e) {
			Log.e("createFile", e.getMessage());
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
     * 根据FileMsg对象创建
     * 手机对应文件
     *
     * @param fi
     * @return
     * @throws IOException
     */
    public static File createFileByBaseFile(FileMsg fi,String pathName) throws IOException {
        File path = new File(pathName);//App.SdCardPath + App.DownLoadPath
        if (!path.exists()) {
            path.mkdirs();
        }
        File fl = new File(path.getPath() + "/" + fi.name);
        if (!fl.exists()) {
            fl.createNewFile();
        } else {
            int num = 1;
            while (fl.exists()) {
                fl = new File(path.getPath() + "/" + "(" + num + ")"
                        + fi.name);
                num++;
            }
            fl.createNewFile();
        }
        return fl;
    }

	private static String [] videoFiles = {".avi",".mp4",".flv"};

	public static boolean isVideo(String fileName){
		for (String video:videoFiles){
			if (fileName.contains(video))
				return true;
		}
		return false;
	}
}
