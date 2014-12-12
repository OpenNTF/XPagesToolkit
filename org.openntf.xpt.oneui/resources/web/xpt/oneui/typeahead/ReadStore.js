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
		currentNames : {},
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
		_computeFormId : function tars_cfid(_id, _count, _sep) {
			if (!_id || _count <= 0) {
				return "";
			}
			_sep = _sep || ":";
			var _counter = 0;
			while (_counter != -1 && _count > 0) {
				_counter = String(_id).indexOf(_sep, _counter == 0 ? _counter : _counter + 1);
				_count = _count - 1;
			}
			return _counter != -1 && _id.substring(0, _counter);
		},
		_findForm : function tars_ff() {
			var _myNode = _dojo.byId(this.dataNode);
			while (_myNode) {
				_myNode = _dijit.registry.byNode(_myNode) || _dojo.byId(_myNode);
				_myNode = _myNode.domNode || _myNode;
				if (_myNode && _myNode.tagName && String(_myNode.tagName).toLowerCase() == "form") {
					return _dojo.byId(_myNode);
				}
				_myNode = _myNode.parentNode;
			}
			_myNode = this._computeFormId(_dojo.attr(_dojo.byId(this.dataNode), "id"), 2, ":");
			return _dojo.byId(_myNode);
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
			this.content["$$axtarget"] = this.ajaxId;
			if (!this.viewId && _a) {
					this.viewId = _a.elements["$$viewid"].value;
			}
			this.content["$$viewid"] = this.viewId;
		},
		fetch : function _intFetch(_caller) {
			var _query = _caller.query.name;
			if (_query && _query.length > 0 && -1 != _query.indexOf("*", _query.length - 1)) {
				_query = _query.substring(0, _query.length - 1);
			}
			if (_query.length < this.minChars) {
				this._retrievedCallbacks(_caller, []);
				return;
			}
			this.content["$$value"] = _query;
			var _args = {
				url : this.url,
				handleAs : "json",
				timeout : XSP.submitLatency,
				content : this.content,
				form : null,
				load : _dojo.hitch(this, this.retrieved, _caller),
				error : _dojo.hitch(_caller, _caller.onError)
			};
			var _result = _dojo.xhr(this.method, _args, false);
			return _result;
		},
		retrieved : function _intRetrieved(_caller, _resultData) {
			var tmp = document.createElement("div");
			tmp.innerHTML = this._buildHTLMResult(_resultData);
			var _res = _dojo.query("> ul", tmp);
			if (!_res || _res.length < 1) {
				this._retrievedCallbacks(_caller, []);
				return;
			}
			this.currentRoot = _res[0];
			this.currentNames = _resultData.names;
			var _resLI = _dojo.query("> li", this.currentRoot);
			var _start = _caller.start || 0, _end = ("count" in _caller && _caller.count != Infinity) ? (_13 + _caller.count) : _resLI.length;
			var _resLIPart = _resLI.slice(_start, _end);
			this._retrievedCallbacks(_caller, _resLIPart);
		},
		_retrievedCallbacks : function tars_rc(_caller, _resLIPart) {
			var _scope = _caller.scope || _dojo.global;
			if (_caller.onBegin) {
				_caller.onBegin.call(_scope, _resLIPart.length, _caller);
			}
			var _hasItem = _caller.onItem || false;
			if (_hasItem) {
				for (var i = 0; i < _resLIPart.length; i++) {
					_caller.onItem.call(_scope, _resLIPart[i], _caller);
				}
			}
			if (_caller.onComplete) {
				var _rp2 = _hasItem ? null : _resLIPart;
				_caller.onComplete.call(_scope, _rp2, _caller);
			}
		},
		getValue : function tars_gv(_value, _attr, _caller) {
			if ("label" == _attr) {
				return this.getLabel(_value);
			}
			return this._getFormalText(_value);
		},
		_getFormalText : function tars_gft(e) {
			var _rc = "";
			if ("informal" == e.className) {
				return _rc;
			}
			if (e.hasChildNodes()) {
				var _cnodes = e.childNodes;
				for (var i = 0; i < _cnodes.length; i++) {
					var _thisNode = _cnodes[i];
					if (1 == _thisNode.nodeType) {
						_rc += this._getFormalText(_thisNode);
					} else {
						if (3 == _thisNode.nodeType) {
							_rc += _thisNode.nodeValue;
						}
					}
				}
			}
			return _rc;
		},
		getLabel : function tars_gl(_value) {
			return _value.innerHTML;
		},
		getIdentity : function tars_gi(_it) {
			return _dojo.attr(_it, "value");
		},
		fetchItemByIdentity : function tars_fibi(_it) {
			var _el = _dojo.query("option[value='" + _it.identity + "']", this.currentRoot)[0];
			_it.onItem(_el);
		},
		close : function tars_c(_it) {
			return;
		},
		containsValue : function tars_f(_value, _attr, _search) {
			return _search == this.getValue(_value, _attr);
		},
		filter : function tars_fil(_value, _attr, _filter) {
			_filter(_attr, _value);
		},
		getAttributes : function tars_ga(_it) {
			return [ "value", "label" ];
		},
		getFeatures : function tars_gf() {
			return {
				"dojo.data.api.Read" : true
			};
		},
		getLabelAttributes : function tars_gla(_it) {
			return [ "label" ];
		},
		getValues : function tars_gvs(_2d, _2e) {
			return [ getValue(_2d, _2e) ];
		},
		hasAttribute : function tars_ha(_it, _attr) {
			return _attr == "value" || _attr == "label";
		},
		isItem : function tars_ii(_it) {
			return true;
		},
		isItemLoaded : function tars_iil(_it) {
			return true;
		},
		loadItem : function tars_li(_it) {
			return _it.item;
		},
		_buildHTLMResult: function _intBuildHTMLResult( _resultData) {
			var strRC = "<ul><li><span class='informal'>Suggestions:</span></li>";
			if (_resultData.status == "ok") {
				var _names = _resultData.names;
				if (_names) {
					for (var i = 0; i < _names.length; i++) {
						strRC += "<li>" +_names[i].linehl;
					}
				}
			}
			return strRC+"</ul>";
		},
		getValueNodeJSON: function _intgetValueNodeJSON( _strCode ) {
			var _names = this.currentNames;
			for (var i = 0; i < _names.length; i++) {
				if (_names[i].line == _strCode) {
					return _names[i];
				};
			}
			return null;	
		}
	});
});