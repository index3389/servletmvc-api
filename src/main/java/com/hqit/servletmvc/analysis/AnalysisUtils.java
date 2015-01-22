package com.hqit.servletmvc.analysis;
import java.lang.reflect.Method;

import com.hqit.servletmvc.annotation.Get;
import com.hqit.servletmvc.annotation.Path;
import com.hqit.servletmvc.annotation.Post;
import com.hqit.servletmvc.entry.RequestMappingInfo;
import com.hqit.servletmvc.servlet.FrameworkServlet;
import com.hqit.servletmvc.util.StringUtils;

public class AnalysisUtils
{
	/**
	 * 得到控制器的访问url
	 * BookController
	 * 得到的结果就是 /book/(首字母小写)
	 */
	public String getControllerName(Class<?> clazz)
	{
		String clazzName = clazz.getSimpleName();
		int len = clazzName.length();
		if(len>10)
		{
			String fix = clazzName.substring(0,len-10);
			fix = StringUtils.toLowerCaseFirstOne(fix);
			if(!fix.startsWith("/"))
			{
				fix = "/"+fix;
			}
			if(!fix.endsWith("/"))
			{
				fix = fix+"/";
			}
			return fix;
		}
		return null;
	}
	
	/*
	 * 把字符串该成 /xxx/ 的形式
	 */
	public String getPrefix(Path p)
	{
		String fix = p.value();
		if(!fix.startsWith("/"))
		{
			fix = "/"+fix;
		}
		if(!fix.endsWith("/"))
		{
			fix = fix+"/";
		}
		return fix;
	}
	
	/*
	 * 把字符串该成 /xxx 的形式
	 */
	public String getPrefix2(Path p)
	{
		String fix = p.value();
		if(!fix.startsWith("/"))
		{
			fix = "/"+fix;
		}
		if(fix.length()>1 && fix.endsWith("/"))
		{
			fix = fix.substring(0,fix.length()-1);
		}
		return fix;
	}
	/*
	 * 把形如xxxAction改成xxx
	 */
	public String getMethodAction(String methodName)
	{
		if(methodName.indexOf("Action") != -1)
		{
			int len = methodName.length();
			if(len > 6)
			{
				return  methodName.substring(0,len-6);
			}
		}
		return null;
	}
	
	public String getMethodActionByPath(Method method)
	{
		Path path = method.getAnnotation(Path.class);
		if(path != null)
		{
			String v = path.value();
			if(v.indexOf('.')>0)
			{
				v = v.substring(0,v.lastIndexOf('.'));
			}
			if(!v.startsWith("/"))
			{
				v = "/"+v;
			}
			return v;
		}
		return null;
	}
	
	public String getSuffixByPath(Method method)
	{
		Path path = method.getAnnotation(Path.class);
		if(path != null)
		{
			String v = path.value();
			if(v.lastIndexOf('.')>0)
			{
				v = v.substring(v.lastIndexOf('.')+1);
				return v;
			}
		}
		return null;
	}
	
	public RequestMappingInfo getRequestUrl(Method method,String prefix)
	{
		
		String doAction = getDoAction(method);//提交方式
		
		String methodName = method.getName();
		String action = getMethodAction(methodName);
		
		if(action != null)
		{
			String url = prefix + action;
			RequestMappingInfo requestUrl = new RequestMappingInfo();
			
			requestUrl.setRequestUrl(url);
			requestUrl.setSuffix(getSuffixByPath(method));
			requestUrl.setRequestAction(doAction);
			requestUrl.setMethod(method);
			requestUrl.setParameters(method.getParameterTypes());
			
			return requestUrl;
		}
		
		return null;
	}
	
	public RequestMappingInfo getRequestUrl2(Method method,String prefix)
	{
		
		String doAction = getDoAction(method);//提交方式
		
		String action = getMethodActionByPath(method);
		
		if(action != null)
		{
			String url = prefix + action;
			
			if(prefix.endsWith("/") && action.startsWith("/"))
			{
				url = prefix + action.substring(1);
			}
			
			RequestMappingInfo requestUrl = new RequestMappingInfo();
			
			requestUrl.setRequestUrl(url);
			requestUrl.setSuffix(getSuffixByPath(method));
			requestUrl.setRequestAction(doAction);
			requestUrl.setMethod(method);
			requestUrl.setParameters(method.getParameterTypes());
			
			return requestUrl;
		}
		
		return null;
	}
	
	//获取处理方式（get，post）
	public String getDoAction(Method method)
	{
		if(method != null)
		{
			Get get = method.getAnnotation(Get.class);
			if(get != null)
			{
				return "GET";
			}
			
			Post post = method.getAnnotation(Post.class);
			if(post != null)
			{
				return "POST";
			}
		}
		return FrameworkServlet.defaultDoAction;
	}
	
	public String getReturnType(Class<?> clazz)
	{
		return clazz.getName();
	}
}

