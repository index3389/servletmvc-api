package com.hqit.servletmvc.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hqit.servletmvc.analysis.MethodParameters;
import com.hqit.servletmvc.entry.RequestMappingInfo;
import com.hqit.servletmvc.support.NestedServletException;
import com.hqit.servletmvc.util.RequestUtils;

public abstract class FrameworkServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	protected static String projectName;							//项目名 
	
	protected static Map<String,RequestMappingInfo> urlMaps = new HashMap<String,RequestMappingInfo>();
	
	public static String defaultDoAction = "Both";					//默认get，post方式都可以提交
	
	protected static String suffix = "do";							//默认urlmapping
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private static final String packageValue = "base-package";		//要扫描的值
	
	private static final String loadingStrategyClass = "loadingStrategyClass";
	
	protected static String loadingStrategy = null;
	
	private static String packages = null;
	
	public void init()
	{
		projectName = this.getServletContext().getContextPath();
		
		packages = this.getInitParameter(packageValue);
		
		loadingStrategy = this.getInitParameter(loadingStrategyClass);
		
		if(StringUtils.isNotBlank(packages))
		{
			try 
			{
				loadController(packages);
			} catch (Exception e) 
			{
				e.printStackTrace();
			} 
		}
	}

	//公共调用
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException 
	{
		
		try
		{
			doService(request, response);
		}
		catch (ServletException ex) 
		{
			throw ex;
		}
		catch (IOException ex)
		{
			throw ex;
		}
		catch (Exception ex)
		{
			throw new NestedServletException("Request processing failed", ex);
		}
	}
	
	//真正处理的方法
	@SuppressWarnings("unchecked")
	protected void doService(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		String _requestUrl = request.getRequestURI();
		
		String requestUrl = RequestUtils.removeProjectName(projectName, _requestUrl);
		
		if(urlMaps.containsKey(requestUrl))
		{
			
			RequestMappingInfo requestObj = urlMaps.get(requestUrl);
			Object target = requestObj.getDoObject();
			Method method = requestObj.getMethod();
			String suffix = requestObj.getSuffix();
			
			String doMethod = request.getMethod();					//获取请求处理方式
			String mapMethod = requestObj.getRequestAction(); 		//实际的处理方式
			
			if(!RequestUtils.isSuffix(request.getRequestURI(), suffix))
			{
				response.sendError(404,_requestUrl);
				return;
			}
			
			if("GET".equalsIgnoreCase(doMethod) && "POST".equals(mapMethod))
			{
				send405(response,doMethod);
				return;
			}
			else if("POST".equalsIgnoreCase(doMethod) && "GET".equals(mapMethod))
			{
				send405(response,doMethod);
				return;
			}
			
			Object[] obj = MethodParameters.getParametersInfo(requestObj.getParameters(),request,response);
			Object result = null;
			if(!requestObj.getRequestUrl().equals("void"))
			{
				result = method.invoke(target,obj);//反射调用;
			}
			
			if(result != null)
			{
				String back = (String)result;
				if(RequestUtils.isRedirect(back))
				{
					back = projectName+RequestUtils.redirectUrl(back);
					response.sendRedirect(back);
				}
				else
				{
					request.getRequestDispatcher(back).forward(request,response);
				}
			}
			return;
		}
		else
		{
			response.sendError(404,_requestUrl);
			return;
		}
	}
	
	//加载Map对象
	public abstract void loadController(String packages)throws Exception;

	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException 
	{
		processRequest(req, resp);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException 
	{
		processRequest(req, resp);
	}
	
	public void send405(HttpServletResponse response,String doMethod) throws IOException
	{
		response.sendError(405,"HTTP method "+doMethod+" is not supported");
	}
}
