package org.openntf.xpt.core.dss.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface XPTPresentationControl {

	String[] editRoles() default {};

	boolean editNewOnly() default false;

	boolean modifyOnly() default false;
}
