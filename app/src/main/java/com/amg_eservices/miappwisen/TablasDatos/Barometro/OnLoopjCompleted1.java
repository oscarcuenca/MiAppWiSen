package com.amg_eservices.miappwisen.TablasDatos.Barometro;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Propietario on 28/07/2016.
 */
public interface OnLoopjCompleted1 {

    void onLoopjTaskCompleted(JSONObject stringArrayList, int i);
    void onLoopComplete();

    void onLoopjTaskCompleted2(JSONObject stringArrayList, int i);
    void onLoopComplete2();

    void onLoopjTaskCompleted4(String last_temperatura);
    void onLoopComplete4();

    void onLoopjTaskCompleted5(String last_date);
    void onLoopComplete5();

    void onLoopjTaskCompleted9(String last_date);
    void onLoopComplete9();

    void onLoopjTaskCompleted10(String temperaturamedia);
    void onLoopComplete10();

    void onLoopjTaskCompleted3(String temperaturasmedias, int i);
    void onLoopComplete3();
}


