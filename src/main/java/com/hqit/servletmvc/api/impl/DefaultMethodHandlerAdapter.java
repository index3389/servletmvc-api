package com.hqit.servletmvc.api.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.hqit.servletmvc.analysis.AnalysisUtils;
import com.hqit.servletmvc.api.IHandlerAdapter;
import com.hqit.servletmvc.entry.RequestMappingInfo;

public class DefaultMethodHandlerAdapter implements IHandlerAdapter
{
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
			String prefix = urlUtil.getControllerName(clazz);
			try
			{
				if(prefix != null)
				{
					Method[] methods = clazz.getMethods();
					for(Method m : methods)
					{
						RequestMappingInfo requestUrl = urlUtil.getRequestUrl(m,prefix);
						if(requestUrl != null)
						{
							requestUrl.setDoClass(clazz);
							requestUrl.setReturnType(urlUtil.getReturnType(clazz));
							urlMap.put(requestUrl.getRequestUrl(), requestUrl);					//添加到urlMap
						}
					}
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return urlMap;
	}
}
