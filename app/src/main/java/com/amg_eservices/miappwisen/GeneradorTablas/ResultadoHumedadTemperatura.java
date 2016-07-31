package com.amg_eservices.miappwisen.GeneradorTablas;

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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Propietario on 27/07/2016.
 */
public class ResultadoHumedadTemperatura extends AppCompatActivity implements OnLoopjCompleted {

    private DrawerLayout drawerLayout;

    private OnLoopjCompleted loopjListener;

    private String temeperatura;
    private Timestamp timestamp;
    private TextView UltimaTemperatura;
    private String dato;
    List<Map.Entry> temperature = new ArrayList<>();

    List<Map.Entry> humidity = new ArrayList<>();
    List<String> dates = new ArrayList<>();
    LoopjTasks loopjTasks;
    TextView txttemperaturamedia;
    TextView txttemperaturamaxima;
    TextView txttemperaturaminima;
    TextView txthumedadamaxima;
    TextView txthumedadminima;
    TextView txthumedadmedia;
    TextView txtultimaentrada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_tabla_humedad_temperatura);
        Toast.makeText(getApplicationContext(), "Despliegue la barra superior para navegar", Toast.LENGTH_LONG).show();
        String idObjeto = (String) getIntent().getExtras().getSerializable("IdentidadEnviada");

        loopjTasks = new LoopjTasks(this, this);
        loopjTasks.CaptarParametros(idObjeto);

        int currentTime = (int) System.currentTimeMillis();
        timestamp = new Timestamp(currentTime);

        txttemperaturamedia = (TextView) findViewById(R.id.ultimaentradas);
        agregarToolbar();
        txttemperaturamaxima = (TextView) findViewById(R.id.ultimaentradas);
        agregarToolbar();
        txttemperaturaminima = (TextView) findViewById(R.id.ultimaentradas);
        agregarToolbar();
        txthumedadmedia = (TextView) findViewById(R.id.ultimaentradas);
        agregarToolbar();
        txthumedadamaxima = (TextView) findViewById(R.id.ultimaentradas);
        agregarToolbar();
        txthumedadminima = (TextView) findViewById(R.id.ultimaentradas);
        agregarToolbar();
        txtultimaentrada = (TextView) findViewById(R.id.ultimaentradas);
        agregarToolbar();



        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        if (navigationView != null) {
            prepararDrawer(navigationView);
            // Seleccionar item por defecto
            seleccionarItem(navigationView.getMenu().getItem(0));

        }


    }
    @Override
    public void onLoopjTaskCompleted(JSONObject parametrosdht11, int i) {
        String temperatura = null;
        String humedad = null;
        String fecha = null;
        String media_temperatura=null;
        String media_humedad=null;

        try {
            temperatura = parametrosdht11.getString("temperatura");
            humedad = parametrosdht11.getString("humedad");
            fecha = parametrosdht11.getString("Insertado");


            // display product data in EditText

            txttemperaturamedia.setText(temperatura);
            txttemperaturamaxima.setText(temperatura);
            txttemperaturaminima.setText(temperatura);
            txthumedadamaxima.setText(temperatura);
            txthumedadminima.setText(temperatura);
            txthumedadminima.setText(temperatura);
            txthumedadminima.setText(temperatura);
            txtultimaentrada.setText(temperatura);


            //"fecha" is date and time
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //temperature.add(new Entry(Float.valueOf(i), Float.valueOf(temperatura)));
        //humidity.add(new Entry(Float.valueOf(i), Float.valueOf(humedad)));
        //dates.add(fecha); // reduce the string to just 12:13 etc
        //Log.i(UtilitiesGlobal.TAG, "onSuccess: FECHA " + fecha);

        //Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " + usuarioiJSONbject);
        Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " +"temperatura: "+ temperatura +" humedad: " +humedad +" Ultima Entrada: " + fecha);
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
}
