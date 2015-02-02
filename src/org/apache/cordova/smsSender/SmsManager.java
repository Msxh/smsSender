package org.apache.cordova.smsSender;

import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

public class SmsManager {
	private SmsListener smsListener;
	private List<PhoneInfo> phoneList = new ArrayList<PhoneInfo>();

	public void setSmsListenerMessage(SmsListener smsListen) {
		this.smsListener = smsListen;
	}

	public void sendSMS(String phoneNumber, String message) {
		// 获取短信管理器
		android.telephony.SmsManager smsManager = android.telephony.SmsManager
				.getDefault();
		// 拆分短信内容（手机短信长度限制）
		List<String> divideContents = smsManager.divideMessage(message);
		for (String text : divideContents) {
			smsManager.sendTextMessage(phoneNumber, null, text, null, null);
		}
	}

	public void parsePhone(String msg) {
		try {
			JSONObject jobj = new JSONObject(msg);
			JSONArray jarray = jobj.getJSONArray("info");

			int len = jarray.length();
			for (int i = 0; i < len; i++) {
				JSONObject obj = jarray.getJSONObject(i);
				PhoneInfo p = new PhoneInfo();
				p.message = obj.getString("message");
				p.phone = obj.getString("phone");
				phoneList.add(p);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void startSend() {
		int index = 0;
		int total = phoneList.size();
		SmsUtils.smsTotal = total;
		for (int i = 0; i < total; i++) {
			while (SmsUtils.isPaused) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			while (SmsUtils.isCanceled)
				return;
			sendSMS(phoneList.get(i).phone, phoneList.get(i).message); // 发送成功
			SmsUtils.smsIndex = i + 1;
			SmsUtils.smsStatus = "已发送 < " + SmsUtils.smsIndex + " >条信息";
			smsListener.Message("发送成功");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		SmsUtils.smsStatus = "信息全部发送完毕";

	}

}
