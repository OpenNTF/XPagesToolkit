/*
 * © Copyright WebGate Consulting AG, 2013
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
package org.openntf.xpt.agents.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface XPagesAgent {

	String Name();

	String Alias();

	/**
	 * 1 = onSchedule 2 = onRequest
	 * 
	 * @return
	 */
	ExecutionMode executionMode() default ExecutionMode.ON_REQUEST;

	/**
	 * Intervall in minutes. Means pause after excution
	 * 
	 * @return
	 */
	int intervall() default 0;

	ExecutionDay[] executionDay() default { ExecutionDay.ALLDAY };

	int execTimeWindowStartHour() default 0;

	int execTimeWindowStartMinute() default 0;

	int execTimeWindowEndHour() default 23;

	int execTimeWindowEndMinute() default 59;
}
