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
package org.openntf.xpt.core.dss.binding;

import java.util.Vector;

import lotus.domino.Document;

import org.openntf.xpt.core.dss.DSSException;

public interface IBinder<T> {
	public void processDomino2Java(Document docCurrent, Object objCurrent, Vector<?> vecValues, Definition def);

	public T[] processJava2Domino(Document docCurrent, Object objCurrent, Definition def);

	public T getValue(Object objCurrent, String strJavaField);

	public T getValueFromStore(Document docCurrent, Vector<?> vecValues, Definition def) throws DSSException;
}
