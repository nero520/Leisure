package com.leisure.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  系统权限资源标签，系统使用springSecurity作为权限框架，该注解用在需要纳入权限管理的action中，
 *  通过AdminManageAction中init_role方法完成权限资源基础数据的导入以及不同用户角色权限的分配
 * @author xiaoxiong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SecurityMapping {
	String title() default "";// The authorization name

	String value() default "";//  The authorization value

	String rname() default "";// The role name

	String rcode() default "";// The role code

	int rsequence() default 0;// The role sequence

	String rgroup() default "";// The role group

	String rtype() default "";// The role type
    
	/** Whether to display the role, the default 1 for display, 
    special circumstances set to 0 does not display, no significant role,
    automatically given when adding administrator  */
	boolean display() default true;


}
