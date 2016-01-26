/**
 * Copyright 2013, WebGate Consulting AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.xpt.core.dss.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DominoStore {
	String Form();
	String PrimaryKeyField();
	String View();
	String JavaFieldPrefix() default "m_";
	String JavaFieldPostFix() default "";
	/**
	 * Define witch datatype has the primary key.<br>
	 * use:<br> String.class -> String<br>
	 * Integer.class -> int<br>
	 * Double.class -> double;<br>
	 * Date.class -> Date;<br>
	 * Boolean.class -> boolean;<br>
	 * String[].class -> String[]<br>
	 * @return
	 */
	Class<?> PrimaryFieldClass();
}
