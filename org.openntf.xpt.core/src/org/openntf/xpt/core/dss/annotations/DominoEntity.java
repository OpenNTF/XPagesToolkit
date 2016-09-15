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

/**
 * Domino Entity defines the mapping between a Java member and a Notes Field in a document.
 * @author Christian Guedemann
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

public @interface DominoEntity {
	/**
	 * Name of the Notes Field in the document
	 * @return
	 */
	String FieldName();
	/**
	 * Readonly access to the document, no update during save
	 * @return
	 */
	boolean readOnly() default false;
	/**
	 * Writeonly access, this propertie is not read during load
	 * @return
	 */
	boolean writeOnly() default false;
	/**
	 * Value is calculated during load, based on the @Formula defined in the FieldName
	 * @return
	 */
	boolean isFormula() default false;
	/**
	 * Value is stored with Names Field Attribute in the document
	 * @return
	 */
	boolean isNames() default false;
	/**
	 * Value is stored with Author Field Attribute in the document
	 * @return
	 */
	boolean isAuthor() default false;
	/**
	 * Value is stored with Reader Field Attribute in the document
	 * @return
	 */
	boolean isReader() default false;
	/**Defines the format of the Name (CN and ABBREVIATE are implemented)
	 * @return
	 */
	String showNameAs() default ""; //CN and ABBREVIATE are implemented in NamesProcessor.java
	/**
	 * Stores only the date of a Date Object in the document
	 * @return
	 */
	boolean dateOnly() default false;
	/**
	 * Mark this field as "log any changes" field. You have to implement a ChangeLog facility
	 * @return
	 */
	boolean changeLog() default false;
	/**
	 * Marks this field as "to encrypt". You have to implement a Encryption facility
	 * @return
	 */
	boolean encrypt() default false;
	/**
	 * Defines the usere roles who can access the encypted field
	 * @return
	 */
	String[] encRoles() default {};
	/**
	 * Store a business Object with the FieldName as prefix and all its properties.
	 * @return
	 */
	boolean embedded() default false;
	/**
	 * Value is stored with Author Field Attribute in the document
	 * @return
	 */
	boolean isNotesSummary() default true;
}
