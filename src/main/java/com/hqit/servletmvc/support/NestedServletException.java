package com.hqit.servletmvc.support;

import javax.servlet.ServletException;

public class NestedServletException extends ServletException 
{

	private static final long serialVersionUID = -5292377985529381145L;
	
	public NestedServletException(String msg)
	{
		super(msg);
	}
	public NestedServletException(String msg, Throwable cause) 
	{
		super(msg, cause);
		if (getCause() == null && cause!=null)
		{
			initCause(cause);
		}
	}


	/**
	 * Return the detail message, including the message from the nested exception
	 * if there is one.
	 */
	@Override
	public String getMessage()
	{
		return buildMessage(super.getMessage(), getCause());
	}
	
	public static String buildMessage(String message, Throwable cause)
	{
		if (cause != null)
		{
			StringBuilder sb = new StringBuilder();
			if (message != null)
			{
				sb.append(message).append("; ");
			}
			sb.append("nested exception is ").append(cause);
			return sb.toString();
		}
		else 
		{
			return message;
		}
	}

}
