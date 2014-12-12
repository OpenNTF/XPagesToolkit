package org.openntf.xpt.core.json;

import java.io.IOException;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.domino.services.util.JsonWriter;

public enum JSONEmptyValueStrategy {
	NULL, EMPTYSTRING, NOPROPERTY;

	public static JSONEmptyValueStrategy getStrategy(boolean showEmptyValue, boolean showEmptyValueAsString) {
		if (showEmptyValueAsString) {
			return EMPTYSTRING;
		}
		if (showEmptyValue) {
			return NULL;
		}
		return NOPROPERTY;
	}

	public void writeJSONValue(JsonWriter jsWriter) throws IOException, JsonException {
		if (this == JSONEmptyValueStrategy.EMPTYSTRING) {
			jsWriter.outStringLiteral("");
			return;
		}
		if (this == JSONEmptyValueStrategy.NULL) {
			jsWriter.outNull();
		}
		
	}
}
