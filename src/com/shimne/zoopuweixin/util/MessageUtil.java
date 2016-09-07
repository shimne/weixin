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
	 * ������Ϣ���ͣ��ı�
	 */
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";

	/**
	 * ������Ϣ���ͣ�����
	 */
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

	/**
	 * ������Ϣ���ͣ�����
	 */
	public static final String RESP_MESSAGE_TYPE_VOICE = "voice";

	/**
	 * ������Ϣ���ͣ�ͼƬ
	 */
	public static final String RESP_MESSAGE_TYPE_IMAGE = "image";

	/**
	 * ������Ϣ���ͣ���Ƶ
	 */
	public static final String RESP_MESSAGE_TYPE_VIDEO = "video";

	/**
	 * ������Ϣ���ͣ�ͼ��
	 */
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";

	/**
	 * ������Ϣ���ͣ��ı�
	 */
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";

	/**
	 * ������Ϣ���ͣ�ͼƬ
	 */
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

	/**
	 * ������Ϣ���ͣ�����
	 */
	public static final String REQ_MESSAGE_TYPE_LINK = "link";

	/**
	 * ������Ϣ���ͣ�����λ��
	 */
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

	/**
	 * ������Ϣ���ͣ���Ƶ
	 */
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

	/**
	 * ������Ϣ���ͣ���Ƶ
	 */
	public static final String REQ_MESSAGE_TYPE_VIDEO = "video";

	/**
	 * ������Ϣ���ͣ�С��Ƶ
	 */
	public static final String REQ_MESSAGE_TYPE_SHORTVIDEO = "shortvideo";

	/**
	 * ������Ϣ���ͣ�����
	 */
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";

	/**
	 * �¼����ͣ�subscribe(����)
	 */
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

	/**
	 * �¼����ͣ�unsubscribe(ȡ������)
	 */
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

	/**
	 * �¼����ͣ�CLICK(�Զ���˵�����¼�)
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