package com.nercms.ryong21;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private String path="/sdcard/logZEF.txt"; 	//日志记录器的文件位置
	private String logText;						//日志文件内容
	private FileInputStream fis ;				//读取文件的流
	private DataInputStream dis;				//读取文字的流
	private FileOutputStream fos ;				//写入文件的流
	private DataOutputStream dos ;				//写入文字的流
	private File file;							//文件类
	/**
	 * 构造函数
	 */
	public Logger(){
		init();
	}
	/**
	 * 构造函数
	 * @param path 日志记录器的文件位置（自定义）
	 */
	public Logger(String path){
		this.path = path;
		init();
	}
	/**
	 * 初始化方法，在这个方法里读取了指定路径下的日志文件以及里面的文字，
	 * 如果没有该文件会自动创建。
	 */
	private void init(){
		try {
			file = new File(path);
			if (!file.exists()) {
				fos = new FileOutputStream(file);
				dos = new DataOutputStream(fos);
				dos.writeUTF("");
				fos.close();
				dos.close();
			}
			fis = new FileInputStream(file);
			dis = new DataInputStream(fis);
			logText=dis.readUTF();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally{
			try {
				fis.close();
				dis.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}
	/**
	 *
	 * @param log 需要写入日志的内容。
	 * @return	  返回一个真假值，返回真为写入成功，返回假为写入失败。
	 */
	public boolean writeLog(String log){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss E");
			String date= sdf.format(new Date());
			logText +="\n"+date+"\n"+log;
			fos = new FileOutputStream(file);
			dos = new DataOutputStream(fos);
			dos.writeUTF(logText);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}finally{
			try {
				fos.close();
				dos.close();
			} catch (Exception e2) {
				e2.printStackTrace();
				// TODO: handle exception
			}
		}
	}
	/**
	 * 获取日志上的内容
	 * @return 日志的内容，类型为String
	 */
	public String readLog(){
		return logText;
	}
}
