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

if (!dojo._hasResource["xptoneui.typeahead.pre17.widget"]) {
	dojo._hasResource["xptoneui.typeahead.pre17.widget"] = true;
	dojo.provide("xptoneui.typeahead.pre17.widget");
	dojo.require("xptoneui.typeahead.pre17.ReadStore");
	dojo.require("dijit.form.ComboBox");
	dojo.declare("xptoneui.typeahead.pre17.widget", dijit.form.ComboBox, {
		hasDownArrow : false,
		autoComplete : false,
		queryExpr : "${0}",
		searchDelay : 400,
		separators : null,
		jscallback : "",
		myresult: null,
		constructor : function ta_ctor(_node, _value) {
			this.jscallback = _node.jsCallback;
		},
		postMixInProperties : function ta_pmip() {
			if (!this.store) {
				this.store = new xptoneui.typeahead.pre17.ReadStore({}, this.srcNodeRef);
			}
			this.inherited("postMixInProperties", arguments);
		},
		startup : function ta_s() {
			var n = this._buttonNode;
			if (n != null) {
				dojo.style(n, "display", "none");
			}
		},
		_getMenuLabelFromItem : function ta_gmlfi(_it) {
			return {
				html : true,
				label : this.store.getLabel(_it, this.searchAttr)
			};
		},
		_getQueryString : function ta_gqs(_search) {
			var _ind = this._indexAfterLastSeparator(_search);
			var _pos = (-1 == _ind) ? _search : _search.substring(_ind);
			return dojo.string.substitute(this.queryExpr, [ _pos ]);
		},
		_selectOption : function ta_so(_Event) {
			var _Target = null;
			if (!_Event) {
				_Event = {
					target : this._popupWidget.getHighlightedOption()
				};
			}
			if (!_Event.target) {
				this.setDisplayedValue(this.getDisplayedValue());
				return;
			} else {
				_Target = _Event.target;
			}
			if (!_Event.noHide) {
				if (typeof this._hideResultList == "function") {
					this._hideResultList();
				} else {
					this.closeDropDown();
				}
				this._setCaretPos(this.focusNode, this._getAutoCompleteText(_Target.item).length);
			}
			this._doSelect(_Target);
		},
		_getAutoCompleteText : function ta_gact(_9) {
			var _a = this.store.getValue(_9, this.searchAttr);
			var _b = this.focusNode.value;
			var _c = this._indexAfterLastSeparator(_b);
			if (-1 == _c) {
				return _a;
			}
			var _d = _b.substring(0, _c);
			var _e = _d + _a;
			return _e;
		},
		_indexAfterLastSeparator : function ta_ials(_f) {
			if (!this.separators || 0 == this.separators.length) {
				return -1;
			}
			var _10 = -1;
			for (var i = 0; i < this.separators.length; i++) {
				var _11 = this.separators[i];
				var _12 = _f.lastIndexOf(_11);
				if (-1 != _12) {
					_12 = _12 + _11.length;
					if (_12 > _10) {
						_10 = _12;
					}
				}
			}
			return _10;
		},
		_doSelect : function ta_ds(tgt) {
			this.item = tgt.item;
			var _value = this._getSelectValue(tgt);
			this.setValue(this._getSelectValue(tgt), true);
			
			var _node = this.store.getValueNodeJSON( _value );
			this.myresult = _node;
			
			if (this.myresult) {
				eval( this.jscallback+"('"+ this.myresult.label +"','"+ this.myresult.value +"')");
			}
		},
		_getSelectValue : function ta_gsv(tgt) {
			var _rc = this._getAutoCompleteText(tgt.item);
			if (this.separators && 0 < this.separators.length) {
				_rc = _rc + this.separators[0];
			}
			return _rc;
		},
		compositionend : function $DBhI_(evt) {
			var _14 = {
				charOrCode : 229
			};
			if (this.onkeypress) {
				this.onkeypress(_14);
			} else {
				this._onKeyPress(_14);
			}
		},
		_announceOption : function ta_ao(_Event) {
			if (_Event == null) {
				return;
			}
			this.focusNode.value = this.focusNode.value.substring(0, this._getCaretPos(this.focusNode));
			if (typeof this.dropDown != "undefined") {
				this._popupWidget = this.dropDown;
			}
			var _check;
			if (_Event == this._popupWidget.nextButton || _Event == this._popupWidget.previousButton) {
				_chekc = _Event.innerHTML;
			} else {
				_check = this._getAutoCompleteText(_Event.item);
			}
			dijit.setWaiState(this.focusNode, "activedescendant", dojo.attr(_Event, "id"));
			this._autoCompleteText(_check);
		}
	});
}