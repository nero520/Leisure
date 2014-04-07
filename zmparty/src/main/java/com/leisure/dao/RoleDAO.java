package com.leisure.dao;

import org.springframework.stereotype.Repository;

import com.leisure.core.base.GenericDAO;
import com.leisure.domain.Role;

@Repository("roleDAO")
public class RoleDAO extends GenericDAO<Role> {
}
