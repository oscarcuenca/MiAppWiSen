package com.amg_eservices.miappwisen.GeneradorCharts;

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

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Propietario on 01/09/2016.
 */
public class LoopjTasksBarometro {

    private OnLoopjCompletedBarometro loopjListener;
    private Context context;

    public LoopjTasksBarometro(Context context, OnLoopjCompletedBarometro loopjListener) {
        this.context = context;
        this.loopjListener = loopjListener;
    }

    public void CaptarParametros(String idObjeto) {

        AsyncHttpClient client = new AsyncHttpClient();

        final RequestParams params = new RequestParams();
        params.put(UtilitiesGlobal.SENSOR_ID, idObjeto);

        RequestHandle post = client.post(context, UtilitiesGlobal.CONSULTAS_SENSOR, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                JSONObject jsonobject = null;
                JSONObject dht11JSONbject = null;
                JSONArray dht11JSONarray = null;


                try {

                    jsonobject = new JSONObject(String.valueOf(response));
                    //dht11JSONbject = jsonobject.getJSONObject("result");


                    //List<String> allNames = new ArrayList<String>();
                    JSONArray cast = jsonobject.getJSONArray("result");


                    ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<JSONObject>();

                    for (int i=0; i<cast.length(); i++) {
                        JSONObject parametrosdht11 = cast.getJSONObject(i);

                        jsonObjectArrayList.add(parametrosdht11);

                    }
                    loopjListener.onLoopjTaskCompletedBarometro(jsonObjectArrayList);
                    loopjListener.onLoopCompleteBarometro();

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