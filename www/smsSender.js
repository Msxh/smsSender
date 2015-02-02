cordova.define("org.apache.cordova.smsSender.SmsSender", function(require, exports, module) { 

var exec = require('cordova/exec');
var platform = require('cordova/platform');
var cordova = require('cordova');

var smsSender= {
    send:function(param) {
        exec(null, null, "SmsSender", "send", [param]);
    },
	getSmsStatus:function(successCallback, errorCallback){
	        exec(successCallback, errorCallback, "SmsSender", "getSmsStatus", []);
	},
	cancelsend:function(){
	        exec(null, null, "SmsSender", "cancelsend", []);
	},
	pausesend:function(){
	        exec(null, null, "SmsSender", "pausesend", []);
	}
};
module.exports = smsSender;

});