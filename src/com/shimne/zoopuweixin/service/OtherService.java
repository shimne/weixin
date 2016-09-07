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

			// ���ں�
			String toUserName = map.get("ToUserName");
			// ������
			String fromUserName = map.get("FromUserName");
			// �¼�
			String msgType = map.get("MsgType");
			// �¼�����
			String event = map.get("Event");

			returnMap.put("ToUserName", fromUserName);
			returnMap.put("FromUserName", toUserName);
			returnMap.put("CreateTime", System.currentTimeMillis() + "");
			returnMap.put("MsgType", MessageUtil.RESP_MESSAGE_TYPE_TEXT);
//			returnMap.put("Content", "�����쳣�����Ժ����ԣ�");

			// �ı���Ϣ
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT))
			{
				String text = map.get("Content");

				JSONObject jsonObject = SchoolUtil.getSchoolInfo(text);

				if (ObjectUtil.notNull(jsonObject) && StringUtil.notEmpty(jsonObject.getString("phone")))
				{
					returnMap.put("MsgType", MessageUtil.RESP_MESSAGE_TYPE_NEWS);
					List<Map<String, String>> list = new ArrayList<Map<String,String>>();

					String description = "�绰��" + jsonObject.getString("phone") + "\n";
					description += "��ַ��" + jsonObject.getString("city") + " " + jsonObject.getString("address") + "\n";
					description += "������" + jsonObject.getString("info");

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
						
						contentBuilder.append("���У�" + jsonObject.getString("cnty") + " " + jsonObject.getString("city")).append("\n");
						contentBuilder.append("��ǰ������").append(jsonObject.getString("txt")).append("\n");
						contentBuilder.append("��ǰ���£�").append(jsonObject.getString("tmp")).append("\n");
						contentBuilder.append("��ǰ������").append(jsonObject.getString("dir") + jsonObject.getString("sc")).append("\n");
						contentBuilder.append("��ǰ����������ָ�� ").append(jsonObject.getString("aqi")).append(" PM2.5 ").append(jsonObject.getString("pm25")).append("\n");

						returnMap.put("Content", contentBuilder.toString());
					}
					else if (text.contains("Ц��"))
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

//				if (text.startsWith("ѧУ"))
//				{
//					String school = text.replaceAll("ѧУ", "").replaceAll("\\s*", "");
//					JSONObject jsonObject = SchoolUtil.getSchoolInfo(school);
//
//					if (jsonObject == null)
//					{
//						returnMap.put("Content", school + "�����ڣ���������ȷѧУ���ƣ�");
//					}
//					else
//					{
//						returnMap.put("MsgType", MessageUtil.RESP_MESSAGE_TYPE_NEWS);
//						List<Map<String, String>> list = new ArrayList<Map<String,String>>();
//
//						String description = "�绰��" + jsonObject.getString("phone") + "\n";
//						description += "��ַ��" + jsonObject.getString("city") + " " + jsonObject.getString("address") + "\n";
//						description += "������" + jsonObject.getString("info");
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
//				else if (text.startsWith("����"))
//				{
//					String city = text.replaceAll("����", "").replaceAll("\\s*", "");
//					JSONObject jsonObject = WeatherUtil.getWeatherInfo(city);
//
//					if (jsonObject == null)
//					{
//						returnMap.put("Content", city + "�����ڣ���������ȷ���У�");
//					}
//					else
//					{
//						StringBuilder contentBuilder = new StringBuilder();
//
//						contentBuilder.append("���У�" + jsonObject.getString("cnty") + " " + jsonObject.getString("city")).append("\n");
//						contentBuilder.append("��ǰ������").append(jsonObject.getString("txt")).append("\n");
//						contentBuilder.append("��ǰ���£�").append(jsonObject.getString("tmp")).append("\n");
//						contentBuilder.append("��ǰ������").append(jsonObject.getString("dir") + jsonObject.getString("sc")).append("\n");
//						contentBuilder.append("��ǰ����������ָ�� ").append(jsonObject.getString("aqi")).append(" PM2.5 ").append(jsonObject.getString("pm25")).append("\n");
//
//						returnMap.put("Content", contentBuilder.toString());
//					}
//				}
//				else if (text.contains("Ц��"))
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
			// ������Ϣ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) 
			{
				returnMap.put("Content", "<a href=\"" + map.get("Url") + "\">" + map.get("Title") + "</a>");
			}
			// ͼƬ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) 
			{
				returnMap.put("MsgType", MessageUtil.RESP_MESSAGE_TYPE_IMAGE);
				Map<String, String> imageMap = new HashMap<String, String>();
				imageMap.put("MediaId", map.get("MediaId"));
				returnMap.put("Image", imageMap);
			}
			// ����λ��
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION))
			{
				returnMap.put("Content", "������Ϣ�ݲ�֧�֣�");
			}
			// ��Ƶ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE))
			{
				returnMap.put("MsgType", MessageUtil.RESP_MESSAGE_TYPE_VOICE);
				Map<String, String> voiceMap = new HashMap<String, String>();
				voiceMap.put("MediaId", map.get("MediaId"));
				returnMap.put("Voice", voiceMap);
			}
			// ��Ƶ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO) || msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_SHORTVIDEO))
			{
				returnMap.put("MsgType", MessageUtil.RESP_MESSAGE_TYPE_VIDEO);
				Map<String, String> videoMap = new HashMap<String, String>();
				videoMap.put("MediaId", map.get("MediaId"));
				videoMap.put("Title", "");
				videoMap.put("Description", "");
			}
			// �¼�
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT))
			{
				// ����
				if (event.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE))
				{
					StringBuilder contentBuilder = new StringBuilder();
					contentBuilder.append(MessageUtil.emoji(0x1F334)).append("��ӭ����ע��\n");
					contentBuilder.append("�����������²�����\n");
					contentBuilder.append("1�������ѧ���ƣ���ѯ��ѧ��Ϣ\n");
					contentBuilder.append("2������������ƣ���ѯ������Ϣ\n");
					contentBuilder.append("3������Ц��������һ��\n");
					contentBuilder.append("4�������˶Ի�\n");

					returnMap.put("Content", contentBuilder.toString());
				}
			}
			// ����Ϣ���ݲ�֧��
			else
			{
				returnMap.put("Content", "������Ϣ�ݲ�֧�֣�");
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
		map.put("Content", "ѧУ �廪��ѧ");
		map.put("MsgType", MessageUtil.REQ_MESSAGE_TYPE_TEXT);

		Map<String, Object> returnMap = new TreeMap<String, Object>().descendingMap();
		returnMap.put("ToUserName", "11111");
		returnMap.put("FromUserName", "22222");
		returnMap.put("CreateTime", System.currentTimeMillis() + "");
		returnMap.put("MsgType", MessageUtil.RESP_MESSAGE_TYPE_TEXT);

		String text = map.get("Content");

		if (text.startsWith("ѧУ"))
		{
			String school = text.replaceAll("ѧУ", "").replaceAll("\\s*", "");
			JSONObject jsonObject = SchoolUtil.getSchoolInfo(school);

			if (jsonObject == null)
			{
				returnMap.put("Content", text + "�����ڣ���������ȷѧУ���ƣ�");
			}
			else
			{
				returnMap.put("MsgType", MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				List<Map<String, String>> list = new ArrayList<Map<String,String>>();

				String description = "�绰��" + jsonObject.getString("phone") + "\n";
				description += "��ַ��" + jsonObject.getString("city") + " " + jsonObject.getString("address") + "\n";
				description += "������" + jsonObject.getString("info");

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
		map.put("Content", "���� ����");
		map.put("MsgType", MessageUtil.REQ_MESSAGE_TYPE_TEXT);

		Map<String, Object> returnMap = new TreeMap<String, Object>().descendingMap();
		returnMap.put("ToUserName", "11111");
		returnMap.put("FromUserName", "22222");
		returnMap.put("CreateTime", System.currentTimeMillis() + "");
		returnMap.put("MsgType", MessageUtil.RESP_MESSAGE_TYPE_TEXT);

		String text = map.get("Content");

		if (text.startsWith("����"))
		{
			String city = text.replaceAll("����", "").replaceAll("\\s*", "");
			JSONObject jsonObject = WeatherUtil.getWeatherInfo(city);

			if (jsonObject == null)
			{
				returnMap.put("Content", city + "�����ڣ���������ȷ���У�");
			}
			else
			{
				StringBuilder contentBuilder = new StringBuilder();

				contentBuilder.append("���У�" + jsonObject.getString("cnty") + " " + jsonObject.getString("city")).append("\n");
				contentBuilder.append("��ǰ������").append(jsonObject.getString("txt")).append("\n");
				contentBuilder.append("��ǰ���£�").append(jsonObject.getString("tmp")).append("\n");
				contentBuilder.append("��ǰ������").append(jsonObject.getString("dir") + jsonObject.getString("sc")).append("\n");
				contentBuilder.append("��ǰ����������ָ�� ").append(jsonObject.getString("aqi")).append(" PM2.5 ").append(jsonObject.getString("pm25")).append("\n");

				returnMap.put("Content", contentBuilder.toString());
			}
		}

		System.out.println(MessageUtil.mapToXml(returnMap));

	}
}