package com.nrl.sinicainformationcenter;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;


import com.nrl.utility.ConnectNRLServer;
import com.nrl.utility.Constant;

public class RetrieveDataTask extends AsyncTask<JSONObject, JSONObject, JSONObject> {

	public interface UITask{
		void post_run(JSONObject object);
		void pre_run();
	};
	private UITask uiTask;
	public RetrieveDataTask(UITask uiTask){
		this.uiTask = uiTask;
	}

    @Override
	protected void onPreExecute() {
    	uiTask.pre_run();
		super.onPreExecute();
	}

	protected void onPostExecute(JSONObject object) {
			uiTask.post_run(object);
	
    }

	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		try {
			long t = System.currentTimeMillis()/1000;
			return ConnectNRLServer.connectServer(""+t,t,Constant.DATA_DURATION);
		} catch (ConnectTimeoutException e) {
			Log.d("RetrieveDataTask","ConnectTimeout");
			return null;
		}
	}

 };
