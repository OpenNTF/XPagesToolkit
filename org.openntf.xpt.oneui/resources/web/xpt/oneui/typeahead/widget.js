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

define("xptoneui.typeahead.widget", [ "dijit", "dojo", "dojo/dom-attr", "dojo/require!xptoneui/typeahead/ReadStore,dijit/form/ComboBox" ],
		function(_1, _2, _3) {
			_2.provide("xptoneui.typeahead.widget");
			_2.require("xptoneui.typeahead.ReadStore");
			_2.require("dijit.form.ComboBox");
			_2.declare("xptoneui.typeahead.widget", _1.form.ComboBox, {
				hasDownArrow : false,
				autoComplete : false,
				queryExpr : "${0}",
				searchDelay : 400,
				separators : null,
				constructor : function ta_ctor(_4, _5) {
				},
				postMixInProperties : function ta_pmip() {
					if (!this.store) {
						this.store = new xptoneui.typeahead.ReadStore({}, this.srcNodeRef);
					}
					this.inherited("postMixInProperties", arguments);
				},
				startup : function ta_s() {
					var n = this._buttonNode;
					if (n != null) {
						_2.style(n, "display", "none");
					}
				},
				_getMenuLabelFromItem : function ta_gmlfi(_6) {
					return {
						html : true,
						label : this.store.getLabel(_6, this.searchAttr)
					};
				},
				_getQueryString : function ta_gqs(_7) {
					var _8 = this._indexAfterLastSeparator(_7);
					var _9 = (-1 == _8) ? _7 : _7.substring(_8);
					return _2.string.substitute(this.queryExpr, [ _9 ]);
				},
				_startSearch : function ta_ss(_a) {
					var _b = this._getQueryString(_a);
					this.inherited("_startSearch", [ _b ]);
				},
				_selectOption : function ta_so(_c) {
					this.closeDropDown();
					if (_c) {
						this._announceOption(_c, true);
					}
					this._setCaretPos(this.focusNode, this.focusNode.value.length);
					this._handleOnChange(this.value, true);
				},
				_getCombinedOldAndItemValue : function ta_gact(_d, _e) {
					var _f = this._indexAfterLastSeparator(_d);
					if (-1 == _f) {
						return _e;
					}
					var _10 = _d.substring(0, _f);
					var _11 = _10 + _e;
					return _11;
				},
				_indexAfterLastSeparator : function ta_ials(_12) {
					if (!this.separators || 0 == this.separators.length) {
						return -1;
					}
					var _13 = -1;
					for (var i = 0; i < this.separators.length; i++) {
						var _14 = this.separators[i];
						var _15 = _12.lastIndexOf(_14);
						if (-1 != _15) {
							_15 = _15 + _14.length;
							if (_15 > _13) {
								_13 = _15;
							}
						}
					}
					return _13;
				},
				compositionend : function ta_ce(evt) {
					var _16 = {
						charOrCode : 229
					};
					if (this.onkeypress) {
						this.onkeypress(_16);
					} else {
						this._onKeyPress(_16);
					}
				},
				_announceOption : function ta_ao(_17, _18) {
					if (!_17) {
						return;
					}
					var _19 = this.focusNode.value;
					var _1a = this._lastInput.length;
					var _1b = this._indexAfterLastSeparator(_19);
					if (-1 != _1b) {
						_1a = _1b + _1a;
					}
					this.focusNode.value = this.focusNode.value.substring(0, _1a);
					var _1c;
					if (_17 == this.dropDown.nextButton || _17 == this.dropDown.previousButton) {
						_1c = _17.innerHTML;
						this.item = undefined;
						this.value = "";
					} else {
						var _1d = this.dropDown.items[_17.getAttribute("item")];
						var _1e = (this.store._oldAPI ? this.store.getValue(_1d, this.searchAttr) : _1d[this.searchAttr]).toString();
						var _1c = _1e;
						if (this.separators && 0 < this.separators.length) {
							var _1f = this.focusNode.value;
							_1c = this._getCombinedOldAndItemValue(_1f, _1e);
							if (_18 != "undefined" && _18) {
								_1c = _1c + this.separators[0];
							}
						}
						this.set("item", _1d, false, _1c);
					}
					this.focusNode.setAttribute("aria-activedescendant", _3.get(_17, "id"));
					this._autoCompleteText(_1c);
				}
			});
		});