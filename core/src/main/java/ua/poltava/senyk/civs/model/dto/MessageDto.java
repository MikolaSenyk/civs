/*
 * Эта программа является свободным ПО: вы можете распространять и/или
 * модифицировать её согласно условиям GNU General Public License
 * 3-ей версии, либо (на ваш выбор) любой последующей версией,
 * опубликованной Free Software Foundation.
 * 
 * Вы можете получить копию GNU GPLv3 по ссылке
 *   http://www.gnu.org/licenses/
 * Эта программа не подразумевает АБСОЛЮТНО НИКАКИХ ГАРАНТИЙ.
 * Подробнее - см. GNU General Public License.
 */
package ua.poltava.senyk.civs.model.dto;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import net.sf.json.JSONObject;

/**
 * Бин для сериализации сообщений.
 * @author Александр Дашковский
 * @version 1.0
 * @created 2011-01-12
 * @updated 2011-01-14
 */
@XmlRootElement(name = "MessageDto")
public class MessageDto implements Serializable {

	private boolean success;		// Признак успешности выполнения.

	private String messageCode;		// Код сообщения.

	private String messageText;		// Текст сообщения.

	private String messageTarget;	// Цель сообщения.

	private Object body;			// Дополнительный объект сообщения.

	public MessageDto() { }

	public MessageDto(boolean success) {
		this.success = success;
	}

	public MessageDto(boolean success, String messageCode) {
		this.success = success;
		this.messageCode = messageCode;
	}

	public MessageDto(boolean success, String messageCode,
					String messageText, String messageTarget) {
		this.success = success;
		this.messageCode = messageCode;
		this.messageText = messageText;
		this.messageTarget = messageTarget;
	}
	
	/**
	 * Get MessageDto as JSON object
	 * @return JSON object
	 */
	public JSONObject getJSON() {
		JSONObject json = new JSONObject();
		json.put("success", isSuccess());
		json.put("messageText", getMessageText());
		json.put("messageCode", getMessageCode());
		return json;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getMessageTarget() {
		return messageTarget;
	}

	public void setMessageTarget(String messageTarget) {
		this.messageTarget = messageTarget;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

}
