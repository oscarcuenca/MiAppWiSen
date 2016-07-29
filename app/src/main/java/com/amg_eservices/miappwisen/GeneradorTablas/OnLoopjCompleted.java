package com.amg_eservices.miappwisen.GeneradorTablas;

import org.json.JSONObject;

/**
 * Created by Propietario on 28/07/2016.
 */
public interface OnLoopjCompleted {

    void onLoopjTaskCompleted(JSONObject stringArrayList, int i);
    void onLoopComplete();
}


