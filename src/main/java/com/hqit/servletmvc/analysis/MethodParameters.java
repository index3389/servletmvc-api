package com.hqit.servletmvc.analysis;

import java.lang.reflect.Array;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hqit.servletmvc.multipart.MultipartHandler;
import com.hqit.servletmvc.multipart.MultipartHandlerImpl;

public class MethodParameters 
{
	/**
	 * 设置参数
	 * @return
	 */
	public static Object[] getParametersInfo(final Class<Object>[] parameters,HttpServletRequest request,HttpServletResponse response)
	{
		if(parameters.length<1)
		{
			return null;
		}
		
		Object[] params = new Object[parameters.length];
		
		int index = 0;
		
		for(Class<Object> clazz : parameters)
		{
			if(clazz.isAssignableFrom(HttpServletRequest.class))
			{
				Array.set(params, index, request);
			}
			else if(clazz.isAssignableFrom(HttpServletResponse.class))
			{
				Array.set(params, index, response);
			}
			else if(clazz.isAssignableFrom(HttpSession.class))
			{
				Array.set(params, index, request.getSession());
			}
			else if(clazz.isAssignableFrom(ServletContext.class))
			{
				Array.set(params, index, request.getSession().getServletContext());
			}
			else if(clazz.isAssignableFrom(MultipartHandler.class))
			{
				Array.set(params, index, new MultipartHandlerImpl(request));
			}
			index++;
		}
		return params;
	}
}
