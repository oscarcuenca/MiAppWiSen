package com.amg_eservices.miappwisen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Wellcome extends AppCompatActivity {

    // Temporizador para la pantalla de bienvenida
    private static int SPLASH_TIEMPO = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);

        new Handler().postDelayed(new Runnable() {

            /*
            * Mostramos la pantalla de bienvenida con un temporizador.
            * De esta forma se puede mostrar el logo de la app o
            * compaÃƒÂ±ia durante unos segundos.
            */
            @Override
            public void run() {
                // Este metodo se ejecuta cuando se consume el tiempo del temporizador.
                // Se pasa a la activity principal
                Intent i = new Intent(Wellcome.this, MainActivity.class);
                startActivity(i);

                //startActivity(new Intent(this, MainActivity.class));

                // Cerramos esta activity
                finish();
            }
        }, SPLASH_TIEMPO);
    }
}

