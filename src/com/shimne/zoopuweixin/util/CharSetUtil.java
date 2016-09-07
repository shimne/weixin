package com.shimne.zoopuweixin.util;

import java.net.URLEncoder;

import com.shimne.util.StringUtil;

public class CharSetUtil
{
	public static String urlEncode(String source, String charSet)
	{  
        String result = source;

        if (StringUtil.isEmpty(charSet))
        {
        	charSet = "UTF-8";
        }

        try
        {  
            result = URLEncoder.encode(source, charSet);  
        }
        catch (Exception e)
        {  
            e.printStackTrace();  
        } 

        return result;  
    }
}
