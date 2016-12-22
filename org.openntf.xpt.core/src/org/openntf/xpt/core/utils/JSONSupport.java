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

import java.io.IOException;
import java.util.Date;

import org.openntf.xpt.core.json.JSONEmptyValueStrategy;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.domino.services.util.JsonWriter;

/**
 * JSON Support is a library of convenience function to write values as
 * property: value pair. The class is heavily used by the JSONBinder Framework
 * 
 * @author Christian Guedemann
 * 
 */
public class JSONSupport {
	/**
	 * Writes a String value for a given property
	 * 
	 * @param writer
	 * @param jsonProperty
	 * @param value
	 * @param writeEmpty
	 * @throws IOException
	 * @throws JsonException
	 */
	public static void writeString(JsonWriter writer, String jsonProperty, String value, boolean writeEmpty) throws IOException, JsonException {
		writeString(writer, jsonProperty, value, writeEmpty ? JSONEmptyValueStrategy.NULL : JSONEmptyValueStrategy.NOPROPERTY);
	}

	/**
	 * Writes a Integer value for a given property
	 * 
	 * @param writer
	 * @param jsonProperty
	 * @param nValue
	 * @param writeEmpty
	 * @throws IOException
	 * @throws JsonException
	 */
	public static void writeInt(JsonWriter writer, String jsonProperty, Integer nValue, boolean writeEmpty) throws IOException, JsonException {
		writeInt(writer, jsonProperty, nValue, writeEmpty ? JSONEmptyValueStrategy.NULL : JSONEmptyValueStrategy.NOPROPERTY);
	}

	/**
	 * Writes a Date value for a given property
	 * 
	 * @param writer
	 * @param jsonProperty
	 * @param datCurrent
	 * @param writeEmpty
	 * @throws IOException
	 * @throws JsonException
	 */
	public static void writeDate(JsonWriter writer, String jsonProperty, Date datCurrent, boolean writeEmpty) throws IOException, JsonException {
		writeDate(writer, jsonProperty, datCurrent, writeEmpty ? JSONEmptyValueStrategy.NULL : JSONEmptyValueStrategy.NOPROPERTY);
	}

	/**
	 * Writes a Double value for a given property
	 * 
	 * @param writer
	 * @param jsonProperty
	 * @param dblValue
	 * @param writeEmpty
	 * @throws IOException
	 * @throws JsonException
	 */
	public static void writeDouble(JsonWriter writer, String jsonProperty, Double dblValue, boolean writeEmpty) throws IOException, JsonException {
		writeDouble(writer, jsonProperty, dblValue, writeEmpty ? JSONEmptyValueStrategy.NULL : JSONEmptyValueStrategy.NOPROPERTY);
	}

	/**
	 * Writes a Boolean value for a given property
	 * 
	 * @param writer
	 * @param jsonProperty
	 * @param blValue
	 * @param writeEmpty
	 * @throws IOException
	 * @throws JsonException
	 */
	public static void writeBoolean(JsonWriter writer, String jsonProperty, Boolean blValue, boolean writeEmpty) throws IOException, JsonException {
		writeBoolean(writer, jsonProperty, blValue, writeEmpty ? JSONEmptyValueStrategy.NULL : JSONEmptyValueStrategy.NOPROPERTY);
	}

	/**
	 * Writes a Boolean value for a given property
	 * 
	 * @param writer
	 * @param strJSONProperty
	 * @param blValue
	 * @param strategy
	 * @throws IOException
	 * @throws JsonException
	 */
	public static void writeBoolean(JsonWriter writer, String strJSONProperty, Boolean blValue, JSONEmptyValueStrategy strategy) throws IOException, JsonException {
		if (blValue != null) {
			writer.startProperty(strJSONProperty);
			writer.outBooleanLiteral(blValue);
			writer.endProperty();

		} else {
			writeEmpty(writer, strJSONProperty, strategy);
		}

	}

	/**
	 * Writes a Date value for a given property
	 * 
	 * @param writer
	 * @param strJSONProperty
	 * @param datValue
	 * @param strategy
	 * @throws IOException
	 * @throws JsonException
	 */
	public static void writeDate(JsonWriter writer, String strJSONProperty, Date datValue, JSONEmptyValueStrategy strategy) throws IOException, JsonException {
		if (datValue != null) {
			writer.startProperty(strJSONProperty);
			writer.outDateLiteral(datValue);
			writer.endProperty();

		} else {
			writeEmpty(writer, strJSONProperty, strategy);
		}

	}

	/**
	 * Writes a Double value for a given property
	 * 
	 * @param writer
	 * @param strJSONProperty
	 * @param nValue
	 * @param strategy
	 * @throws IOException
	 * @throws JsonException
	 */
	public static void writeDouble(JsonWriter writer, String strJSONProperty, Double nValue, JSONEmptyValueStrategy strategy) throws IOException, JsonException {
		if (nValue != null) {
			writer.startProperty(strJSONProperty);
			writer.outNumberLiteral(nValue);
			writer.endProperty();

		} else {
			writeEmpty(writer, strJSONProperty, strategy);
		}

	}

	/**
	 * Writes a Integer value for a given property
	 * 
	 * @param writer
	 * @param strJSONProperty
	 * @param nValue
	 * @param strategy
	 * @throws IOException
	 * @throws JsonException
	 */
	public static void writeInt(JsonWriter writer, String strJSONProperty, Integer nValue, JSONEmptyValueStrategy strategy) throws IOException, JsonException {
		if (nValue != null) {
			writer.startProperty(strJSONProperty);
			writer.outIntLiteral(nValue);
			writer.endProperty();

		} else {
			writeEmpty(writer, strJSONProperty, strategy);
		}

	}

	/**
	 * Writes a String value for a given property
	 * 
	 * @param writer
	 * @param strJSONProperty
	 * @param strValue
	 * @param strategy
	 * @throws IOException
	 * @throws JsonException
	 */
	public static void writeString(JsonWriter writer, String strJSONProperty, String strValue, JSONEmptyValueStrategy strategy) throws IOException, JsonException {
		if (strValue != null) {
			writer.startProperty(strJSONProperty);
			writer.outStringLiteral(strValue);
			writer.endProperty();

		} else {
			writeEmpty(writer, strJSONProperty, strategy);
		}
	}

	/**
	 * Writes a Empty value for a given property
	 * 
	 * @param writer
	 * @param jsonProperty
	 * @param strategy
	 * @throws IOException
	 * @throws JsonException
	 */
	private static void writeEmpty(JsonWriter writer, String jsonProperty, JSONEmptyValueStrategy strategy) throws IOException, JsonException {
		if (strategy != JSONEmptyValueStrategy.NOPROPERTY) {
			writer.startProperty(jsonProperty);
			strategy.writeJSONValue(writer);
			writer.endProperty();

		}
	}
}
