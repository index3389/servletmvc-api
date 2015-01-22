package com.hqit.servletmvc.test;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
public class MethodName
{
	HttpServletRequest request;
	public void list(String path)
	{
		
	}
	@SuppressWarnings("rawtypes")
	public void addValue()throws Exception
	{
		Class clazz = MethodName.class;
		Method[] methods = clazz.getMethods();
		for(Method method : methods)
		{
			String name = method.getName();
			if(name.equals("list"))
			{
				/*
				 * 此处得到list方法的参数的类型（String）和参数名称(path)
				 */
				String path = null;
				String pathValue = request.getParameter(path);
				method.invoke(null,pathValue);
			}
		}
	}
}
