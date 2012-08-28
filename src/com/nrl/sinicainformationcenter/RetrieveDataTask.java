package com.nrl.sinicainformationcenter;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;


import com.nrl.utility.ConnectNRLServer;
import com.nrl.utility.Constant;

public class RetrieveDataTask extends AsyncTask<JSONObject, JSONObject, JSONObject> {

	public interface PostTask{
		void run(JSONObject object);
	};
	private PostTask postTask;
	public RetrieveDataTask(PostTask postTask){
		this.postTask = postTask;
	}

    protected void onPostExecute(JSONObject object) {
			postTask.run(object);
	
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
