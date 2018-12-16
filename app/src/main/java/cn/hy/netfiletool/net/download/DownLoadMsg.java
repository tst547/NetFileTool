package cn.hy.netfiletool.net.download;

import cn.hy.netfiletool.net.pojo.FileMsg;

import java.io.File;

/**
 * 下载信息
 * @author temp547
 *
 */
public class DownLoadMsg {

	public DownLoadMsg(){
	}

	private String msg;
	
	private long Id;
	
	private Progress progress;//进度条
	
	private File file;//下载文件手机文件
	
	private volatile boolean runFlag;//下载标志

	private FileMsg baseFile;//主机文件信息

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public Progress getProgress() {
		return progress;
	}

	public void setProgress(Progress progress) {
		this.progress = progress;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public boolean isRunFlag() {
		return runFlag;
	}

	public void setRunFlag(boolean runFlag) {
		this.runFlag = runFlag;
	}

	public FileMsg getBaseFile() {
		return baseFile;
	}

	public void setBaseFile(FileMsg baseFile) {
		this.baseFile = baseFile;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
