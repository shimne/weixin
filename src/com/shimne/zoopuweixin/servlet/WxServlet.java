package com.shimne.zoopuweixin.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shimne.weixin.util.SignUtil;
import com.shimne.zoopuweixin.common.PropertiesTool;
import com.shimne.zoopuweixin.service.OtherService;

public class WxServlet extends HttpServlet
{
	private static final long serialVersionUID = 2556272477036647693L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String token = PropertiesTool.properties.getProperty("weixin.token");
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");

		PrintWriter out = response.getWriter();
		
		if (SignUtil.checkSignature(token, signature, timestamp, nonce, echostr))
		{
			out.print(echostr);
		}

		out.close();
		out = null;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String text = OtherService.process(request);

		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		PrintWriter out = response.getWriter();
		out.print(text);
		out.close();
		out = null;
	}
}