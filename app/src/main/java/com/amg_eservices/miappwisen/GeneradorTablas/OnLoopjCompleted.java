package com.amg_eservices.miappwisen.GeneradorTablas;

import org.json.JSONObject;

/**
 * Created by Propietario on 28/07/2016.
 */
public interface OnLoopjCompleted {

    void onLoopjTaskCompleted(JSONObject stringArrayList, int i);
    void onLoopComplete();
    void onLoopjTaskCompleted2(JSONObject stringArrayList, int i);
    void onLoopComplete2();
    void onLoopjTaskCompleted3(JSONObject stringArrayList, int i);
    void onLoopComplete3();
    void onLoopjTaskCompleted4(String last_temperatura);
    void onLoopComplete4();
    void onLoopjTaskCompleted5(String last_date);
    void onLoopComplete5();
}


