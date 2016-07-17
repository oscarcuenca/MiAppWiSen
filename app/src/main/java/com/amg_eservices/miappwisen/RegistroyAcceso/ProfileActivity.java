package com.amg_eservices.miappwisen.RegistroyAcceso;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amg_eservices.miappwisen.R;

//import com.amg_eservices.appiot.R;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    //Textview to show currently logged in user
    private TextView textView;
    private Button btnLogout;
    private Button buttoncontinua;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        agregarToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            prepararDrawer(navigationView);
            // Seleccionar item por defecto
            seleccionarItem(navigationView.getMenu().getItem(0));
        }

        //Initializing textview
        textView = (TextView) findViewById(R.id.textView);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        buttoncontinua = (Button) findViewById(R.id.buttoncontinua);

        btnLogout.setOnClickListener(this);
        buttoncontinua.setOnClickListener(this);

        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String user_email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");
        //Showing the current logged in email to textview
        textView.setText("Current User: " + user_email);


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

    //Logout function
    private void logout() {
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email and API Key
                        editor.putString(Config.EMAIL_SHARED_PREF, "");


                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(ProfileActivity.this, Login.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

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
    public void onClick(View v) {
        if (v == btnLogout) {
            logout();
        }
        if (v == buttoncontinua) {
            startActivity(new Intent(this, com.amg_eservices.miappwisen.MainActivity.class));
        }


    }


    }




