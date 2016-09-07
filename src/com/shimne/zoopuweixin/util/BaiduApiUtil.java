package com.shimne.zoopuweixin.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.shimne.zoopuweixin.common.PropertiesTool;

public class BaiduApiUtil
{
	public static String getResult(String apiUrl)
	{
		String appKey = PropertiesTool.properties.getProperty("baidu.appKey");
//		String appKey = "8e2b264533da5c58414da03df6f3ec38";

		BufferedReader reader = null;
		String result = "";
		StringBuffer sbf = new StringBuffer();

	    try
	    {
	    	URL url = new URL(apiUrl);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");

	        // ÃÓ»ÎapikeyµΩHTTP header
	        connection.setRequestProperty("apikey", appKey);
	        connection.connect();

	        InputStream is = connection.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        String strRead = null;

	        while ((strRead = reader.readLine()) != null)
	        {
	            sbf.append(strRead);
	            sbf.append("\r\n");
	        }

	        reader.close();
	        result = sbf.toString();
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	    finally
	    {
	    	try
	    	{
				reader.close();
			}
	    	catch (Exception e2)
	    	{
				e2.printStackTrace();
			}
	    }

	    return result;
	}
}