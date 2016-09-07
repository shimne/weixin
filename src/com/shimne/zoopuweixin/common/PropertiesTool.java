package com.shimne.zoopuweixin.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesTool
{
	private static Logger log = LoggerFactory.getLogger(PropertiesTool.class);

	public static Properties properties;

	public void init(String filePath)
	{
		log.info("初始化相关属性开始。");

		properties = new Properties();
		File file = new File(filePath);

		if (file.isFile())
		{
			InputStream is = null;

			try
			{
				is = new BufferedInputStream(new FileInputStream(file));

				properties.load(is);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if (is != null)
					{
						is.close();
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		log.info("初始化相关属性结束。");
	}
}