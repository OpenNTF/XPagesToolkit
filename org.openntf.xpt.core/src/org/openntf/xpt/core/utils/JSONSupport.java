/*
 * © Copyright WebGate Consulting AG, 2012
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

import java.io.IOException;
import java.util.Date;

import com.ibm.domino.services.util.JsonWriter;

public class JSONSupport {
	public static void writeString(JsonWriter jsWriter, String strField, String strValue, boolean writeEmpty) throws IOException {
		if (strValue != null) {
			jsWriter.startProperty(strField);
			jsWriter.outStringLiteral(strValue);
			jsWriter.endProperty();

		} else {
			writeEmpty(jsWriter, strField, writeEmpty);
		}
	}

	public static void writeInt(JsonWriter jsWriter, String strField, Integer nValue, boolean writeEmpty) throws IOException {
		if (nValue > 0) {
			jsWriter.startProperty(strField);
			jsWriter.outIntLiteral(nValue);
			jsWriter.endProperty();

		} else {
			writeEmpty(jsWriter, strField, writeEmpty);
		}
	}

	public static void writeDate(JsonWriter jsWriter, String strField, Date datCurrent, boolean writeEmpty) throws IOException {
		if (datCurrent != null) {
			jsWriter.startProperty(strField);
			jsWriter.outDateLiteral(datCurrent);
			jsWriter.endProperty();

		} else {
			writeEmpty(jsWriter, strField, writeEmpty);
		}

	}

	public static void writeDouble(JsonWriter jsWriter, String strField, Double dblValue, boolean writeEmpty) throws IOException {
		if (dblValue != null) {
			jsWriter.startProperty(strField);
			jsWriter.outNumberLiteral(dblValue);
			jsWriter.endProperty();

		} else {
			writeEmpty(jsWriter, strField, writeEmpty);
		}
	}

	public static void writeBoolean(JsonWriter jsWriter, String strField, Boolean blValue, boolean writeEmpty) throws IOException {
		if (blValue != null) {
			jsWriter.startProperty(strField);
			jsWriter.outBooleanLiteral(blValue);
			jsWriter.endProperty();

		} else {
			writeEmpty(jsWriter, strField, writeEmpty);
		}
	}

	private static void writeEmpty(JsonWriter jsWriter, String strField, boolean writeEmpty) throws IOException {
		if (writeEmpty) {
			jsWriter.startProperty(strField);
			jsWriter.endProperty();

		}
	}

}
