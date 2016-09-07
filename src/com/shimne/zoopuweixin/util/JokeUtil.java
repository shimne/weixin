package com.shimne.zoopuweixin.util;

import java.util.Random;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shimne.zoopuweixin.common.PropertiesTool;

public class JokeUtil
{
	public static JSONObject getJoke()
	{
		String jokeUrl = PropertiesTool.properties.getProperty("baidu.jokeUrl");
//		String jokeUrl = "http://apis.baidu.com/showapi_open_bus/showapi_joke/joke_text";
		
		int page = new Random().nextInt(50) + 1;

	    try
	    {
	    	jokeUrl += "?page=" + page;
			String result = BaiduApiUtil.getResult(jokeUrl);
			String body = JSONObject.parseObject(result).getString("showapi_res_body");
			JSONArray jokeArray = JSONObject.parseArray(JSONObject.parseObject(body).getString("contentlist"));

			int size = jokeArray.size();
			int no = new Random().nextInt(size) + 1;

			return jokeArray.getJSONObject(no);
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }

	    return null;
	}

	public static void main(String[] args) {
		System.out.println(getJoke().getString("text").replaceAll("<p>\\s*", "").replaceAll("</p>\\S*", "").replaceAll("\\t*", ""));
	}
}