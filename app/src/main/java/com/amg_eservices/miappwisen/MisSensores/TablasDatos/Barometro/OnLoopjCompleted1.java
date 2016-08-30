package com.amg_eservices.miappwisen.MisSensores.TablasDatos.Barometro;

import org.json.JSONObject;

/**
 * Created by Propietario on 28/07/2016.
 */
public interface OnLoopjCompleted1 {

    void onLoopjTaskCompleted(JSONObject temperaturasminimas, int i);
    void onLoopComplete();

    void onLoopjTaskCompleted2(JSONObject temperaturasmaximas, int i);
    void onLoopComplete2();

    void onLoopjTaskCompleted4(String last_temperatura);
    void onLoopComplete4();

    void onLoopjTaskCompleted5(String last_date_temp);
    void onLoopComplete5();



    void onLoopjTaskCompleted3(String mi_media_temp, int i);
    void onLoopComplete3();


    void onLoopjTaskCompleted6(JSONObject presionesminimas, int i);

    void onLoopCompleted6();

    void onLoopjTaskCompleted7(JSONObject presionesmaximas, int i);

    void onLoopCompleted7();

    void onLoopjTaskCompleted8(String mi_media_press, int i);

    void onLoopCompleted8();

    void onLoopjTaskCompleted9(String last_presion);

    void onLoopjTaskCompleted10(String last_date_press);

    void onLoopjTaskCompleted11(JSONObject altitudesminimas, int i);

    void onLoopCompleted10();

    void onLoopjTaskCompleted12(JSONObject altitudesmaximas, int i);

    void onLoopCompleted11();

    void onLoopCompleted12();

    void onLoopjTaskCompleted13(String last_altitud);

    void onLoopjTaskCompleted14(String last_date_alt);

    void onLoopjTaskCompleted15(String mi_media_alt, int i);

    void onLoopCompleted15();
}


