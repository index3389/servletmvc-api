package com.hqit.servletmvc.multipart;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import com.hqit.servletmvc.entry.MultipartBean;
import com.hqit.servletmvc.util.StringUtils;

/**
 * @上传
 *
 */
public class MultipartHandlerImpl implements MultipartHandler
{
	private HttpServletRequest request;
	
	/**上传文件流*/
	private ServletInputStream input;
	
	/**分界符*/
	private String boundary = null;
	
	/**分界符 结束*/
	private String lastBoundary = null;
	
	/**临时上传目录*/
	private String tempDir = null;
	
	private Hashtable<String,MultipartBean> fileMap = new Hashtable<String,MultipartBean>();
	
	public MultipartHandlerImpl(HttpServletRequest request)
	{
		this.request = request;
		String contentType = request.getContentType();
		this.boundary = this.getBoundary(contentType);
		this.lastBoundary = boundary+"--";
		
		tempDir = System.getProperty("java.io.tmpdir")+"/tempfile";
		
		if(!new File(tempDir).exists())
		{
			new File(tempDir).mkdirs();
		}
		
		try 
		{
			init();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void  init() throws IOException
	{
		//获得输入流
		input = request.getInputStream();
	    
		//临时字节数组
		byte[] by = new byte[1024];
		
		//读取字节数
		int temp=0;
		
		//读取空行
		temp = input.readLine(by, 0, by.length);
		
		while((temp = input.readLine(by, 0, by.length)) != -1)
		{
			String content = new String(by,0,temp).trim();
			
			if(content.equals(this.lastBoundary))
			{
				break;
			}
			
			if(content.indexOf("filename=") >0)
			{
				String key = getKey(content);
				
				if(!StringUtils.isEmpty(key))
				{
					
					String name = getFileNameByKey(key, content).trim();
					
					if(!StringUtils.isEmpty(name))
					{
						MultipartBean obj = new MultipartBean();
						obj.setKey(key);
						obj.setFileName(name);
						
						String filename = "";
						
						if(tempDir.endsWith(File.separator))
						{
							filename = tempDir + name;
						}
						else
						{
							filename = tempDir + File.separator + name;
						}
						
						//读取空行
						temp = input.readLine(by, 0, by.length);
						temp = input.readLine(by, 0, by.length);
						
						FileOutputStream out = new FileOutputStream(filename);
						
						while((temp = input.readLine(by, 0, by.length)) != -1)
						{
							String subContent = new String(by,0,temp).trim();
							if(this.lastBoundary.equals(subContent) || this.boundary.equals(subContent))
							{
								break;
							}
							out.write(by,0,temp);
						}
						
						obj.setInputStream(new FileInputStream(filename));
						
						out.close();
						
						fileMap.put(key,obj);
					}
					
				}
			}
		}
		
		input.close();
	}
	
	public MultipartBean getMultipartBean(String key)
	{
		return fileMap.get(key);
	}
	
	//得到key
	public String getKey(String content)
	{
		String reg = "name=\"(.*)\";";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(content);
		
		String name = "";
		
		while(m.find())
		{
			name = m.group();
			break;
		}

		int len = name.length();
		
		return name.substring(6,len-2);
	}
	
	//根据KEY得到文件名
	public String getFileNameByKey(String key,String content)
	{
		if(content.indexOf(key)>0)
		{
			int start = content.lastIndexOf("filename=");
			int len = content.length();
			return content.substring(start+10,len-1);
		}
		return null;
	}

	private String getBoundary(String contentType)
	{
		int index = contentType.indexOf("boundary=");
		return "--"+contentType.substring(index+"boundary=".length());
	}
	
}
