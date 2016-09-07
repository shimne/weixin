package com.shimne.zoopuweixin.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.shimne.util.ObjectUtil;

public class MessageUtil
{
	/**
	 * 返回消息类型：文本
	 */
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 返回消息类型：音乐
	 */
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

	/**
	 * 返回消息类型：语音
	 */
	public static final String RESP_MESSAGE_TYPE_VOICE = "voice";

	/**
	 * 返回消息类型：图片
	 */
	public static final String RESP_MESSAGE_TYPE_IMAGE = "image";

	/**
	 * 返回消息类型：视频
	 */
	public static final String RESP_MESSAGE_TYPE_VIDEO = "video";

	/**
	 * 返回消息类型：图文
	 */
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";

	/**
	 * 请求消息类型：文本
	 */
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 请求消息类型：图片
	 */
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

	/**
	 * 请求消息类型：链接
	 */
	public static final String REQ_MESSAGE_TYPE_LINK = "link";

	/**
	 * 请求消息类型：地理位置
	 */
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

	/**
	 * 请求消息类型：音频
	 */
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

	/**
	 * 请求消息类型：视频
	 */
	public static final String REQ_MESSAGE_TYPE_VIDEO = "video";

	/**
	 * 请求消息类型：小视频
	 */
	public static final String REQ_MESSAGE_TYPE_SHORTVIDEO = "shortvideo";

	/**
	 * 请求消息类型：推送
	 */
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";

	/**
	 * 事件类型：subscribe(订阅)
	 */
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

	/**
	 * 事件类型：unsubscribe(取消订阅)
	 */
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

	/**
	 * 事件类型：CLICK(自定义菜单点击事件)
	 */
	public static final String EVENT_TYPE_CLICK = "CLICK";

	@SuppressWarnings("unchecked")
	public static Map<String, String> xmlToMap(InputStream inputStream)
	{
		Map<String, String> map = null;

		try
		{
			map = new HashMap<String, String>();

			SAXReader reader = new SAXReader();
			Document document = reader.read(inputStream);

			Element root = document.getRootElement();
			List<Element> childElements = root.elements();

			if (ObjectUtil.notEmpty(childElements))
			{
				for (Element element : childElements)
				{
					map.put(element.getName(), element.getText());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				inputStream.close();
				inputStream = null;
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
			}
		}

		return map;
	}

	public static String mapToXml(Map<String, Object> map)
	{
		if (map.get("MsgType").equals(RESP_MESSAGE_TYPE_TEXT))
		{
			return textMapToXml(map);
		}
		else if (map.get("MsgType").equals(RESP_MESSAGE_TYPE_NEWS))
		{
			return newsMapToXml(map);
		}
		else
		{
			return otherMapToXml(map, map.get("MsgType").toString());
		}
	}

	private static String textMapToXml(Map<String, Object> map)
	{
		StringBuilder xmlBuilder = new StringBuilder("<xml>\r\n");

		for (String key : map.keySet())
		{
			xmlBuilder.append(getXmlString(key, map.get(key).toString()));
		}

		xmlBuilder.append("</xml>");

		return xmlBuilder.toString();
	}

	@SuppressWarnings("unchecked")
	private static String newsMapToXml(Map<String, Object> map)
	{
		StringBuilder xmlBuilder = new StringBuilder("<xml>\r\n");

		for (String key : map.keySet())
		{
			if (key.equals("Articles"))
			{
				List<Map<String, Object>> artiles = (List<Map<String, Object>>) map.get(key);

				xmlBuilder.append("<ArticleCount>" + artiles.size() + "</ArticleCount>\r\n");
				xmlBuilder.append("<Articles>\r\n");

				for (Map<String, Object> artile : artiles)
				{
					xmlBuilder.append("<item>\r\n");

					for (String articleKey : artile.keySet())
					{
						xmlBuilder.append(getXmlString(articleKey, artile.get(articleKey).toString()));
					}
					
					xmlBuilder.append("</item>\r\n");
				}
				
				xmlBuilder.append("</Articles>\r\n");
			}
			else
			{
				xmlBuilder.append(getXmlString(key, map.get(key).toString()));
			}
		}

		xmlBuilder.append("</xml>");

		return xmlBuilder.toString();
	}

	@SuppressWarnings("unchecked")
	private static String otherMapToXml(Map<String, Object> map, String msgType)
	{
		StringBuilder xmlBuilder = new StringBuilder("<xml>\r\n");

		for (String key : map.keySet())
		{
			if (key.equalsIgnoreCase(msgType))
			{
				xmlBuilder.append(key + "\r\n");

				Map<String, Object> imageMap = (Map<String, Object>) map.get(key);

				for (String imageKey : imageMap.keySet())
				{
					xmlBuilder.append(getXmlString(imageKey, imageMap.get(imageKey).toString()));
				}
				
				xmlBuilder.append(key + "\r\n");
			}
			else
			{
				xmlBuilder.append(getXmlString(key, map.get(key).toString()));
			}
		}

		xmlBuilder.append("</xml>");

		return xmlBuilder.toString();
	}

	private static String getXmlString(String tag, String text)
	{
		return "<" + tag + "><![CDATA[" + text + "]]></" + tag + ">\r\n";
	}

	public static String emoji(int codePoint)
	{
		return String.valueOf(Character.toChars(codePoint));
	}
}