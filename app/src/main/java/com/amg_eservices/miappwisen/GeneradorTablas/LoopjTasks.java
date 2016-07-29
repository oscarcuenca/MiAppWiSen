package com.amg_eservices.miappwisen.GeneradorTablas;

import android.content.Context;
import android.widget.Toast;

import com.amg_eservices.miappwisen.UtilitiesGlobal;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoopjTasks {

    private com.amg_eservices.miappwisen.GeneradorTablas.OnLoopjCompleted loopjListener;
    private Context context;

    public LoopjTasks(Context context, com.amg_eservices.miappwisen.GeneradorTablas.OnLoopjCompleted loopjListener) {
        this.context = context;
        this.loopjListener = loopjListener;
    }

    public void CaptarParametros(String idObjeto) {

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put(UtilitiesGlobal.SENSOR_ID, idObjeto);

        RequestHandle post = client.post(context, UtilitiesGlobal.MAXMINAVG_TH_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                JSONObject jsonobject = null;
                JSONObject jsonobject2 = null;
                JSONObject dht11JSONbject = null;
                JSONArray dht11JSONarray = null;


                try {

                    jsonobject = new JSONObject(String.valueOf(response));
                    //dht11JSONbject = jsonobject.getJSONObject("result");


                    //List<String> allNames = new ArrayList<String>();
                    JSONObject cast = jsonobject.getJSONObject("result");
                    JSONObject cast1 = cast.getJSONObject("temprature");

                    jsonobject2 = new JSONObject(String.valueOf(cast1));
                    JSONArray minimos = jsonobject2.getJSONArray("min");

                    for (int i=0; i<minimos.length(); i++) {
                        JSONObject parametrosdht11 = minimos.getJSONObject(i);


                        loopjListener.onLoopjTaskCompleted(parametrosdht11, i);

                    }
                    loopjListener.onLoopComplete();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override

            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                // Hide Progress Dialog
                /*prgDialog.hide();*/
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(context, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(context, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }

        });

    }
}
