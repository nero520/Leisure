﻿package com.leisure.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.leisure.core.query.support.IPageList;
import com.leisure.core.query.support.IQueryObject;
import com.leisure.domain.Album;

public interface IAlbumService {
	/**
	 * @see 保存一个Album，如果保存成功返回true，否则返回false
	 * 
	 * @param instance
	 * @return 是否保存成功
	 */
	boolean save(Album instance);

	/**
	 * @see 根据一个ID得到Album
	 * 
	 * @param id
	 * @return
	 */
	Album getObjById(Long id);

	/**
	 * @see 删除一个Album
	 * 
	 * @param id
	 * @return
	 */
	boolean delete(Long id);

	/**
	 * @see 批量删除Album
	 * 
	 * @param ids
	 * @return
	 */
	boolean batchDelete(List<Serializable> ids);

	/**
	 * @see 通过一个查询对象得到Album
	 * 
	 * @param properties
	 * @return
	 */
	IPageList list(IQueryObject properties);

	/**
	 * @see 更新一个Album
	 * 
	 * @param id
	 *            需要更新的Album的id
	 * @param dir
	 *            需要更新的Album
	 */
	boolean update(Album instance);

	/**
	 * 
	 * @param query
	 * @param params
	 * @param begin
	 * @param max
	 * @return
	 */
	List<Album> query(String query, Map params, int begin, int max);

	/**
	 * 
	 * @param propertyName
	 * @return
	 */
	Album getDefaultAlbum(Long id);
}
