package com.hqit.servletmvc.api;

import java.util.Map;
import java.util.Set;

import com.hqit.servletmvc.entry.RequestMappingInfo;

/**
 * url分析
 * @author Administrator
 *
 */
public interface IHandlerAdapter
{
	
	public Map<String,RequestMappingInfo> urlAnalysis(Set<Class<?>> packageSets);
}
