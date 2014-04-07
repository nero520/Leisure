package com.leisure.core.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import com.leisure.core.tools.CommUtil;
import com.leisure.core.exception.CanotRemoveObjectException;

/**
 *  The database operation based on DAO, system uses JPA to complete all database operations, 
 *       the default JPA implementation for Hibernate
 * @author xiaoxiong
 * 
 */
public class GenericEntityDao extends JpaDaoSupport {

	public GenericEntityDao() {

	}

	public Object get(Class clazz, Serializable id) {
		if (id == null)
			return null;
		return this.getJpaTemplate().find(clazz, id);
	}

	public List<Object> find(Class clazz, final String queryStr,
			final Map params, final int begin, final int max) {
		final Class claz = clazz;
		List<Object> ret = (List<Object>) this.getJpaTemplate().execute(
				new JpaCallback() {

					public Object doInJpa(EntityManager em)
							throws PersistenceException {
						String clazzName = claz.getName();
						StringBuffer sb = new StringBuffer("select obj from ");
						sb.append(clazzName).append(" obj").append(" where ")
								.append(queryStr);
						Query query = em.createQuery(sb.toString());
						for (Object key : params.keySet()) {
							query.setParameter(key.toString(), params.get(key));
						}
						if (begin >= 0 && max > 0) {
							query.setFirstResult(begin);
							query.setMaxResults(max);
						}
						query.setHint("org.hibernate.cacheable", true);
						return query.getResultList();
					}
				});
		if (ret != null && ret.size() >= 0) {
			return ret;
		} else {
			return new ArrayList<Object>();
		}
	}

	public List query(final String queryStr, final Map params, final int begin,
			final int max) {
		List list = (List) this.getJpaTemplate().execute(new JpaCallback() {
			@Override
			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = em.createQuery(queryStr);
				if (params != null && params.size() > 0) {
					for (Object key : params.keySet()) {
						query.setParameter(key.toString(), params.get(key));
					}
				}
				if (begin >= 0 && max > 0) {
					query.setFirstResult(begin);
					query.setMaxResults(max);
				}
				query.setHint("org.hibernate.cacheable", true);
				return query.getResultList();
			}

		});
		if (list != null && list.size() > 0) {
			return list;
		} else
			return new ArrayList();
	}

	public void remove(Class clazz, Serializable id)
			throws CanotRemoveObjectException {
		Object object = this.get(clazz, id);
		if (object != null) {
			try {
				this.getJpaTemplate().remove(object);
			} catch (Exception e) {
				throw new CanotRemoveObjectException();
			}
		}
	}

	public void save(Object instance) {
		this.getJpaTemplate().persist(instance);
	}

	public Object getBy(Class clazz, final String propertyName,
			final Object value) {
		final Class claz = clazz;
		List<Object> ret = (List<Object>) this.getJpaTemplate().execute(
				new JpaCallback() {
					public Object doInJpa(EntityManager em)
							throws PersistenceException {
						String clazzName = claz.getName();
						StringBuffer sb = new StringBuffer("select obj from ");
						sb.append(clazzName).append(" obj");
						Query query = null;
						if (propertyName != null && value != null) {
							sb.append(" where obj.").append(propertyName)
									.append(" = :value");
							query = em.createQuery(sb.toString()).setParameter(
									"value", value);
						} else {
							query = em.createQuery(sb.toString());
						}
						query.setHint("org.hibernate.cacheable", true);
						return query.getResultList();
					}
				});
		if (ret != null && ret.size() == 1) {
			return ret.get(0);
		} else if (ret != null && ret.size() > 1) {
			throw new java.lang.IllegalStateException(
					"worning  --more than one object find!!");
		} else {
			return null;
		}
	}

	public List executeNamedQuery(final String queryName,
			final Object[] params, final int begin, final int max) {
		List ret = (List) this.getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = em.createNamedQuery(queryName);
				int parameterIndex = 1;
				if (params != null && params.length > 0) {
					for (Object obj : params) {
						query.setParameter(parameterIndex++, obj);
					}
				}
				if (begin >= 0 && max > 0) {
					query.setFirstResult(begin);
					query.setMaxResults(max);
				}
				query.setHint("org.hibernate.cacheable", true);
				return query.getResultList();
			}
		});
		if (ret != null && ret.size() >= 0) {
			return ret;
		} else {
			return new ArrayList();
		}
	}

	public void update(Object instance) {
		this.getJpaTemplate().merge(instance);
	}

	public List executeNativeNamedQuery(final String nnq) {
		Object ret = this.getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = em.createNativeQuery(nnq);
				return query.getResultList();
			}
		});
		return (List) ret;
	}

	public List executeNativeQuery(final String nnq, final Map params,
			final int begin, final int max) {
		List ret = (List) this.getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = em.createNativeQuery(nnq);
				int parameterIndex = 1;
				if (params != null) {
					Iterator its = params.keySet().iterator();
					while (its.hasNext()) {
						query.setParameter(CommUtil.null2String(its.next()),
								params.get(its.next()));
					}
				}
				if (begin >= 0 && max > 0) {
					query.setFirstResult(begin);
					query.setMaxResults(max);
				}
				return query.getResultList();
			}
		});
		if (ret != null && ret.size() >= 0) {
			return ret;
		} else {
			return new ArrayList();
		}
	}

	public List executeNativeQuery(final String nnq, final Object[] params,
			final int begin, final int max) {
		List ret = (List) this.getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = em.createNativeQuery(nnq);
				int parameterIndex = 1;
				if (params != null && params.length > 0) {
					for (Object obj : params) {
						query.setParameter(parameterIndex++, obj);
					}
				}
				if (begin >= 0 && max > 0) {
					query.setFirstResult(begin);
					query.setMaxResults(max);
				}
				return query.getResultList();
			}
		});
		if (ret != null && ret.size() >= 0) {
			return ret;
		} else {
			return new ArrayList();
		}
	}

	public int executeNativeSQL(final String nnq) {
		Object ret = this.getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = em.createNativeQuery(nnq);
				query.setHint("org.hibernate.cacheable", true);
				return query.executeUpdate();
			}
		});
		return (Integer) ret;
	}

	public int batchUpdate(final String jpql, final Object[] params) {
		Object ret = this.getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = em.createQuery(jpql);
				int parameterIndex = 1;
				if (params != null && params.length > 0) {
					for (Object obj : params) {
						query.setParameter(parameterIndex++, obj);
					}
				}
				query.setHint("org.hibernate.cacheable", true);
				return query.executeUpdate();
			}
		});
		return (Integer) ret;
	}

	public void flush() {
		this.getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				em.getTransaction().commit();
				return null;
			}
		});
	}
}
