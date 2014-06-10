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
package ua.poltava.senyk.civs.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import ua.poltava.senyk.civs.model.dto.UserDto;

/**
 * Config app servlet. Singleton.
 * Stolen from Daaz
 */
public class AppConfig extends WebApplicationObjectSupport {

	private static Logger _logger = Logger.getLogger(AppConfig.class);
	private static String _propertyFileLocation;
	private static Properties _config;
    
    private boolean _setupDone;
	private ApplicationContext _appContext;
	@Autowired
	private UserService _userService;
	// config properties
	private long adminUserId;

	protected AppConfig() {
	}

	/**
	 * Class holder on JVM start initialization
	 */
	private static class SingletonHolder {

		public static AppConfig instance = new AppConfig();
	}
	
	/**
	 * Get admin user login and look for ID
	 */
	private void findAdminUser() throws Exception {
		String adminLogin = _config.getProperty("admin.login");
		UserDto adminUser = _userService.findUser(adminLogin);
		if ( adminUser.isSuccess() ) {
			this.adminUserId = adminUser.getId();
		}
	}

	/**
	 * Выполняется установка конфигурационных параметров. Параметры считываются с
	 * конфигурационных файлов.
	 */
	public void init() throws Exception {
		if (_setupDone) {
			return;
		}
		_appContext = getApplicationContext();

		_logger.info("AppConfig::init():WebAppRootPath = " + getWebAppRootPath());

		//-- read file with properties --
		String fPath = _propertyFileLocation;
		if ( !fPath.startsWith(File.separator) ) {
			// read as resource
			Resource fResource = _appContext.getResource(_propertyFileLocation);
			fPath = fResource.getFile().getAbsolutePath();
		}
		if (_propertyFileLocation == null || _propertyFileLocation.length() == 0
				|| !(new File(fPath).isFile())) {
			String msg = "Cannot read the files config file.";
			_logger.fatal(msg);
			throw new Exception(msg);
		}
		try {
			FileInputStream fin = new FileInputStream(fPath);
			_config = new Properties();
			_config.load(fin);
			fin.close();
		} catch (Exception e) {
			throw e;
		}
		
		// custom init
		findAdminUser();
		
		_logger.info(" OK!");
		_setupDone = true;
	}

	/**
	 * Close service
	 */
	public void destroy() {
		if (_logger != null) {
			_logger.info("CivilSociety service: destroyed.");
		}
	}

	/**
	 * Получить контекст приложения. Контекст определяется в конфирационном
	 * файле Spring.
	 * @return Контекст приложения.
	 */
	public ApplicationContext getSpringApplicationContext() {
		return _appContext;
	}

	/**
	 * Получить путь к корневой папке web-приложеня.
	 * @return Путь к корневой папке web-приложеня.
	 */
	public String getWebAppRootPath() {
		return getWebApplicationContext().getServletContext().getRealPath("/");
	}

	/**
	 * Установить местоположение файла описания настроек файловой системы
	 * для пользователей.
	 * @param location Местоположение файла политик.
	 */
	public void setPropertyFileLocation(String location) {
		_propertyFileLocation = location;
	}

	/**
	 * Get ID of admin user that described in config property file
	 * @return ID of admin user (admin.login property name)
	 */
	public long getAdminUserId() {
		return adminUserId;
	}
    
	/**
	 * Статический метод возвращает единственный экземпляр данного класса.
	 * @return Экземпляр класса ConfigSingleton
	 */
	public static AppConfig getInstance() {
		return SingletonHolder.instance;
	}
	
}
