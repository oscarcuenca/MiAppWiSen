package com.amg_eservices.miappwisen.GeneradorCharts;

/**
 * Created by Propietario on 01/09/2016.
 */

import org.json.JSONObject;

import java.util.ArrayList;


public interface OnLoopjCompletedBarometro {

    // void onLoopjTaskCompletedBarometro(JSONObject jsonObject, int i);
    void onLoopjTaskCompletedBarometro(ArrayList<JSONObject> jsonObjectArrayList);


    void onLoopCompleteBarometro();
}