package com.shimne.zoopuweixin.util;

import com.alibaba.fastjson.JSONObject;
import com.shimne.zoopuweixin.common.PropertiesTool;

public class TuringUtil
{
	public static JSONObject getTuring(String content)
	{
		String turingUrl = PropertiesTool.properties.getProperty("baidu.turingUrl");
		String key = PropertiesTool.properties.getProperty("baidu.turingKey");
		String userId = PropertiesTool.properties.getProperty("baidu.turingUserId");
//		String key = "879a6cb3afb84dbf4fc84a1df2ab7319";
//		String userId = "eb2edb736";
//		String turingUrl = "http://apis.baidu.com/turing/turing/turing";

	    try
	    {
	    	turingUrl += "?key=" + key + "&userid=" + userId;
	    	turingUrl += "&info=" + CharSetUtil.urlEncode(content, "UTF-8");

			String turingInfo = BaiduApiUtil.getResult(turingUrl);
			return JSONObject.parseObject(turingInfo); 
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }

	    return null;
	}
}