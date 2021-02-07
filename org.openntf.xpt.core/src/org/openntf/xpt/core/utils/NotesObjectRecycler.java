/**
 * Copyright 2014, WebGate Consulting AG
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
package org.openntf.xpt.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.openntf.xpt.core.utils.logging.LoggerFactory;

import lotus.domino.Base;

public class NotesObjectRecycler {

	public static void recycle(Base... recyclingObjects) {
		for (Base torecycle : recyclingObjects) {
			if (torecycle != null) {
				try {
					torecycle.recycle();
				} catch (Exception ex) {
					LoggerFactory.logError(NotesObjectRecycler.class, "Error during recyle", ex);
				}
			}
		}
	}
	public static void recycleAll(List<?> all) {
		List<Base> toRecyle = new ArrayList(all.size());
		for (Object obj:all) {
			if(obj instanceof Base) {
				toRecyle.add((Base)obj);
			}
		}
		recycle(toRecyle.toArray(new Base[toRecyle.size()]));
	}
}
