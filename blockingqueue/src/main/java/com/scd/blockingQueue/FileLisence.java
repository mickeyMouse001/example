package com.scd.blockingQueue;

import java.io.File;
import java.util.Observable;
/**
 * 被订阅者（监听文件修改时间）
 * @author mickeyMouse001
 * @data 2017-09-04
 */
public class FileLisence extends Observable{

	private File file;
	private long lastModifyTime;
	

	public long checkFile(File file){
		if(file.lastModified()!=lastModifyTime){
			setChanged();
			notifyObservers();
			this.lastModifyTime=file.lastModified();
		}
		return lastModifyTime;
	}
}