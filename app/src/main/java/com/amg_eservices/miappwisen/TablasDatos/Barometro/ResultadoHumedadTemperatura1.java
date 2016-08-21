package com.amg_eservices.miappwisen.TablasDatos.Barometro;

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
public class ResultadoHumedadTemperatura1 extends AppCompatActivity implements OnLoopjCompleted1 {

    private DrawerLayout drawerLayout;

    private OnLoopjCompleted1 loopjListener;

    LoopjTasks1 loopjTasks;
    TextView txttemperaturamediaB;
    TextView txttemperaturamaximaB;
    TextView txttemperaturaminimaB;
    TextView txtultimaentradatemperaturaB;
    TextView txtfechaultimaentradaB;
    TextView fechatemperaturamaximaB;
    TextView fechatemperaturaminimaB;
    TextView txtfechaultimaentrada2B;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_barometro);
        Toast.makeText(getApplicationContext(), "Despliegue la barra superior para navegar", Toast.LENGTH_LONG).show();
        String idObjeto = (String) getIntent().getExtras().getSerializable("IdentidadEnviada");

        loopjTasks = new LoopjTasks1(this, this);
        loopjTasks.CaptarParametros(idObjeto);

        agregarToolbar();

        txttemperaturamediaB = (TextView) findViewById(R.id.txttemperaturamediaB);

        txttemperaturamaximaB = (TextView) findViewById(R.id.txttemperaturamaximaB);
        fechatemperaturamaximaB = (TextView) findViewById(R.id.fechatemperaturamaximaB);

        txttemperaturaminimaB = (TextView) findViewById(R.id.txttemperaturaminimaB);
        fechatemperaturaminimaB = (TextView) findViewById(R.id.fechatemperaturaminimaB);

        txtultimaentradatemperaturaB = (TextView) findViewById(R.id.txtultimaentradatemperaturaB);

        txtfechaultimaentradaB = (TextView) findViewById(R.id.txtfechaultimaentradaB);


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


            txttemperaturamaximaB.setText(temperatura1);
            fechatemperaturamaximaB.setText(fecha1);


            //"fecha" is date and time
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " + "temperatura Mínima: "+ temperatura1 +" " +
                "Fecha último Mínima: " + fecha1 +" ");


    }
    @Override
    public void onLoopjTaskCompleted2(JSONObject temperaturasminimas, int i) {
        String temperatura2 = null;
        String fecha2 = null;


        try {
            temperatura2 = temperaturasminimas.getString("temperatura");
            fecha2 = temperaturasminimas.getString("Insertado");


            txttemperaturaminimaB.setText(temperatura2);
            fechatemperaturaminimaB.setText(fecha2);


            //"fecha" is date and time
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " + "temperatura Máxima: "+ temperatura2 +" " +
                "Fecha último Máxima: " + fecha2 +" ");


    }

    @Override
    public void onLoopjTaskCompleted4(String last_temperatura) {
        String temperatura4 = null;
        temperatura4 = last_temperatura.toString();
        txtultimaentradatemperaturaB.setText(temperatura4);

        Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " + "temperatura ultima: " + temperatura4 + " ");

    }

    @Override
    public void onLoopComplete3() {

    }

    @Override
    public void onLoopjTaskCompleted3(String temperaturasmedia, int i) {
        String temperatura5 = null;
        temperatura5 = temperaturasmedia.toString();
        txttemperaturamediaB.setText(temperatura5);

        Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " + "temperatura media: " + temperatura5 + " ");

    }


    @Override
    public void onLoopjTaskCompleted5(String last_date) {
        String ultima_fecha = null;
        ultima_fecha = last_date.toString();
        txtfechaultimaentradaB.setText(ultima_fecha);
        //txtfechaultimaentrada2B.setText(ultima_fecha);

        Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " + "Feacha temperatura ultima: " + ultima_fecha + " ");

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
    public void onLoopComplete4() {

    }



    @Override
    public void onLoopComplete5() {

    }

    @Override
    public void onLoopjTaskCompleted9(String last_date) {

    }
    @Override
    public void onLoopComplete9() {

    }

    @Override
    public void onLoopjTaskCompleted10(String temperaturamedia) {

    }


    @Override
    public void onLoopComplete10() {

    }




}
