package com.amg_eservices.miappwisen.ConfiguraSensores.ui;

import android.accounts.Account;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.amg_eservices.miappwisen.ConfiguraSensores.provider.Contrato;
import com.amg_eservices.miappwisen.ConfiguraSensores.utilidades.UCuentas;
import com.amg_eservices.miappwisen.ConfiguraSensores.utilidades.UPreferencias;
import com.amg_eservices.miappwisen.ConfiguraSensores.utilidades.UWeb;
import com.amg_eservices.miappwisen.RegistroyAcceso.Config;
import com.amg_eservices.miappwisen.UtilitiesGlobal;
//import com.amg_eservices.miappwisent.R;

import com.amg_eservices.miappwisen.R;

public class ActividadListaObjeto extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, AdaptadorObjetos.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = ActividadListaObjeto.class.getSimpleName();
    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorObjetos adaptador;
    private BroadcastReceiver receptorSync;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_lista_objeto);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDetalles(null);
            }
        });


        // Preparar elementos UI

        prepararLista();

        getSupportLoaderManager().restartLoader(1, null, this);
        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String claveApi = sharedPreferences.getString(Config.API_SHARED_PREF, "Not Available");
//Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " + usuarioiJSONbject);
        Log.i(UtilitiesGlobal.TAG, "onSuccess: transferencia de API " + claveApi);

        // Reemplaza con tu clave
        UPreferencias.guardarClaveApi(this, claveApi);

        receptorSync = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                mostrarProgreso(false);
                String mensaje = intent.getStringExtra("extra.mensaje");
                Snackbar.make(findViewById(R.id.drawer_layout),
                        mensaje, Snackbar.LENGTH_LONG).show();

                // he sustituido id.coordianador por id.drawer_layout en el snackbar

            }
        };

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    protected void onResume(){
        super.onResume();
        // Registrar receptor
        IntentFilter filtroSync= new IntentFilter(Intent.ACTION_SYNC);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorSync, filtroSync);


    }
    @Override
    protected void onPause() {
        super.onPause();
        // Desregistrar receptor
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorSync);

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actividad_lista_objeto, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == R.id.accion_sync) {
            if (UWeb.hayConexion(this)) {
                mostrarProgreso(true);
                sincronizar();
            } else {
                Snackbar.make(findViewById(R.id.drawer_layout),
                        "No hay conexion disponible. La sincronización queda pendiente",
                        Snackbar.LENGTH_LONG).show();

            }
            return true;
        }

        return super.onOptionsItemSelected(item);

    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.item_inicio) {
            // Handle the camera action
        } else if (id == R.id.item_web) {

            startActivity(new Intent(this, com.amg_eservices.miappwisen.SaltoWeb.WebOficial.class));


        } else if (id == R.id.item_categorias) {

            startActivity(new Intent(this, com.amg_eservices.miappwisen.ConfiguraSensores.ui.ActividadListaObjeto.class));

        } else if (id == R.id.item_acceso) {

            startActivity(new Intent(this, com.amg_eservices.miappwisen.RegistroyAcceso.MainActivity.class));

        }
        /*
        else if (id == R.id.nav_share) {


        } else if (id == R.id.nav_send) {

        }
    */


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void mostrarDetalles(Uri uri) {
        Intent intent = new Intent(this, ActividadInsercionObjeto.class);
        if (null != uri) {
            intent.putExtra(ActividadInsercionObjeto.URI_OBJETO, uri.toString());
        }
        startActivity(intent);
    }

    private void prepararLista() {
        reciclador = (RecyclerView) findViewById(R.id.reciclador);
        layoutManager = new LinearLayoutManager(this);
        adaptador = new AdaptadorObjetos(this);

        reciclador.setLayoutManager(layoutManager);
        reciclador.setAdapter(adaptador);
        //reciclador.setHasFixedSize(true);



    }

    private void sincronizar() {
        // Verificación para evitar iniciar más de una sync a la vez
        Account cuentaActiva = UCuentas.obtenerCuentaActiva(this);
        if (ContentResolver.isSyncActive(cuentaActiva, Contrato.AUTORIDAD)) {
            Log.d(TAG, "Ignorando sincronización ya que existe una en proceso.");
            return;
        }

        Log.d(TAG, "Solicitando sincronizacion manual");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(cuentaActiva, Contrato.AUTORIDAD, bundle);

    }


    private void mostrarProgreso(boolean mostrar) {
                findViewById(R.id.barra).setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {



    return new CursorLoader(
                      this,
            Contrato.Objetos.URI_CONTENIDO,
            null, Contrato.Objetos.ELIMINADO + "=?", new String[]{"0"}, null);
}


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
         adaptador.swapCursor(data);
        Toast.makeText(getApplicationContext(), "Actualización finalizada", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
           adaptador.swapCursor(null);
    }

    @Override
    public void onClick(AdaptadorObjetos.ViewHolder holder, String idObjeto) {
           mostrarDetalles(Contrato.Objetos.construirUriObjeto(idObjeto));
    }
}
