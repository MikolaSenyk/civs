/*
 * Проект "Портал подготовки специалистов"
 * автоматизированной системы управления процессом подготовки специалистов.
 *
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

import java.util.Set;
import net.sf.json.JSONObject;

/**
 * Класс утилит для работы с JSON объектами
 * @author Александр Дашковский
 * @version 2.0
 * @created 2010-12-30
 * @updated 2011-09-13
 */
public class JsonUtils {

	/** Конструктор */
	private JsonUtils() {
	}
	
	public static JSONObject buildErrorMessage(String msg) {
		return buildErrorMessage(msg, null);
	}
	
	public static JSONObject buildErrorMessage(String msg, String code) {
		JSONObject json = new JSONObject();
		json.put("success", false);
		json.put("messageText", msg);
		if ( code != null ) {
			json.put("messageCode", code);
		}
		return json;
	}
	
	public static JSONObject buildSuccessMessage() {
		JSONObject json = new JSONObject();
		json.put("success", true);
		return json;
	}

	/**
	 * Возвращает объект JSON с сообщением об ошибке.
	 * @param msg Текс сообщения.
	 * @param forElementName Имя элемента формы, для которого получена ошибка.
	 * Обычно форма или поле формы.
	 * @return Строка JSON-сообщения
	 */
	public static String getErrorJsonMessage(String msg, String forElementName) {
		StringBuilder json = new StringBuilder();
		json.append("{\"success\": false");
		if (forElementName != null) {
			json.append(", forElement:'").append(forElementName).append("'");
		}
		json.append(", \"messageText\":\"").append(encode(msg)).append("\"}");
		return json.toString();
	}

	/**
	 * Возвращает объект JSON с сообщением об ошибке.
	 * @param msg Текс сообщения.
	 * @return Строка JSON-сообщения
	 */
	public static String getErrorJsonMessage(String msg) {
		return getErrorJsonMessage(msg, null);
	}

	/**
	 * Возвращает объект JSON с признаком успеха и одним параметром.
	 * @param name Имя данных.
	 * @param value Значение данных.
	 * @return Строка JSON-сообщения
	 */
	public static String getSuccessJsonMessage(String name, String value) {
		StringBuilder json = new StringBuilder();
		json.append("{\"success\":true");
		if (name != null) {
			json.append(", ").append(name).append(":'").append(value).append("'");
		}
		json.append("}");
		return json.toString();
	}

	/**
	 * Возвращает объект JSON с признаком успеха.
	 * @return Строка JSON-сообщения
	 */
	public static String getSuccessJsonMessage() {
		return getSuccessJsonMessage(null, null);
	}

	/**
	 * Возвращает объект JSON с признаком успеха и списком параметров.
	 * @param params Список пар имя/значение для формирования JSON
	 * @return JSON сообщение
	 */
	public static String getSuccessJsonMessage(Set<String[]> params) {
		StringBuilder json = new StringBuilder();
		json.append("{\"success\":true");
		if (params != null) {
			for (String[] param : params) {
				json.append(", ").append(param[0]).append(":");
				String value = encode(param[1]);
				if (isString(value)) {
					json.append("'").append(value).append("'");
				} else {
					json.append(value);
				}
			}
		}
		json.append("}");
		return json.toString();
	}

	/**
	 * Возвращает строку с закодированными кавычками.
	 * @param input Входная строка.
	 * @return Приведенная строка
	 */
	public static String encode(String input) {
		if (input == null || input.length() == 0) {
			return "";
		}
		StringBuilder buf = new StringBuilder();
		char ch;
		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			if (ch == '"') {
				buf.append("\\\"");
			} else if (ch == '\n') {
				buf.append("\\n");
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}

	/**
	 * Возвращает признак того, что входные данные - это строка. Если входные
	 * данные содержат только цифры или <code>true/false</code> - это не строка.
	 * @param input Входная строка.
	 * @return
	 */
	public static boolean isString(String input) {
		if (input == null || input.length() == 0) {
			return false;
		}
		if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {
			return false;
		}
		char ch;
		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			if (!Character.isDigit(ch)) {
				return true;
			}
		}
		return false;
	}

}
