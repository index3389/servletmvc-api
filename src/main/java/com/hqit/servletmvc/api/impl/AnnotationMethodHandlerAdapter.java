package com.hqit.servletmvc.api.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hqit.servletmvc.analysis.AnalysisUtils;
import com.hqit.servletmvc.annotation.Controller;
import com.hqit.servletmvc.annotation.Path;
import com.hqit.servletmvc.api.IHandlerAdapter;
import com.hqit.servletmvc.entry.RequestMappingInfo;

public class AnnotationMethodHandlerAdapter implements IHandlerAdapter 
{
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	public Map<String, RequestMappingInfo> urlAnalysis(Set<Class<?>> packageSets) 
	{
		return this.loadRequestUrl(packageSets);
	}
	
	public Map<String, RequestMappingInfo> loadRequestUrl(Set<Class<?>> clazzSets)
	{
		
		Map<String, RequestMappingInfo> urlMap = new HashMap<String, RequestMappingInfo>();
		
		Iterator<Class<?>> iter = clazzSets.iterator();
		
		AnalysisUtils urlUtil = new AnalysisUtils();
		
		while(iter.hasNext())
		{
			Class<?> clazz = iter.next();
			Controller controller = clazz.getAnnotation(Controller.class);
			Path path = clazz.getAnnotation(Path.class);
			
			//如果两个注解都没有，则视为无效url
			if(controller == null && path == null)
			{
				break;
			}
			
			String prefix = null;
			
			if(path != null)
			{
				prefix = urlUtil.getPrefix2(path);
				
			}
			else
			{
				prefix = "/";
			}
			
			int addCount = 0;
			
			try
			{
				
				Method[] methods = clazz.getMethods();
				
				for(Method m : methods)
				{
					RequestMappingInfo requestUrl = urlUtil.getRequestUrl2(m,prefix);
					if(requestUrl != null)
					{
						requestUrl.setDoClass(clazz);
						requestUrl.setReturnType(urlUtil.getReturnType(clazz));
						urlMap.put(requestUrl.getRequestUrl(), requestUrl);
						addCount++;
					}
				}
				
				if(addCount != urlMap.keySet().size())
				{
					//如果有两个相同的URL，则抛异常
					throw new IllegalStateException("Ambiguous handler url have the same");
				}
				
			}catch(Exception e)
			{
				logger.error(e);
				e.printStackTrace();
			}
		}
		return urlMap;
	}

}
