package com.amg_eservices.miappwisen.MisSensores.TablasDatos.HumedadTemperatura;

import org.json.JSONObject;

/**
 * Created by Propietario on 28/07/2016.
 */
public interface OnLoopjCompleted {

    void onLoopjTaskCompleted(JSONObject temperaturasminimas, int i);
    void onLoopComplete();
    void onLoopjTaskCompleted2(JSONObject temperaturasmaximas, int i);
    void onLoopComplete2();
    void onLoopjTaskCompleted3(String Temp_media);
    void onLoopComplete3();
    void onLoopjTaskCompleted4(String last_temperatura);
    void onLoopComplete4();
    void onLoopjTaskCompleted5(String last_date);
    void onLoopComplete5();
    void onLoopjTaskCompleted6(JSONObject humedadesminimas, int i);
    void onLoopComplete6();
    void onLoopjTaskCompleted7(JSONObject humedadesmaximas, int i);
    void onLoopComplete7();
    void onLoopjTaskCompleted8(String Humd_media);
    void onLoopComplete8();
    void onLoopjTaskCompleted9(String last_humedad);
    void onLoopComplete9();


}


