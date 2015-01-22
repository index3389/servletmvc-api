package com.hqit.servletmvc.entry;

import java.io.InputStream;

public class MultipartBean
{
	//用户表单名称
	private String key;
	
	//用户上传的文件名
	private String fileName;
	
	//文件流
	private InputStream inputStream;
	
	public String getKey()
	{
		return key;
	}
	public void setKey(String key) 
	{
		this.key = key;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream)
	{
		this.inputStream = inputStream;
	}
}
