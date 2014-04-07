package com.leisure.service;

import java.util.List;
import java.util.Map;

import com.leisure.core.query.support.IPageList;
import com.leisure.core.query.support.IQueryObject;
import com.leisure.domain.Accessory;

public interface IAccessoryService {
	public boolean save(Accessory acc);

	public boolean delete(Long id);

	public boolean update(Accessory acc);

	IPageList list(IQueryObject properties);

	public Accessory getObjById(Long id);

	public Accessory getObjByProperty(String propertyName,String value);

	public List<Accessory> query(String query, Map params, int begin, int max);
}
