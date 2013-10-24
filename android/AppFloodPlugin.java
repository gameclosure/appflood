package com.tealeaf.plugin.plugins;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.tealeaf.EventQueue;
import com.tealeaf.TeaLeaf;
import com.tealeaf.logger;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import java.util.HashMap;

import com.tealeaf.plugin.IPlugin;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.tealeaf.EventQueue;
import com.tealeaf.event.*;

import com.appflood.AppFlood;
import com.appflood.AppFlood.AFEventDelegate;
import com.appflood.AppFlood.AFRequestDelegate;

public class AppFloodPlugin implements IPlugin, AFEventDelegate {
	Context _ctx;
	HashMap<String, String> manifestKeyMap = new HashMap<String,String>();
    Activity afActivity;

	public AppFloodPlugin() {
	}

    public class AppFloodEvent extends com.tealeaf.event.Event {
        boolean success;
        String message;
        int errorCode;

        public AppFloodEvent(String s) {
            super("appflood");
            this.message = s;
        }

        public AppFloodEvent(String s, int ec) {
            super("appflood");
            this.message = s;
            this.errorCode = ec;
            logger.log("{appflood} event failed: message='" + s + "' code=" + ec);
        }
    }


	public void onCreateApplication(Context applicationContext) {
		_ctx = applicationContext;
	}

    public void showInterstitial(String jsonData) {
        AppFlood.showFullScreen(afActivity);
    }

	public void onCreate(Activity activity, Bundle savedInstanceState) {
        afActivity = activity;
		PackageManager manager = activity.getBaseContext().getPackageManager();
		String[] keys = {"appFloodAppKey", "appFloodSecretKey"};
		try {
			Bundle meta = manager.getApplicationInfo(activity.getApplicationContext().getPackageName(),
					PackageManager.GET_META_DATA).metaData;
			for (String k : keys) {
				if (meta.containsKey(k)) {
					manifestKeyMap.put(k, meta.get(k).toString());
				}
			}
		} catch (Exception e) {
			logger.log("Exception while loading manifest keys:", e);
		}

		String AppKey = manifestKeyMap.get("appFloodAppKey");
		String SecretKey = manifestKeyMap.get("appFloodSecretKey");

		logger.log("{appflood} Installing AppFlood for AppKey:", AppKey);

		// Connect with the AppFlood server.
		AppFlood.initialize(_ctx, AppKey, SecretKey, AppFlood.AD_ALL);
        AppFlood.setEventDelegate(this);
	}

    public void onClick(JSONObject _) {
        EventQueue.pushEvent(new AppFloodEvent("appFlood ad clicked"));
    }

    public void onClose(JSONObject _) {
        EventQueue.pushEvent(new AppFloodEvent("appFlood ad closed"));
    }

	public void onResume() {
	}

	public void onStart() {
	}

	public void onPause() {
	}

	public void onStop() {
	}

	public void onDestroy() {
	}

	public void onNewIntent(Intent intent) {
	}

	public void setInstallReferrer(String referrer) {
	}

	public void onActivityResult(Integer request, Integer result, Intent data) {
	}

	public void logError(String error) {
	}

	public boolean consumeOnBackPressed() {
		return true;
	}

	public void onBackPressed() {
	}
}

