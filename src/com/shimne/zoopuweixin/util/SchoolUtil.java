package com.shimne.zoopuweixin.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shimne.zoopuweixin.common.PropertiesTool;

public class SchoolUtil
{
	public static JSONObject getSchoolInfo(String school)
	{
		String schoolUrl = PropertiesTool.properties.getProperty("baidu.schoolUrl");
//		String schoolUrl = "http://apis.baidu.com/jidichong/school_search/school_search";

	    try
	    {
	    	schoolUrl += "?npage=1";
			schoolUrl += "&name=" + CharSetUtil.urlEncode(school, "UTF-8");

			String schoolInfo = BaiduApiUtil.getResult(schoolUrl);
			String result = JSONObject.parseObject(schoolInfo).getString("result"); 
			String data = JSONObject.parseObject(result).getString("data");
			JSONArray array = JSONObject.parseArray(data);

			if (array.size() > 0)
			{
				return array.getJSONObject(0);
			}
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }

	    return null;
	}
}