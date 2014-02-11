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

define("xptoneui.typeahead.ReadStore", [ "dijit", "dojo" ], function(_dijit, _dojo) {
	_dojo.provide("xptoneui.typeahead.ReadStore");
	_dojo.declare("xptoneui.typeahead.ReadStore", null, {
		mode : "",
		ajaxId : "",
		viewId : "",
		minChars : 1,
		content : {},
		url : "",
		method : "GET",
		constructor : function tars_ctor(_args, _node) {
			this.dataNode = _node;
			this.url = _args.url;
			//this.method = _args.method;
			this.mode = _args.mode || "full";
			this.ajaxId = _args.ajaxId;
			this.viewId = _args.viewId;
			this.minChars = (_args.minChars) ? _args.minChars : 1;
			this.content = (_args.content) ? _args.content : {};
			this.doInit();
		},
		_computeFormId : function tars_cfid(_5, _6, _7) {
			if (!_5 || _6 <= 0) {
				return "";
			}
			_7 = _7 || ":";
			var _8 = 0;
			while (_8 != -1 && _6 > 0) {
				_8 = String(_5).indexOf(_7, _8 == 0 ? _8 : _8 + 1);
				_6 = _6 - 1;
			}
			return _8 != -1 && _5.substring(0, _8);
		},
		_findForm : function tars_ff() {
			var _9 = _dojo.byId(this.dataNode);
			while (_9) {
				_9 = _dijit.registry.byNode(_9) || _dojo.byId(_9);
				_9 = _9.domNode || _9;
				if (_9 && _9.tagName && String(_9.tagName).toLowerCase() == "form") {
					return _dojo.byId(_9);
				}
				_9 = _9.parentNode;
			}
			_9 = this._computeFormId(_dojo.attr(_dojo.byId(this.dataNode), "id"), 2, ":");
			return _dojo.byId(_9);
		},
		doInit : function tars_i() {
			var _a = this._findForm();
			if (!(_dojo.byId(_a))) {
				console.error("Could not find containing FORM element.");
				return;
			}
			if (!this.url && _a) {
				this.url = _a.getAttribute("action");
			}
			if (this.mode == "full" || this.mode == "partial") {
				this.content["$$ajaxmode"] = "typeahead";
				this.content["$$ajaxid"] = this.ajaxId;
			} else {
					this.content["$$axtarget"] = this.ajaxId;
			}
			if (!this.viewId && _a) {
					this.viewId = _a.elements["$$viewid"].value;
			}
			this.content["$$viewid"] = this.viewId;
		},
		fetch : function tars_f(_b) {
			var _c = _b.query.name;
			if (_c && _c.length > 0 && -1 != _c.indexOf("*", _c.length - 1)) {
				_c = _c.substring(0, _c.length - 1);
			}
			if (_c.length < this.minChars) {
				this._retrievedCallbacks(_b, []);
				return;
			}
			this.content["$$value"] = _c;
			var _d = {
				url : this.url,
				handleAs : "text",
				timeout : XSP.submitLatency,
				content : this.content,
				form : null,
				load : _dojo.hitch(this, this.retrieved, _b),
				error : _dojo.hitch(_b, _b.onError)
			};
			var _e = _dojo.xhr(this.method, _d, false);
			return _e;
		},
		retrieved : function tars_r(_f, _10) {
			var tmp = document.createElement("div");
			tmp.innerHTML = _10;
			var _11 = _dojo.query("> ul", tmp);
			if (!_11 || _11.length < 1) {
				this._retrievedCallbacks(_f, []);
				return;
			}
			this.currentRoot = _11[0];
			var _12 = _dojo.query("> li", this.currentRoot);
			var _13 = _f.start || 0, end = ("count" in _f && _f.count != Infinity) ? (_13 + _f.count) : _12.length;
			var _14 = _12.slice(_13, end);
			this._retrievedCallbacks(_f, _14);
		},
		_retrievedCallbacks : function tars_rc(_15, _16) {
			var _17 = _15.scope || _dojo.global;
			if (_15.onBegin) {
				_15.onBegin.call(_17, _16.length, _15);
			}
			var _18 = _15.onItem || false;
			if (_18) {
				for (var i = 0; i < _16.length; i++) {
					_15.onItem.call(_17, _16[i], _15);
				}
			}
			if (_15.onComplete) {
				var _19 = _18 ? null : _16;
				_15.onComplete.call(_17, _19, _15);
			}
		},
		getValue : function tars_gv(_1a, _1b, _1c) {
			if ("label" == _1b) {
				return this.getLabel(_1a);
			}
			return this._getFormalText(_1a);
		},
		_getFormalText : function tars_gft(e) {
			var _1d = "";
			if ("informal" == e.className) {
				return _1d;
			}
			if (e.hasChildNodes()) {
				var _1e = e.childNodes;
				for (var i = 0; i < _1e.length; i++) {
					var _1f = _1e[i];
					if (1 == _1f.nodeType) {
						_1d += this._getFormalText(_1f);
					} else {
						if (3 == _1f.nodeType) {
							_1d += _1f.nodeValue;
						}
					}
				}
			}
			return _1d;
		},
		getLabel : function tars_gl(_20) {
			return _20.innerHTML;
		},
		getIdentity : function tars_gi(_21) {
			return _2.attr(_21, "value");
		},
		fetchItemByIdentity : function tars_fibi(_22) {
			var _23 = _dojo.query("option[value='" + _22.identity + "']", this.currentRoot)[0];
			_22.onItem(_23);
		},
		close : function tars_c(_24) {
			return;
		},
		containsValue : function tars_f(_25, _26, _27) {
			return _27 == this.getValue(_25, _26);
		},
		filter : function tars_fil(_28, _29, _2a) {
			_2a(_29, _28);
		},
		getAttributes : function tars_ga(_2b) {
			return [ "value", "label" ];
		},
		getFeatures : function tars_gf() {
			return {
				"dojo.data.api.Read" : true
			};
		},
		getLabelAttributes : function tars_gla(_2c) {
			return [ "label" ];
		},
		getValues : function tars_gvs(_2d, _2e) {
			return [ getValue(_2d, _2e) ];
		},
		hasAttribute : function tars_ha(_2f, _30) {
			return _30 == "value" || _30 == "label";
		},
		isItem : function tars_ii(_31) {
			return true;
		},
		isItemLoaded : function tars_iil(_32) {
			return true;
		},
		loadItem : function tars_li(_33) {
			return _33.item;
		}
	});
});