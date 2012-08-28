package com.nrl.utility;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ConnectNRLServer {
	
	
	static public JSONObject connectServer(String object,long startTime,long duration) throws ConnectTimeoutException{
		JSONObject feedback = null;
		HttpPost httpRequest = new HttpPost(Constant.CONNECT_PATH+"?datetime="+startTime+"&duration="+duration);
		MultipartEntity entity = new MultipartEntity();
		try {
			entity.addPart("data", new StringBody(object,Charset.forName("UTF-8")));
			httpRequest.setEntity(entity);
			
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, Constant.CONNECT_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParameters, Constant.WAIT_DATA_TIMEOUT);

			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			HttpResponse httpResponse = httpClient.execute(httpRequest);

			//if (entity != null) { entity.consumeContent(); }
			String result;
			switch(httpResponse.getStatusLine().getStatusCode())
			{
			case 200:
				result = EntityUtils.toString(httpResponse.getEntity());
				Log.i("feedback",result);
				feedback = new JSONObject(result);						
				break;
			default:
				result = EntityUtils.toString(httpResponse.getEntity());
				Log.i("feedback","not 200 "+result);
				feedback = new JSONObject(result);


			}

		} catch (UnsupportedEncodingException e) { e.printStackTrace();
		} catch (ClientProtocolException e) { e.printStackTrace();
		} catch (IOException e) { e.printStackTrace(); 
		} catch (NumberFormatException e){ e.printStackTrace(); 
		} catch (JSONException e) { 
			e.printStackTrace();
			Log.i("feedback","JSON error");
			return null;
		} 


		return feedback;
	}
}
