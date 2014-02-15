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
		function(_dijit, _dojo, _domattr) {
			_dojo.provide("xptoneui.typeahead.widget");
			_dojo.require("xptoneui.typeahead.ReadStore");
			_dojo.require("dijit.form.ComboBox");
			_dojo.declare("xptoneui.typeahead.widget", _dijit.form.ComboBox, {
				hasDownArrow : false,
				autoComplete : false,
				queryExpr : "${0}",
				searchDelay : 400,
				separators : null,
				jscallback : "",
				myresult: null,
				constructor : function ta_ctor(_node, _values) {
					this.jscallback = _node.jsCallback;
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
						_dojo.style(n, "display", "none");
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
					return _dojo.string.substitute(this.queryExpr, [ _pos ]);
				},
				_startSearch : function ta_ss(_search) {
					var _start = this._getQueryString(_search);
					this.inherited("_startSearch", [ _start ]);
				},
				_selectOption : function ta_so(_it) {
					this.closeDropDown();
					if (_it) {
						this._announceOption(_it, true);
					}
					this._setCaretPos(this.focusNode, this.focusNode.value.length);
					this._handleOnChange(this.value, true);
					if (this.myresult) {
						eval( this.jscallback+"('"+ this.myresult.label +"','"+ this.myresult.value +"')");
					}
				},
				_getCombinedOldAndItemValue : function ta_gact(_search, _for) {
					var _ind = this._indexAfterLastSeparator(_search);
					if (-1 == _ind) {
						return _for;
					}
					var _pos = _search.substring(0, _ind);
					var _rc = _pos + _for;
					return _rc;
				},
				_indexAfterLastSeparator : function ta_ials(_search) {
					if (!this.separators || 0 == this.separators.length) {
						return -1;
					}
					var _rc = -1;
					for (var i = 0; i < this.separators.length; i++) {
						var _point = this.separators[i];
						var _p2 = _search.lastIndexOf(_point);
						if (-1 != _p2) {
							_p2 = _p2 + _14.length;
							if (_p2 > _rc) {
								_rc = _p2;
							}
						}
					}
					return _rc;
				},
				compositionend : function ta_ce(evt) {
					var _rc = {
						charOrCode : 229
					};
					if (this.onkeypress) {
						this.onkeypress(_rc);
					} else {
						this._onKeyPress(_rc);
					}
				},
				_announceOption : function ta_ao(_it, _mv) {
					if (!_it) {
						return;
					}
					var _value = this.focusNode.value;
					var _lenght = this._lastInput.length;
					var _pos = this._indexAfterLastSeparator(_value);
					if (-1 != _pos) {
						_lenght = _pos + _lenght;
					}
					this.focusNode.value = this.focusNode.value.substring(0, _lenght);
					var _rc;
					if (_it == this.dropDown.nextButton || _it == this.dropDown.previousButton) {
						_rc = _it.innerHTML;
						this.item = undefined;
						this.value = "";
					} else {
						var _itm = this.dropDown.items[_it.getAttribute("item")];
						var _rvalue = (this.store._oldAPI ? this.store.getValue(_itm, this.searchAttr) : _itm[this.searchAttr]).toString();
						var _rc = _rvalue;
						if (this.separators && 0 < this.separators.length) {
							var _val2 = this.focusNode.value;
							_rc = this._getCombinedOldAndItemValue(_val2, _rvalue);
							if (_mv != "undefined" && _mv) {
								_rc = _rc + this.separators[0];
							}
						}
						if (_rc && _rc != "") {
							var _node = this.store.getValueNodeJSON( _rc );
							this.myresult = _node;
						}
						this.set("item", _itm, false, _rc);
					}
					this.focusNode.setAttribute("aria-activedescendant", _domattr.get(_it, "id"));
					this._autoCompleteText(_rc);
				}
			});
		});