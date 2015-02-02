package org.apache.cordova.smsSender;

import java.util.List;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SmsSender extends CordovaPlugin {

	SmsManager smsManager;
	JSONObject obj = new JSONObject();

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		// TODO Auto-generated method stub
		super.initialize(cordova, webView);
		smsManager = new SmsManager();
	}

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		// TODO Auto-generated method stub

		if (action.equals("send")) {
			smsManager.setSmsListenerMessage(new SmsListener() {

				@Override
				public void Message(String msg) {
					// TODO Auto-generated method stub
					parseToJson();
				}
			});

			String msg = args.getString(0);
			if (smsManager == null)
				smsManager = new SmsManager();
			smsManager.parsePhone(msg);
			smsManager.startSend();
			return true;
		} else if (action.equals("getSmsStatus")) {
			callbackContext.success(obj);
			return true;
		} else if (action.equals("cancelsend")) {
			SmsUtils.isCanceled = true;
			callbackContext.success();
			return true;

		} else if (action.equals("pausesend")) {
			SmsUtils.isPaused = !SmsUtils.isPaused;
			callbackContext.success();
			return true;

		}
		return true;
	}

	protected void parseToJson() {
		// TODO Auto-generated method stub
		try {
			obj.put("smsIndex", SmsUtils.smsIndex);
			obj.put("smsTotal", SmsUtils.smsTotal);
			obj.put("smsStatus", SmsUtils.smsStatus);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
