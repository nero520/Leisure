package com.leisure.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.leisure.domain.LogType;

/**
 * @see 系统日志记录注解，该注解用在需要记录操作日志的action中，使用Spring AOP结合
 * 该注解完成操作日志记录
 * @author xiaoxiong
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Log {
	/**
	 * 
	 * @return
	 */
	public String title() default "";

	/**
	 * 
	 * @return
	 */
	public String entityName() default "";

	/**
	 * 
	 * @return
	 */
	public LogType type();

	/**
	 * 方法描述
	 * 
	 * @return
	 */
	public String description() default "";

	/**
	 * 
	 * @return
	 */
	public String ip() default "";

}
