package com.amg_eservices.miappwisen.GeneradorCharts;


import org.json.JSONObject;

/**
 * Created by Propietario on 19/07/2016.
 */

public interface OnLoopjCompleted {

    void onLoopjTaskCompleted(JSONObject stringArrayList, int i);
    void onLoopComplete();
}




