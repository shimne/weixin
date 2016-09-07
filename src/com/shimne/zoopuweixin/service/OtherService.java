package com.shimne.zoopuweixin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.shimne.util.ObjectUtil;
import com.shimne.util.StringUtil;
import com.shimne.zoopuweixin.util.JokeUtil;
import com.shimne.zoopuweixin.util.MessageUtil;
import com.shimne.zoopuweixin.util.SchoolUtil;
import com.shimne.zoopuweixin.util.TuringUtil;
import com.shimne.zoopuweixin.util.WeatherUtil;

public class OtherService
{
	public static String process(HttpServletRequest request)
	{
		Map<String, Object> returnMap = null;

		try
		{
			returnMap = new TreeMap<String, Object>().descendingMap();
			Map<String, String> map = MessageUtil.xmlToMap(request.getInputStream());

			// 公众号
			String toUserName = map.get("ToUserName");
			// 发送人
			String fromUserName = map.get("FromUserName");
			// 事件
			String msgType = map.get("MsgType");
			// 事件类型
			String event = map.get("Event");

			returnMap.put("ToUserName", fromUserName);
			returnMap.put("FromUserName", toUserName);
			returnMap.put("CreateTime", System.currentTimeMillis() + "");
			returnMap.put("MsgType", MessageUtil.RESP_MESSAGE_TYPE_TEXT);
//			returnMap.put("Content", "请求异常，请稍后重试！");

			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT))
			{
				String text = map.get("Content");

				JSONObject jsonObject = SchoolUtil.getSchoolInfo(text);

				if (ObjectUtil.notNull(jsonObject) && StringUtil.notEmpty(jsonObject.getString("phone")))
				{
					returnMap.put("MsgType", MessageUtil.RESP_MESSAGE_TYPE_NEWS);
					List<Map<String, String>> list = new ArrayList<Map<String,String>>();

					String description = "电话：" + jsonObject.getString("phone") + "\n";
					description += "地址：" + jsonObject.getString("city") + " " + jsonObject.getString("address") + "\n";
					description += "其它：" + jsonObject.getString("info");

					Map<String, String> newMap = new TreeMap<String, String>().descendingMap();
					newMap.put("Title", text);
					newMap.put("Description", description);
					newMap.put("PicUrl", jsonObject.getString("img"));
					newMap.put("Url", jsonObject.getString("website"));

					list.add(newMap);

					returnMap.put("Articles", list);
				}
				else
				{
					jsonObject = WeatherUtil.getWeatherInfo(text);

					if (ObjectUtil.notNull(jsonObject) && StringUtil.notEmpty(jsonObject.getString("city")))
					{
						StringBuilder contentBuilder = new StringBuilder();
						
						contentBuilder.append("城市：" + jsonObject.getString("cnty") + " " + jsonObject.getString("city")).append("\n");
						contentBuilder.append("当前天气：").append(jsonObject.getString("txt")).append("\n");
						contentBuilder.append("当前气温：").append(jsonObject.getString("tmp")).append("\n");
						contentBuilder.append("当前风力：").append(jsonObject.getString("dir") + jsonObject.getString("sc")).append("\n");
						contentBuilder.append("当前空气：质量指数 ").append(jsonObject.getString("aqi")).append(" PM2.5 ").append(jsonObject.getString("pm25")).append("\n");

						returnMap.put("Content", contentBuilder.toString());
					}
					else if (text.contains("笑话"))
					{
						jsonObject = JokeUtil.getJoke();
						
						String content = jsonObject.getString("text").replaceAll("<p>\\s*", "").replaceAll("</p>\\S*", "").replaceAll("\\t*", "");
						returnMap.put("Content", content);
					}
					else
					{
						returnMap.put("Content", TuringUtil.getTuring(text).getString("text"));
					}
				}

//				if (text.startsWith("学校"))
//				{
//					String school = text.replaceAll("学校", "").replaceAll("\\s*", "");
//					JSONObject jsonObject = SchoolUtil.getSchoolInfo(school);
//
//					if (jsonObject == null)
//					{
//						returnMap.put("Content", school + "不存在，请输入正确学校名称！");
//					}
//					else
//					{
//						returnMap.put("MsgType", MessageUtil.RESP_MESSAGE_TYPE_NEWS);
//						List<Map<String, String>> list = new ArrayList<Map<String,String>>();
//
//						String description = "电话：" + jsonObject.getString("phone") + "\n";
//						description += "地址：" + jsonObject.getString("city") + " " + jsonObject.getString("address") + "\n";
//						description += "其它：" + jsonObject.getString("info");
//
//						Map<String, String> newMap = new TreeMap<String, String>().descendingMap();
//						newMap.put("Title", school);
//						newMap.put("Description", description);
//						newMap.put("PicUrl", jsonObject.getString("img"));
//						newMap.put("Url", jsonObject.getString("website"));
//
//						list.add(newMap);
//
//						returnMap.put("Articles", list);
//					}
//				}
//				else if (text.startsWith("天气"))
//				{
//					String city = text.replaceAll("天气", "").replaceAll("\\s*", "");
//					JSONObject jsonObject = WeatherUtil.getWeatherInfo(city);
//
//					if (jsonObject == null)
//					{
//						returnMap.put("Content", city + "不存在，请输入正确城市！");
//					}
//					else
//					{
//						StringBuilder contentBuilder = new StringBuilder();
//
//						contentBuilder.append("城市：" + jsonObject.getString("cnty") + " " + jsonObject.getString("city")).append("\n");
//						contentBuilder.append("当前天气：").append(jsonObject.getString("txt")).append("\n");
//						contentBuilder.append("当前气温：").append(jsonObject.getString("tmp")).append("\n");
//						contentBuilder.append("当前风力：").append(jsonObject.getString("dir") + jsonObject.getString("sc")).append("\n");
//						contentBuilder.append("当前空气：质量指数 ").append(jsonObject.getString("aqi")).append(" PM2.5 ").append(jsonObject.getString("pm25")).append("\n");
//
//						returnMap.put("Content", contentBuilder.toString());
//					}
//				}
//				else if (text.contains("笑话"))
//				{
//					JSONObject jsonObject = JokeUtil.getJoke();
//
//					String content = jsonObject.getString("text").replaceAll("<p>\\s*", "").replaceAll("</p>\\S*", "").replaceAll("\\t*", "");
//					returnMap.put("Content", content);
//				}
//				else
//				{
//					returnMap.put("Content", TuringUtil.getTuring(text).getString("text"));
//				}
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) 
			{
				returnMap.put("Content", "<a href=\"" + map.get("Url") + "\">" + map.get("Title") + "</a>");
			}
			// 图片
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) 
			{
				returnMap.put("MsgType", MessageUtil.RESP_MESSAGE_TYPE_IMAGE);
				Map<String, String> imageMap = new HashMap<String, String>();
				imageMap.put("MediaId", map.get("MediaId"));
				returnMap.put("Image", imageMap);
			}
			// 地理位置
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION))
			{
				returnMap.put("Content", "请求信息暂不支持！");
			}
			// 音频
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE))
			{
				returnMap.put("MsgType", MessageUtil.RESP_MESSAGE_TYPE_VOICE);
				Map<String, String> voiceMap = new HashMap<String, String>();
				voiceMap.put("MediaId", map.get("MediaId"));
				returnMap.put("Voice", voiceMap);
			}
			// 视频
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO) || msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_SHORTVIDEO))
			{
				returnMap.put("MsgType", MessageUtil.RESP_MESSAGE_TYPE_VIDEO);
				Map<String, String> videoMap = new HashMap<String, String>();
				videoMap.put("MediaId", map.get("MediaId"));
				videoMap.put("Title", "");
				videoMap.put("Description", "");
			}
			// 事件
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT))
			{
				// 订阅
				if (event.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE))
				{
					StringBuilder contentBuilder = new StringBuilder();
					contentBuilder.append(MessageUtil.emoji(0x1F334)).append("欢迎您关注！\n");
					contentBuilder.append("您可以做如下操作！\n");
					contentBuilder.append("1、输入大学名称，查询大学信息\n");
					contentBuilder.append("2、输入城市名称，查询天气信息\n");
					contentBuilder.append("3、输入笑话，欢乐一刻\n");
					contentBuilder.append("4、机器人对话\n");

					returnMap.put("Content", contentBuilder.toString());
				}
			}
			// 其消息件暂不支持
			else
			{
				returnMap.put("Content", "请求信息暂不支持！");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return MessageUtil.mapToXml(returnMap);
	}

	public static void main(String[] args)
	{
		/*Map<String, String> map = new HashMap<String, String>();
		map.put("Content", "学校 清华大学");
		map.put("MsgType", MessageUtil.REQ_MESSAGE_TYPE_TEXT);

		Map<String, Object> returnMap = new TreeMap<String, Object>().descendingMap();
		returnMap.put("ToUserName", "11111");
		returnMap.put("FromUserName", "22222");
		returnMap.put("CreateTime", System.currentTimeMillis() + "");
		returnMap.put("MsgType", MessageUtil.RESP_MESSAGE_TYPE_TEXT);

		String text = map.get("Content");

		if (text.startsWith("学校"))
		{
			String school = text.replaceAll("学校", "").replaceAll("\\s*", "");
			JSONObject jsonObject = SchoolUtil.getSchoolInfo(school);

			if (jsonObject == null)
			{
				returnMap.put("Content", text + "不存在，请输入正确学校名称！");
			}
			else
			{
				returnMap.put("MsgType", MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				List<Map<String, String>> list = new ArrayList<Map<String,String>>();

				String description = "电话：" + jsonObject.getString("phone") + "\n";
				description += "地址：" + jsonObject.getString("city") + " " + jsonObject.getString("address") + "\n";
				description += "其它：" + jsonObject.getString("info");

				Map<String, String> newMap = new TreeMap<String, String>().descendingMap();
				newMap.put("Title", school);
				newMap.put("Description", description);
				newMap.put("PicUrl", jsonObject.getString("img"));
				newMap.put("Url", jsonObject.getString("website"));

				list.add(newMap);

				returnMap.put("Articles", list);
			}
		}

		System.out.println(MessageUtil.mapToXml(returnMap));*/

		Map<String, String> map = new HashMap<String, String>();
		map.put("Content", "天气 日照");
		map.put("MsgType", MessageUtil.REQ_MESSAGE_TYPE_TEXT);

		Map<String, Object> returnMap = new TreeMap<String, Object>().descendingMap();
		returnMap.put("ToUserName", "11111");
		returnMap.put("FromUserName", "22222");
		returnMap.put("CreateTime", System.currentTimeMillis() + "");
		returnMap.put("MsgType", MessageUtil.RESP_MESSAGE_TYPE_TEXT);

		String text = map.get("Content");

		if (text.startsWith("天气"))
		{
			String city = text.replaceAll("天气", "").replaceAll("\\s*", "");
			JSONObject jsonObject = WeatherUtil.getWeatherInfo(city);

			if (jsonObject == null)
			{
				returnMap.put("Content", city + "不存在，请输入正确城市！");
			}
			else
			{
				StringBuilder contentBuilder = new StringBuilder();

				contentBuilder.append("城市：" + jsonObject.getString("cnty") + " " + jsonObject.getString("city")).append("\n");
				contentBuilder.append("当前天气：").append(jsonObject.getString("txt")).append("\n");
				contentBuilder.append("当前气温：").append(jsonObject.getString("tmp")).append("\n");
				contentBuilder.append("当前风力：").append(jsonObject.getString("dir") + jsonObject.getString("sc")).append("\n");
				contentBuilder.append("当前空气：质量指数 ").append(jsonObject.getString("aqi")).append(" PM2.5 ").append(jsonObject.getString("pm25")).append("\n");

				returnMap.put("Content", contentBuilder.toString());
			}
		}

		System.out.println(MessageUtil.mapToXml(returnMap));

	}
}