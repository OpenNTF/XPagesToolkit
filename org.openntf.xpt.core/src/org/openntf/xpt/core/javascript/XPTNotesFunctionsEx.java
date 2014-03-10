package org.openntf.xpt.core.javascript;

/*
 * © Copyright IBM Corp. 2010
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

import java.util.List;

import org.openntf.xpt.core.beans.XPTI18NBean;

import com.ibm.commons.util.StringUtil;
import com.ibm.jscript.InterpretException;
import com.ibm.jscript.JSContext;
import com.ibm.jscript.JavaScriptException;
import com.ibm.jscript.engine.IExecutionContext;
import com.ibm.jscript.types.BuiltinFunction;
import com.ibm.jscript.types.FBSNull;
import com.ibm.jscript.types.FBSObject;
import com.ibm.jscript.types.FBSUtility;
import com.ibm.jscript.types.FBSValue;
import com.ibm.jscript.types.FBSValueVector;

/**
 * Extended Notes/Domino formula language.
 * <p>
 * This class implements a set of new functions available to the JavaScript
 * interpreter. They become available to Domino Designer in the category
 * "@NotesFunctionEx".
 * </p>
 */
public class XPTNotesFunctionsEx extends com.ibm.xsp.extlib.javascript.NotesFunctionsEx {

	// Functions IDs
	private static final int FCT_XPTI18NAllLang = 1001;
	private static final int FCT_XPTI18NValue = 1002;
	private static final int FCT_XPTI18NValueFor = 1003;
	private static final int FCT_XPTI18NCurrentLang = 1004;


	public XPTNotesFunctionsEx(JSContext jsContext) {

		super(jsContext);
		addFunction(FCT_XPTI18NAllLang, "@XPTAvailableLanguages", "():N");
		addFunction(FCT_XPTI18NValue, "@XPTLanguageValue", "(strkey:T):T");
		addFunction(FCT_XPTI18NValueFor, "@XPTLanguageValueFor", "(strkey:T, strLang:T):T");
		addFunction(FCT_XPTI18NCurrentLang, "@XPTMyLanguage", "():T");
	}

	private void addFunction(int index, String functionName, String... params) {
		createMethod(functionName, FBSObject.P_NODELETE | FBSObject.P_READONLY, new NotesFunction(getJSContext(), index, functionName, params));
	}

	public static class NotesFunction extends BuiltinFunction {

		private String functionName;
		private int index;

		NotesFunction(JSContext jsContext, int index, String functionName, String[] params) {

			super(jsContext);
			this.functionName = functionName;
			this.index = index;
		}

	
		@Override
		public FBSValue call(IExecutionContext context, FBSValueVector args, FBSObject _this) throws JavaScriptException {

			try {

				switch (index) {
				case FCT_XPTI18NAllLang: {
					if (args.size() < 1) {
						List<String> allLanguages = XPTI18NBean.get().getAllLanguages();
						if (allLanguages.size() > 0) {
							return FBSUtility.wrap(context.getJSContext(), allLanguages);
						}
						// we should never be here
						return FBSNull.nullValue;
					}
				}
					break;
				case FCT_XPTI18NValue: {
					if (args.size() == 1) {
						String strKey = args.get(0).stringValue();
						return FBSUtility.wrap(XPTI18NBean.get().getValue(strKey));
					}
				}
					break;

				case FCT_XPTI18NValueFor: {
					if (args.size() == 2) {
						String strKey = args.get(0).stringValue();
						String strLang = args.get(1).stringValue();
						return FBSUtility.wrap(XPTI18NBean.get().getValue(strKey, strLang));
					}
				}
					break;

				case FCT_XPTI18NCurrentLang: {
					if (args.size() < 1) {
						return FBSUtility.wrap(XPTI18NBean.get().getCurrentLanguage());
					}
				}
					break;

				default: {
					throw new InterpretException(null, StringUtil.format("Internal error: unknown function \'{0}\'", functionName)); // $NLX-NotesFunctionEx_InternalErrorUnknownFunction-1$
				}

				}

			} catch (Exception e) {
				throw new InterpretException(e, StringUtil.format("Error while executing function \'{0}\'", // $NLX-NotesFunctionEx_ErrorExecutingFunction-1$
						functionName));
			}
			throw new InterpretException(null, StringUtil.format("Cannot evaluate function \'{0}\'", functionName)); // $NLX-NotesFunctionEx_CannotEvaluateFunction-1$
		}
	}
}