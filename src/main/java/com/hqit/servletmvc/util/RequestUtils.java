package com.hqit.servletmvc.util;


public class RequestUtils 
{

	//是否是重定向
	public static boolean isRedirect(String url)
	{
		if(!StringUtils.isEmpty(url))
		{
			if(url.startsWith("redirect:"))
			{
				return true;
			}
		}
		return false;
	}
	
	//如果是重定向，则取得重定向的url
	public static String redirectUrl(String url)
	{
		if(isRedirect(url))
		{
			return url.substring(9,url.length());
		}
		return url;
	}
	
	//得到后缀
	public static String getSuffix(String suffix)
	{
		int len = suffix.lastIndexOf('.');
		if(len>0)
		{
			return suffix.substring(len+1);
		}
		return null;
	}
	
	//判断后缀是否相同
	public static boolean isSuffix(String requestUrl,String suffix)
	{
		if(StringUtils.isEmpty(suffix))
		{
			return true;
		}
		String _suffix = getSuffix(requestUrl);
		if(_suffix.equals(suffix))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 却掉项目名和后缀
	 * 比如说 /game/admin/list.do(/game是项目名)
	 * 结果就是/admin/list
	 */
	public static String removeProjectName(String projectName,String url)
	{
		int location = url.lastIndexOf(".");
		if(location > -1)
		{
			url = url.substring(0,location);
			if(!projectName.equals(""))
			{
				url = url.substring(projectName.length(),url.length());
			}
		}
		return url;
	}
}
