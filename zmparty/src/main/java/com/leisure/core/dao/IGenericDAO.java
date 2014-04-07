package com.leisure.core.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/*
 * @author xiaoxiong
 */

public interface IGenericDAO<T> {

	/**
	 * @see find the object by the id
	 * 
	 * @param id
	 *           
	 * @return 
	 */
	T get(Serializable id);

	/**
	 * @see persist the object。
	 * 
	 * @param newInstance
	 *            
	 */
	void save(T newInstance);

	/**
	 * @see delete the object by the id 
	 * 
	 * @param id
	 *           
	 */
	void remove(Serializable id);

	/**
	 * @see update the object, Mainly used for an update the object except persistenceContext object.
	 * 
	 * @param transientObject
	 *           
	 */
	void update(T transientObject);

	/**
	 * @see find the object by property name and the value;
	 * 
	 * @param propertyName
	 *            
	 * @param value
	 *           
	 * @return  an object
	 */
	T getBy(String propertyName, Object value);

	/**
	 * @see According to a query conditions 、 parameters 、 the start position and max number,  find the list objects
	 * 
	 * @param queryName
	 *            
	 * @param params
	 *            Using Object arrays, keep the params order and query conditions consistent position.
	 * @param begin
	 *            
	 * @param max
	 *           
	 * @return list or null
	 */
	List executeNamedQuery(final String queryName, final Object[] params,
			final int begin, final int max);

	/**
	 * @see find the list data by conditions
	 * 
	 * @param query
	 *             
	 * @param params
	 *            for example:
	 *            Map map=new HashMap();
	 *            map.put("id",id);
	 *            map.put("role",role);         
	 *            query("select obj from User obj where obj.id=:id and obj.userRole=:role order by obj.addTime desc",map,1,20);
	 * @param begin 
	 * @param max
	 * @return 
	 */
	List<T> find(String query, Map params, int begin, int max);

	/**
	 * @see According to a query conditions ,parameters, the start position , find the number to find any type objects
	 * 
	 * @param query
	 *            for example: select user from User user where user.name =
	 *            :name and user.properties = :properties
	 * @param params
	 *            
	 * @param begin
	 *           
	 * @param max
	 *            
	 * @return list or null
	 */
	List query(String query, Map params, int begin, int max);

	/**
	 * @see excute batch update data with jpql
	 * 
	 * @param jpql
	 *          
	 * @param params
	 *            
	 * @return
	 */
	int batchUpdate(String jpql, Object[] params);

	/**
	 * @see execute sql query
	 * 
	 * @param nnq
	 * @return
	 */
	public List executeNativeNamedQuery(String nnq);

	/**
	 * 
	 * @param nnq
	 * @param params
	 * @param begin
	 * @param max
	 * @return
	 */
	List executeNativeQuery(final String nnq, final Object[] params,
			final int begin, final int max);

	/**
	 * @see execute sql 
	 * 
	 * @param nnq
	 * @return
	 */
	public int executeNativeSQL(final String nnq);

	/**
	 * @see clear dao
	 */
	public void flush();

}