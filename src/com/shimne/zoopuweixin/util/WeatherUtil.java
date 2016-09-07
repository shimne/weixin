package com.shimne.zoopuweixin.util;

import com.alibaba.fastjson.JSONObject;
import com.shimne.zoopuweixin.common.PropertiesTool;

public class WeatherUtil
{
	public static JSONObject getWeatherInfo(String city)
	{
		String weatherlUrl = PropertiesTool.properties.getProperty("baidu.weatherUrl");
//		String weatherlUrl = "http://apis.baidu.com/heweather/weather/free";

	    try
	    {
	    	weatherlUrl += "?city=" + CharSetUtil.urlEncode(city, "UTF-8");

			String weatherInfo = BaiduApiUtil.getResult(weatherlUrl);
			JSONObject jsonObject = JSONObject.parseObject(weatherInfo);
			String data = jsonObject.getString("HeWeather data service 3.0");
			JSONObject dataObject = JSONObject.parseArray(data).getJSONObject(0);

			if (dataObject.getString("status").equals("ok"))
			{
				JSONObject returnObj = new JSONObject();

				JSONObject basicObject = JSONObject.parseObject(dataObject.getString("basic"));
				JSONObject nowObject = JSONObject.parseObject(dataObject.getString("now"));
				JSONObject aqiObject = JSONObject.parseObject(dataObject.getString("aqi"));

				JSONObject condObject = JSONObject.parseObject(nowObject.getString("cond"));
				JSONObject windObject = JSONObject.parseObject(nowObject.getString("wind"));

				JSONObject cityObject = JSONObject.parseObject(aqiObject.getString("city"));

				returnObj.putAll(basicObject);
				returnObj.putAll(condObject);
				returnObj.putAll(windObject);
				returnObj.putAll(cityObject);
				returnObj.putAll(nowObject);

				return returnObj;
			}
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }

	    return null;
	}
}