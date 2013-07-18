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

dojo.provide("xptrss.list.feedcontroller");
dojo.require("dijit._Widget");
dojo.require("dojox.dtl._Templated");
dojo.declare("xptrss.list.feedcontroller", [ dijit._Widget, dojox.dtl._Templated ], {
	proxyurl:null,
	feed : null,
	templateString : dojo.cache("xptrss.list.feedcontroller",	"../../html/rssTemplate.html"),
	targetid: null,
	postCreate: function() {
		var mySelf = this;
		var xhrArgs = {
				url : mySelf.proxyurl,
				handleAs : "json",
				preventCache : true,
				load : function(data) {
					mySelf.feed = data;
					mySelf.render();
					dojo.style(mySelf.targetid +"_feedLoader", {
						display : "none"
					});
				},
				error : function(error) {
					alert(error);
				}
			
			}
			var deferred = dojo.xhrGet(xhrArgs);
	}
});


// -- custom DTL filter to parse datetime objects in Internuts Explorer
// -- the built-in date tag of DTL will not work with ISO timestamps
dojo.provide('xptrss.dtl.filter.datetime');
dojo.require("dojo.date.locale");

dojo.mixin(xptrss.dtl.filter.datetime, {
	xptrssDateFilter: function(value) {
		var dt = dojo.date.stamp.fromISOString(value);
		return dojo.date.locale.format(dt, {selector: "date", formatLength: "medium"});
},

xptrssTimeFilter: function(value) {
	 	var dt = dojo.date.stamp.fromISOString(value);
	 	return dojo.date.locale.format(dt, {selector: "time", formatLength: "medium"});
}

});

dojox.dtl.register.filters('xptrss.dtl.filter', { datetime: ['xptrssDateFilter', 'xptrssTimeFilter'] });
// -- end of custom DTL filter
