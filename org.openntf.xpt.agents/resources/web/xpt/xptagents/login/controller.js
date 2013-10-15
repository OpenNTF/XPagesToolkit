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
dojo.provide("xptagents.login.controller");

dojo.declare("xptagents.login.controller", null, {
	serviceurl : null,
	targetid : null,
	refreshid: null,
	// The constructor
	constructor : function(args) {
		dojo.safeMixin(this, args);
	},

	checkLogin : function() {
		var context = this;
		var postData = {
			"method" : "startAgent",
			"agentname" : this.agentname,
			"arguments" : argMapping
		};
		var xhrArgs = { // submit to server
			url : this.serviceurl,
			postData : dojo.toJson(postData),
			handleAs : "json",
			headers : {
				"Content-Type" : "application/json"
			},
			load : function(response) {
				if (response.status == "ok") {
					context.jobid = response.jobid;
					setTimeout(function() {
						context.updatePB();
					}, 500);
				} else {
					alert("Could not start " + this.agentname + "\nreason= "
							+ response.error);
				}
			},
			error : function(error) {
				alert(error);
			}
		};
		dojo.xhrPost(xhrArgs);

	},
	showLoginBox : function() {
		var msgBox = dojo.byId(this.targetid);
		dojo.style(this.targetid, "opacity", "0");
		dojo.style(this.targetid, {
			display : "block"
		});
		dojo.fadeIn({
			node : msgBox
		}).play();
	},
	hideLoginBox : function() {
		var msgBox = dojo.byId(this.targetid);
		dojo.fadeOut({
			node : msgBox,
			onEnd : function() {
				dojo.style(msgBox.id, {
					display : "none"
				});
			}
		}).play();

	}
});