/*
 * Copyright 2009 Alexander Dashkovsky.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ua.poltava.senyk.civs.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

/**
 * Класс утилит для работы с HTTP объектами
 * @author Александр Дашковский
 * @version 1.0
 * @created 2012-02-15
 * @updated 2013-08-11
 */
public class HttpUtils {

	private static final String REQUEST_TYPE = "x-requested-with";

	private static final String REQUEST_XHR = "XMLHttpRequest";

	private static final String TYPE_JSON = "application/json; charset=utf-8";

	/**
	 * Возвращает объект JSON с сообщением об ошибке.
	 * @param msg Текс сообщения.
	 * @return Строка JSON-сообщения
	 */
	public static boolean isXHR(HttpServletRequest request) {
		return REQUEST_XHR.equals(request.getHeader(REQUEST_TYPE));
	}

	/**
	 * Возвращает тело запроса как строку.
	 * @param request
	 * @return 
	 */
	public String getRequestBodyString(HttpServletRequest request)
			throws Exception {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		String line = null;
		try {
			InputStream body = request.getInputStream();
			reader = new BufferedReader(new InputStreamReader(body, "UTF-8")); 
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			try { reader.close(); } catch(Exception ignore) { }
		}
		return sb.toString();
	}
	
	/**
	 * Отослать запрос в виде JSON-строки POST-методом и получить ответ.
	 * @param url Адрес сервера, куда будет отправлен запрос.
	 * @param bodyJsonStr Строка, как содержимое тела запроса.
	 * @param headers Коллекция заголовков запроса.
	 * @param responseTimeout Таймаут ожидания запроса в милисекундах.
	 * Если = 0, таймаут не контролируется.
	 * @return Строка ответа.
	 * @throws Exception 
	 */
	public String sendBodyJsonString(String url, String bodyJsonStr,
			Properties headers, int responseTimeout) throws Exception {
		return sendBodyString(url, bodyJsonStr, TYPE_JSON, headers, responseTimeout);
	}
	
	/**
	 * Отослать запрос в виде JSON-строки POST-методом и получить ответ.
	 * @param url Адрес сервера, куда будет отправлен запрос.
	 * @param bodyJsonStr Строка, как содержимое тела запроса.
	 * @param headers Коллекция заголовков запроса.
	 * контролируется.
	 * @return Строка ответа.
	 * @throws Exception 
	 */
	public String sendBodyJsonString(String url, String bodyJsonStr,
			Properties headers) throws Exception {
		return sendBodyString(url, bodyJsonStr, TYPE_JSON, headers, 0);
	}
	
	/**
	 * Отослать запрос в виде JSON-строки POST-методом и получить ответ.
	 * @param url Адрес сервера, куда будет отправлен запрос.
	 * @param bodyJsonStr Строка, как содержимое тела запроса.
	 * @param responseTimeout Таймаут ожидания запроса в милисекундах.
	 * Если = 0, таймаут не контролируется.
	 * @return Строка ответа.
	 * @throws Exception 
	 */
	public String sendBodyJsonString(String url, String bodyJsonStr,
			int responseTimeout) throws Exception {
		return sendBodyString(url, bodyJsonStr, TYPE_JSON, null, responseTimeout);
	}
	
	/**
	 * Отослать запрос в виде JSON-строки POST-методом и получить ответ.
	 * @param url Адрес сервера, куда будет отправлен запрос.
	 * @param bodyJsonStr Строка, как содержимое тела запроса.
	 * @return Строка ответа.
	 * @throws Exception 
	 */
	public String sendBodyJsonString(String url, String bodyJsonStr)
			throws Exception {
		return sendBodyString(url, bodyJsonStr, TYPE_JSON, null, 0);
	}
	
	/**
	 * Отослать запрос в виде неименованной строки POST-методом и получить ответ
	 * @param url Адрес сервера, куда будет отправлен запрос.
	 * @param bodyStr Строка, как содержимое тела запроса.
	 * @param strType Тип содержимого тела запроса.
	 * @param headers Коллекция заголовков запроса.
	 * @param responseTimeout Таймаут ожидания запроса в милисекундах.
	 * Если = 0, таймаут не контролируется.
	 * @return Строка ответа.
	 * @throws Exception 
	 */
	public String sendBodyString(String url, String bodyStr,
			String strType, Properties headers, int responseTimeout)
			throws Exception {
		
		String result;
		try {
			HttpClient client =	new  HttpClient();
			
			if (responseTimeout > 0) {
				HttpConnectionManager cm = client.getHttpConnectionManager();
				HttpConnectionManagerParams hcmp = new HttpConnectionManagerParams();
				hcmp.setSoTimeout(responseTimeout);
				cm.setParams(hcmp);
			}

			PostMethod mPost = new PostMethod(url.toString());
			
			StringRequestEntity body = 
						new StringRequestEntity(bodyStr, strType, "UTF-8");
			mPost.setRequestEntity(body);
			
			if (headers != null && headers.size() > 0) {
				Header header = new Header();
				Enumeration en = headers.keys();
				while (en.hasMoreElements()) {
					String k = (String)en.nextElement();
					String v = headers.getProperty(k);
					header.setName(k);
					header.setValue(v);
				}
				mPost.addRequestHeader(header);
			}

//			Enumeration en = params.keys();
//			while (en.hasMoreElements()) {
//				String k = (String)en.nextElement();
//				String v = params.getProperty(k);
//				mPost.addParameter(k, v);
//			}

			client.executeMethod(mPost);
			result = mPost.getResponseBodyAsString();
			mPost.releaseConnection();
		}
		catch (Exception e) {
			throw new Exception("HttpUtils::sendBodyString(" + url + ")" + e);
		}
		return result;
	}
	
}
