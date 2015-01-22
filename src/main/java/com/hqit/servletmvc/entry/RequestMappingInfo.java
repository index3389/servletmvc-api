package com.hqit.servletmvc.entry;

import java.lang.reflect.Method;

@SuppressWarnings("rawtypes")
public class RequestMappingInfo 
{
	private String requestUrl;			  //请求url(不带项目名)
	private Method method;				  //请求处理的方法
	private String requestAction = "GET"; //请求方式
	private String suffix;				  //后缀
	
	private String returnType;			  //返回类型
	
	private Class doClass;				  //请求处理的类
	
	private Class[] parameters;   		  //方法参数
	
	public Method getMethod()
	{
		return method;
	}

	public void setMethod(Method method) 
	{
		this.method = method;
	}
	public String getRequestUrl()
	{
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) 
	{
		this.requestUrl = requestUrl;
	}

	public String getRequestAction()
	{
		return requestAction;
	}

	public void setRequestAction(String requestAction) {
		this.requestAction = requestAction;
	}

	public Class getDoClass() 
	{
		return doClass;
	}

	public void setDoClass(Class doClass) 
	{
		this.doClass = doClass;
	}

	
	//目标对象
	public Object getDoObject() 
	{
		try 
		{
			return doClass.newInstance();
			
		} catch (InstantiationException e) 
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	//参数
	public Class[] getParameters()
	{
		return parameters;
	}

	public void setParameters(Class[] parameters)
	{
		this.parameters = parameters;
	}

	public String getSuffix()
	{
		return suffix;
	}

	public void setSuffix(String suffix) 
	{
		this.suffix = suffix;
	}

	public String getReturnType()
	{
		return returnType;
	}

	public void setReturnType(String returnType)
	{
		this.returnType = returnType;
	}
	
}
