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

if (!dojo._hasResource["xptoneui.typeahead.pre17.ReadStore"]) {
	dojo._hasResource["xptoneui.typeahead.pre17.ReadStore"] = true;
	dojo.provide("xptoneui.typeahead.pre17.ReadStore");
	dojo.declare("xptoneui.typeahead.pre17.ReadStore", null, {
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
			this.ajaxId = _args.ajaxId;
			this.viewId = _args.viewId;
			this.minChars = (_args.minChars) ? _args.minChars : 1;
			this.content = (_args.content) ? _args.content : {};
			this.doInit();
		},
		doInit : function tars_i() {
			var _form = null;
			var _node = this.dataNode;
			while (_node) {
				if (_node.tagName.toLowerCase() == "form") {
					_form = _node;
					break;
				}
				if (_node.tagName.toLowerCase() == "body") {
					break;
				}
				_node = _node.parentNode;
			}
			if (!this.url && _form) {
				this.url = _form.getAttribute("action");
			}
			if (!this.ajaxId) {
				this.ajaxId = this.dataNode.getAttribute("id");
			}
			this.content["$$axtarget"] = this.ajaxId;
			if (!this.viewId && _form) {
				this.viewId = _form.elements["$$viewid"].value;
			}
			this.content["$$viewid"] = this.viewId;
		},
		fetch : function tars_f(_caller) {
			var _search = _caller.query.name;
			if (_search.length < this.minChars) {
				_caller.onComplete([], _caller);
				return;
			}
			this.content["$$value"] = _search;
			var _args = {
				url : this.url,
				handleAs : "json",
				timeout : XSP.submitLatency,
				content : this.content,
				form : null,
				load : dojo.hitch(this, this.retrieved, _caller),
				error : dojo.hitch(_caller, _caller.onError)
			};
			dojo.xhr(this.method, _args, false);
			return _caller;
		},
		retrieved : function tars_r(_caller, _resultData) {
			var tmp = document.createElement("div");
			tmp.innerHTML = this._buildHTLMResult(_resultData);
			var _res = dojo.query("> ul", tmp);
			if (!_res || _res.length < 1) {
				_caller.onComplete([], _caller);
				return;
			}
			this.currentRoot = _res[0];
			this.currentNames = _resultData.names;
			var _resLI = dojo.query("> li", this.currentRoot);
			var _start = _caller.start || 0, _end = ("count" in _caller && _caller.count != Infinity) ? (_13 + _caller.count) : _resLI.length;
			var _resLIPart = _resLI.slice(_start, _end);
			_caller.onComplete(_resLIPart, _caller);
		},
		getValue : function tars_gv(_f, _10, _11) {
			return this._getFormalText(_f);
		},
		_getFormalText : function tars_gft(e) {
			var _12 = "";
			if ("informal" == e.className) {
				return _12;
			}
			if (e.hasChildNodes()) {
				var _13 = e.childNodes;
				for (var i = 0; i < _13.length; i++) {
					var _14 = _13[i];
					if (1 == _14.nodeType) {
						_12 += this._getFormalText(_14);
					} else {
						if (3 == _14.nodeType) {
							_12 += _14.nodeValue;
						}
					}
				}
			}
			return _12;
		},
		getLabel : function tars_gl(_15) {
			return _15.innerHTML;
		},
		getIdentity : function tars_gi(_16) {
			return dojo.attr(_16, "value");
		},
		fetchItemByIdentity : function tars_fibi(_17) {
			var _18 = dojo.query("option[value='" + _17.identity + "']", this.currentRoot)[0];
			_17.onItem(_18);
		},
		close : function tars_c(_19) {
			return;
		},
		isItemLoaded : function tars_iil(_1a) {
			return true;
		},

		_buildHTLMResult : function _intBuildHTMLResult(_resultData) {
			var strRC = "<ul><li><span class='informal'>Suggestions:</span></li>";
			if (_resultData.status == "ok") {
				var _names = _resultData.names;
				if (_names) {
					for (var i = 0; i < _names.length; i++) {
						strRC += "<li>" + _names[i].linehl;
					}
				}
			}
			return strRC + "</ul>";
		},
		getValueNodeJSON : function _intgetValueNodeJSON(_strCode) {
			var _names = this.currentNames;
			for (var i = 0; i < _names.length; i++) {
				if (_names[i].line == _strCode) {
					return _names[i];
				}
				;
			}
			return null;
		}

	});
}