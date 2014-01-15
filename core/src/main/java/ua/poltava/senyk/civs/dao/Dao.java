/*
 * Copyright (C) 2010 Александр Дашковский.
 *
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
package ua.poltava.senyk.civs.dao;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 * Абстрактный класс для реализации паттерна Data Access Object.
 * @param <T> Тип сущности, для которой реализуется доступ к базе данных.
 * @author Александр Дашковский
 */
public abstract class Dao<T> {

	private EntityManager _em;

	private Class _entityClass;

	/** Логгер для журналирования */
	protected Logger _logger = Logger.getLogger(super.getClass());

	/**
	 * Установить JPA Entity Manager.
	 * @param em JPA Entity Manager.
	 */
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		_em = em;
	}

	/**
	 * Получить JPA Entity Manager
	 * @return JPA Entity Manager
	 * @throws java.lang.Exception Если JPA Entity Manager не был ранее назначен.
	 */
	public EntityManager getEntityManager() throws Exception {
		if (_em == null) {
			throw new Exception("Dao::getEntityManager(): EntityManager has not been set before usage");
		}
		return _em;
	}

	/**
	 * Установить класс сущности.
	 * @param entityClass класс сущности.
	 */
	public void setEntityClass(Class entityClass) {
		_entityClass = entityClass;
	}

	/**
	 * Метод возвращает класс сущности.
	 */
	public Class getEntityClass() throws Exception {
		if (_entityClass == null) {
			throw new Exception("Dao::getEntityClass(): Entity class has not been set before usage");
		}
		return _entityClass;
	}

	/**
	 * Добавить новый объект в базу данных.
	 * @param entity объект, который должен быть сохранен
	 * @throws java.lang.Exception
	 */
	public void addObject(T entity) throws Exception {
		getEntityManager().persist(entity);
	}

	/**
	 * Обновить объект в базе данных.
	 * @param entity объект, который должен быть обновлен
	 * @throws java.lang.Exception
	 */
	public void updateObject(T entity) throws Exception {
		getEntityManager().merge(entity);
	}

	/**
	 * Удалить объект из базы данных.
	 * @param entity объект, который должен быть удален
	 * @throws java.lang.Exception
	 */
	public void deleteObject(T entity) throws Exception {
		getEntityManager().remove(entity);
	}

	/**
	 * Удалить объект из базы данных.
	 * @param id идентификатор (primary key) записи объекта в базе данных.
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings("unchecked")
    public void deleteObject(Serializable id) throws Exception{
		getEntityManager().remove((T) getEntityManager().find(getEntityClass(), id));
	}

	/**
	 * Выполнить SQL или другой запрос в стиле "prepared statement".
	 * @param request строка запроса к базе данных
	 * @param values значения, которые должны быть подставлены вместо "?"
	 */
	public void updateObject(String request, Object... values) throws Exception {
		try {
			Query query = getEntityManager().createNativeQuery(request);
			int i = 0;
			for (Object v : values) {
				i++;
				query.setParameter(i, v);
			}
			query.executeUpdate();
		} catch (Exception e) {
			StringBuilder buf = new StringBuilder();
			for (Object v : values) {
				buf.append(", ").append(v);
			}
			throw new Exception("Dao::updateObject(\"" + request + "\" " + buf.toString() + "); " + e.toString());
		}
	}

	/**
	 * Метод извлечения объекта из базы данных.
	 * @param id идентификатор (primary key) of the class
	 * @return filled object
	 */
	@SuppressWarnings("unchecked")
    public T getObject(Number id) throws Exception {
		return ((T) getEntityManager().find(getEntityClass(), id));
	}

	/**
	 * Метод извлечения объекта из базы данных.
	 * @param id идентификатор (primary key) of the class
	 * @return filled object
	 */
	@SuppressWarnings("unchecked")
    public T getObject(String id) throws Exception {
		return ((T) getEntityManager().find(getEntityClass(), id));
	}

	/**
	 * Получить коллекцию объектов из базы данных согласно именованному запросу.
	 * @param namedQuery именовенный запрос.
	 * @return список объектов.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
    public List<T> getObjectList(String namedQuery) throws Exception {
		List<T> objects = null;
		try {
			Query query = getEntityManager().createNamedQuery(namedQuery);
			objects = query.getResultList();
		} catch (Exception e) {
			throw new Exception("Dao::getObjectList(\"" + namedQuery + "\"): " + e.toString());
		}
		return objects;
	}
}
