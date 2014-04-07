package com.leisure.core.query;

import java.util.List;
import java.util.Map;

import com.leisure.core.dao.IGenericDAO;
import com.leisure.core.query.support.IQuery;
/**
 * 
 * @author xiaoxiong
 *
 */
public class GenericQuery implements IQuery {

	private IGenericDAO dao;

	private int begin;

	private int max;

	private Map params;

	public GenericQuery(IGenericDAO dao) {
		this.dao = dao;
	}

	public List getResult(String condition) {
		// TODO Auto-generated method stub
		return dao.find(condition, this.params, begin, max);
	}

	public List getResult(String condition, int begin, int max) {
		// TODO Auto-generated method stub
		Object[] params = null;
		return this.dao.find(condition, this.params, begin, max);
	}

	public int getRows(String condition) {
		// TODO Auto-generated method stub
		int n = condition.toLowerCase().indexOf("order by");
		Object[] params = null;
		if (n > 0) {
			condition = condition.substring(0, n);
		}
		List ret = dao.query(condition, this.params, 0, 0);
		if (ret != null && ret.size() > 0) {
			return ((Long) ret.get(0)).intValue();
		} else {
			return 0;
		}
	}

	public void setFirstResult(int begin) {
		this.begin = begin;
	}

	public void setMaxResults(int max) {
		this.max = max;
	}

	@Override
	public void setParaValues(Map params) {
		// TODO Auto-generated method stub
		this.params = params;
	}

}
