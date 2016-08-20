package com.amg_eservices.miappwisen.TablasDatos.HumedadTemperatura;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.amg_eservices.miappwisen.R;
import com.amg_eservices.miappwisen.UtilitiesGlobal;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Propietario on 27/07/2016.
 */
public class ResultadoHumedadTemperatura extends AppCompatActivity implements OnLoopjCompleted {

    private DrawerLayout drawerLayout;

    private OnLoopjCompleted loopjListener;

    LoopjTasks loopjTasks;
    TextView txttemperaturamedia;
    TextView txttemperaturamaxima;
    TextView txttemperaturaminima;
    TextView txthumedadamaxima;
    TextView txthumedadminima;
    TextView txthumedadmedia;
    TextView txtultimaentradatemperatura;
    TextView txtultimaentradahumedad;
    TextView txtfechaultimaentrada;
    TextView fechatemperaturamaxima;
    TextView fechatemperaturaminima;
    TextView fechahumedadmaxima;
    TextView fechahumedadminima;
    TextView txtfechaultimaentrada2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_tabla_humedad_temperatura);
        Toast.makeText(getApplicationContext(), "Despliegue la barra superior para navegar", Toast.LENGTH_LONG).show();
        String idObjeto = (String) getIntent().getExtras().getSerializable("IdentidadEnviada");

        loopjTasks = new LoopjTasks(this, this);
        loopjTasks.CaptarParametros(idObjeto);

        agregarToolbar();

        txttemperaturamedia = (TextView) findViewById(R.id.txttemperaturamedia);

        txttemperaturamaxima = (TextView) findViewById(R.id.txttemperaturamaxima);
        fechatemperaturamaxima = (TextView) findViewById(R.id.fechatemperaturamaxima);

        txttemperaturaminima = (TextView) findViewById(R.id.txttemperaturaminima);
        fechatemperaturaminima = (TextView) findViewById(R.id.fechatemperaturaminima);

        txthumedadmedia = (TextView) findViewById(R.id.txthumedadmedia);

        txthumedadamaxima = (TextView) findViewById(R.id.txthumedadamaxima);
        fechahumedadmaxima = (TextView) findViewById(R.id.fechahumedadmaxima);

        txthumedadminima = (TextView) findViewById(R.id.txthumedadminima);
        fechahumedadminima = (TextView) findViewById(R.id.fechahumedadminima);

        txtultimaentradatemperatura = (TextView) findViewById(R.id.txtultimaentradatemperatura);
        txtultimaentradahumedad = (TextView) findViewById(R.id.txtultimaentradahumedad);
        txtfechaultimaentrada = (TextView) findViewById(R.id.txtfechaultimaentrada);
        txtfechaultimaentrada2 = (TextView) findViewById(R.id.txtfechaultimaentrada2);



        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        if (navigationView != null) {
            prepararDrawer(navigationView);
            // Seleccionar item por defecto
            seleccionarItem(navigationView.getMenu().getItem(0));

        }


    }
    @Override
    public void onLoopjTaskCompleted(JSONObject temperaturasmaximas, int i) {
        String temperatura1 = null;
        String fecha1 = null;

        try {
            temperatura1 = temperaturasmaximas.getString("temperatura");
            fecha1 = temperaturasmaximas.getString("Insertado");


            txttemperaturamaxima.setText(temperatura1);
            fechatemperaturamaxima.setText(fecha1);


            //"fecha" is date and time
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " + "temperatura Máxima: "+ temperatura1 +" " +
                "Fecha último Máxima: " + fecha1 +" ");


    }
    @Override
    public void onLoopjTaskCompleted2(JSONObject temperaturasminimas, int i) {
        String temperatura2 = null;
        String fecha2 = null;


        try {
            temperatura2 = temperaturasminimas.getString("temperatura");
            fecha2 = temperaturasminimas.getString("Insertado");


            txttemperaturaminima.setText(temperatura2);
            fechatemperaturaminima.setText(fecha2);


            //"fecha" is date and time
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " + "temperatura Mínima: "+ temperatura2 +" " +
                "Fecha último Mínima: " + fecha2 +" ");


    }
    @Override
    public void onLoopjTaskCompleted3(JSONObject temperaturasmedias, int i) {
        String temperatura3 = null;

        try {
            temperatura3 = temperaturasmedias.getString("tempmedia");

            txttemperaturamedia.setText(temperatura3);



            //"fecha" is date and time
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " + "temperatura media acumulada: " + temperatura3 + " ");

    }

    @Override
    public void onLoopjTaskCompleted6(JSONObject humedadesminimas, int i) {

        String humedad1 = null;
        String fechahumedad1 = null;

        try {
            humedad1 = humedadesminimas.getString("humedad");
            fechahumedad1 = humedadesminimas.getString("Insertado");

            txthumedadminima.setText(humedad1);
            fechahumedadminima.setText(fechahumedad1);

            //"fecha" is date and time
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " + "humedad minima: " + humedad1 + " "+ "fecha registro: " + fechahumedad1 + " ");

    }
    @Override
    public void onLoopjTaskCompleted7(JSONObject humedadesmaximas, int i) {
        String humedad2 = null;
        String fechahumedad2 = null;

        try {
            humedad2 = humedadesmaximas.getString("humedad");
            fechahumedad2 = humedadesmaximas.getString("Insertado");

            txthumedadamaxima.setText(humedad2);
            fechahumedadmaxima.setText(fechahumedad2);

            //"fecha" is date and time
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " + "humedad máxima: " + humedad2 + " "+ "fecha registro: " + fechahumedad2 + " ");

    }
    @Override
    public void onLoopjTaskCompleted8(JSONObject humedadesmedias, int i) {
        String humedad3 = null;


        try {
            humedad3 = humedadesmedias.getString("humedad");


            txthumedadmedia.setText(humedad3);


            //"fecha" is date and time
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " + "humedad media acumulada: " + humedad3+" ");



    }

    @Override
    public void onLoopjTaskCompleted4(String last_temperatura) {
        String temperatura4 = null;
        temperatura4 = last_temperatura.toString();
        txtultimaentradatemperatura.setText(temperatura4);

        Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " + "temperatura ultima: " + temperatura4 + " ");

    }
    @Override
    public void onLoopjTaskCompleted5(String last_date) {
        String ultima_fecha = null;
        ultima_fecha = last_date.toString();
        txtfechaultimaentrada.setText(ultima_fecha);
        txtfechaultimaentrada2.setText(ultima_fecha);

        Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " + "Feacha temperatura ultima: " + ultima_fecha + " ");

    }

    @Override
    public void onLoopjTaskCompleted9(String last_humedad) {
        String humedad4 = null;
        humedad4 = last_humedad.toString();
        txtultimaentradahumedad.setText(humedad4);

        Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " + "humedad última: " + humedad4 + " ");

    }
    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ÃƒÂ­cono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.drawer_toggle);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void prepararDrawer(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        seleccionarItem(menuItem);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });

    }



    private void seleccionarItem(MenuItem itemDrawer) {
        Fragment fragmentoGenerico = null;

        FragmentManager fragmentManager = getSupportFragmentManager();


        switch (itemDrawer.getItemId()) {
          /*
           case R.id.item_inicio:
                fragmentoGenerico = new FragmentoInicio();
                break;
*/
            case R.id.item_web:

                startActivity(new Intent(this, com.amg_eservices.miappwisen.SaltoWeb.WebOficial.class));
                break;

            //fragmentoGenerico = new BlankFragment();


            case R.id.item_categorias:
                startActivity(new Intent(this, com.amg_eservices.miappwisen.MisSensores.ui.ActividadListaObjeto.class));
                break;

            case R.id.item_acceso:
                startActivity(new Intent(this, com.amg_eservices.miappwisen.RegistroyAcceso.MainActivity.class));
                break;
        }
        if (fragmentoGenerico != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.contenido_principal, fragmentoGenerico)
                    .commit();

            /*
            if(fragmentTransaction) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
               */

        }

        // Setear titulo actual
        setTitle(itemDrawer.getTitle());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actividad_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onLoopComplete() {

    }



    @Override
    public void onLoopComplete2() {

    }


    @Override
    public void onLoopComplete3() {

    }



    @Override
    public void onLoopComplete4() {

    }



    @Override
    public void onLoopComplete5() {

    }


    @Override
    public void onLoopComplete6() {

    }



    @Override
    public void onLoopComplete7() {

    }



    @Override
    public void onLoopComplete8() {

    }



    @Override
    public void onLoopComplete9() {

    }


}
