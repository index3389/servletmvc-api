package com.hqit.servletmvc.servlet;


import java.util.Set;

import com.hqit.servletmvc.analysis.GetClassFormPackage;
import com.hqit.servletmvc.api.IHandlerAdapter;
import com.hqit.servletmvc.api.impl.DefaultMethodHandlerAdapter;


public class DispatcherServlet extends FrameworkServlet 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void loadController(String packages) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		GetClassFormPackage getClass = new GetClassFormPackage();
		if(packages!=null)
		{
			IHandlerAdapter analysis = null;
			if(loadingStrategy != null)
			{
				analysis = (IHandlerAdapter)Class.forName(loadingStrategy).newInstance();
			}
			else
			{
				analysis = new DefaultMethodHandlerAdapter();
			}
			Set<Class<?>> sets = getClass.getClasses(packages);
			urlMaps = analysis.urlAnalysis(sets);
		}
	}
}
