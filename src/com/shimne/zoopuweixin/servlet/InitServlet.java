package com.shimne.zoopuweixin.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shimne.util.StringUtil;
import com.shimne.weixin.thread.TokenThread;
import com.shimne.zoopuweixin.common.PropertiesTool;

public class InitServlet extends HttpServlet
{
	private static final long serialVersionUID = -1665498459601040062L;

	private static Logger logger = LoggerFactory.getLogger(InitServlet.class);

	public void init(ServletConfig config)
	{
		
		String filePath = config.getServletContext().getRealPath("/") + "WEB-INF/classes/weixin.properties";
//		String filePath = "";
		PropertiesTool propertiesTool = new PropertiesTool();
		propertiesTool.init(filePath);

		String appId = PropertiesTool.properties.getProperty("weixin.appId");
		String appSecret = PropertiesTool.properties.getProperty("weixin.appSecret");
		String accessTokenUrl = PropertiesTool.properties.getProperty("weixin.accessTokenUrl");

		accessTokenUrl.replaceAll("APPID", appId).replaceAll("APPSECRET", appSecret);

		logger.info("weixin api appId: " + appId);  
		logger.info("weixin api appSecret: " + appSecret);  
  
        if (StringUtil.isEmpty(appId) || StringUtil.isEmpty(appSecret))
        {
        	logger.error("appId and appSecret configuration error, please check carefully.");  
        }
        else
        {
        	TokenThread.accessTokenUrl = accessTokenUrl;

            new Thread(new TokenThread()).start(); 
        }  
    }
}