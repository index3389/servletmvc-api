package com.hqit.servletmvc.multipart;

import com.hqit.servletmvc.entry.MultipartBean;

/*
 * 上次处理对象
 */
public interface MultipartHandler 
{
	public MultipartBean getMultipartBean(String key)throws Exception;
	
}
