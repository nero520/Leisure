package com.leisure.core.dialect;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQL5Dialect;

public class SystemMySQL5Dialect extends MySQL5Dialect {
	@SuppressWarnings("deprecation")
	public SystemMySQL5Dialect() {
		super();
		registerHibernateType(Types.LONGVARCHAR, Hibernate.TEXT.getName());
	}

}
