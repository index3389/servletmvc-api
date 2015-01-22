package com.hqit.servletmvc.util;

public class StringUtils
{
	//判断字符串是否为空
	public static boolean isEmpty(String str)
	{
		if(str == null)
		{
			return true;
		}
		if("".equals(str) || str.length()==0)
		{
			return true;
		}
		return false;
	}
	

	//首字母小写
	public static String toLowerCaseFirstOne(String s)  
	{  
         if(Character.isLowerCase(s.charAt(0)))  
         {
        	 return s;
         }  
         else  
         {
        	 return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
         }  
	}
	
	//首字母大写
	public static String toUpperCaseFirstOne(String s)   
    {   
        if(Character.isUpperCase(s.charAt(0)))   
        {
        	 return s; 
        } 
        else  
        {
        	return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }   
    }
	
}
