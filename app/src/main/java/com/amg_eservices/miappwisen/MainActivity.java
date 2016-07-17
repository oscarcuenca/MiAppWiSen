package com.amg_eservices.miappwisen;

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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);
        Toast.makeText(getApplicationContext(),"Despliegue la barra superior para navegar", Toast.LENGTH_LONG).show();

        agregarToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        if (navigationView != null) {
            prepararDrawer(navigationView);
            // Seleccionar item por defecto
            seleccionarItem(navigationView.getMenu().getItem(0));
        }
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
}
