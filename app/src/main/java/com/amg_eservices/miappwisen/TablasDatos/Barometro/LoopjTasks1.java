package com.amg_eservices.miappwisen.TablasDatos.Barometro;

import android.content.Context;
import android.util.Log;
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

public class LoopjTasks1 {

    private OnLoopjCompleted1 loopjListener;
    private Context context;

    JSONObject temperaturasmaximas;
    JSONObject temperaturasminimas;
    JSONObject temperaturasmedias;

    JSONObject presionesmaximas;
    JSONObject presionesminimas;
    JSONObject presionesmedias;

    JSONObject altitudesmaximas;
    JSONObject altitudesminimas;
    JSONObject altitudesmedias;


    public LoopjTasks1(Context context, OnLoopjCompleted1 loopjListener) {
        this.context = context;
        this.loopjListener = loopjListener;
    }

    public void CaptarParametros(String idObjeto) {

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put(UtilitiesGlobal.SENSOR_ID, idObjeto);

        RequestHandle post = client.post(context, UtilitiesGlobal.BAROMETRO, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                Log.d("Connection: ", "" + statusCode);
                JSONObject jsonobject = null;
                JSONObject jsonobject2 = null;
                JSONObject jsonobject3 = null;
                JSONObject jsonobject4 = null;


                try {
                    String last_temperatura = "";
                    String last_date_temp = "";
                    String mi_media_temp = "";

                    String last_presion = "";
                    String last_date_press = "";
                    String mi_media_press = "";

                    String last_altitud = "";
                    String last_date_alt = "";
                    String mi_media_alt = "";



                    jsonobject = new JSONObject(String.valueOf(response));
                    JSONObject cast = jsonobject.getJSONObject("result");

                    JSONObject cast1 = cast.getJSONObject("temperature");
                    JSONObject cast3 = cast.getJSONObject("pressure");
                    JSONObject cast4 = cast.getJSONObject("altitude");
                    JSONObject cast2 = cast.getJSONObject("last_entry_temp");
                    JSONObject cast5 = cast.getJSONObject("last_entry_press");
                    JSONObject cast6 = cast.getJSONObject("last_entry_alt");

                    jsonobject2 = new JSONObject(String.valueOf(cast1));
                    JSONArray temperaturaminima = jsonobject2.getJSONArray("min");
                    JSONArray temperaturamaxima = jsonobject2.getJSONArray("max");
                    JSONArray temperaturamedia = jsonobject2.getJSONArray("avg");

                    jsonobject3 = new JSONObject(String.valueOf(cast3));
                    JSONArray presionminima = jsonobject3.getJSONArray("min");
                    JSONArray presionmaxima = jsonobject3.getJSONArray("max");
                    JSONArray presionmedia = jsonobject3.getJSONArray("avg");

                    jsonobject4 = new JSONObject(String.valueOf(cast4));
                    JSONArray altitudminima = jsonobject4.getJSONArray("min");
                    JSONArray altitudmaxima = jsonobject4.getJSONArray("max");
                    JSONArray altitudmedia = jsonobject4.getJSONArray("avg");


                    for (int i = 0; i < temperaturaminima.length(); i++) {
                        temperaturasminimas = temperaturaminima.getJSONObject(i);
                        loopjListener.onLoopjTaskCompleted(temperaturasminimas, i);
                    }
                    loopjListener.onLoopComplete();

                    for (int i = 0; i < presionminima.length(); i++) {
                        presionesminimas = temperaturaminima.getJSONObject(i);
                        loopjListener.onLoopjTaskCompleted6(presionesminimas, i);
                    }
                    loopjListener.onLoopCompleted6();

                    for (int i = 0; i < altitudminima.length(); i++) {
                        altitudesminimas = altitudminima.getJSONObject(i);
                        loopjListener.onLoopjTaskCompleted11(altitudesminimas, i);
                    }
                    loopjListener.onLoopCompleted10();


                    for (int i = 0; i < temperaturamaxima.length(); i++) {
                        temperaturasmaximas = temperaturamaxima.getJSONObject(i);
                        loopjListener.onLoopjTaskCompleted2(temperaturasmaximas, i);
                    }
                    loopjListener.onLoopComplete2();

                    for (int i = 0; i < presionmaxima.length(); i++) {
                        presionesmaximas = presionmaxima.getJSONObject(i);
                        loopjListener.onLoopjTaskCompleted7(presionesmaximas, i);
                    }
                    loopjListener.onLoopCompleted7();

                    for (int i = 0; i < altitudmaxima.length(); i++) {
                        altitudesmaximas = altitudmaxima.getJSONObject(i);
                        loopjListener.onLoopjTaskCompleted12(altitudesmaximas, i);
                    }
                    loopjListener.onLoopCompleted11();

                    for (int i = 0; i < temperaturamedia.length(); i++) {
                        temperaturasmedias = temperaturamedia.getJSONObject(i);
                        mi_media_temp = temperaturasmedias.getString("tempmedia");
                        loopjListener.onLoopjTaskCompleted3(mi_media_temp, i);
                    }
                    loopjListener.onLoopComplete3();

                    for (int i = 0; i < presionmedia.length(); i++) {
                        presionesmedias = presionmedia.getJSONObject(i);
                        mi_media_press = presionesmedias.getString("pressmedia");
                        loopjListener.onLoopjTaskCompleted8(mi_media_press, i);
                    }
                    loopjListener.onLoopCompleted8();

                    for (int i = 0; i < altitudmedia.length(); i++) {
                        altitudesmedias = altitudmedia.getJSONObject(i);
                        mi_media_alt = altitudesmedias.getString("altmedia");
                        loopjListener.onLoopjTaskCompleted15(mi_media_alt, i);
                    }
                    loopjListener.onLoopCompleted12();


                    last_temperatura = cast2.getString("temperatura");
                    loopjListener.onLoopjTaskCompleted4(last_temperatura);

                    last_date_temp = cast2.getString("Insertado_temp");
                    loopjListener.onLoopjTaskCompleted5(last_date_temp);


                    last_presion = cast5.getString("presion");
                    loopjListener.onLoopjTaskCompleted9(last_presion);

                    last_date_press = cast5.getString("Insertado_press");
                    loopjListener.onLoopjTaskCompleted10(last_date_press);

                    last_altitud = cast6.getString("altitud");
                    loopjListener.onLoopjTaskCompleted13(last_altitud);

                    last_date_alt = cast6.getString("Insertado_alt");
                    loopjListener.onLoopjTaskCompleted14(last_date_alt);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            @Override

            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {

                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + response);
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