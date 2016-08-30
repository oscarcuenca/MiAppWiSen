package com.amg_eservices.miappwisen.RegistroyAcceso;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amg_eservices.miappwisen.R;
import com.amg_eservices.miappwisen.UtilitiesGlobal;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

//import com.amg_eservices.miappwisen.R;

public class Login extends AppCompatActivity implements View.OnClickListener {


    //Defining views
    private EditText editTextEmail;
    private EditText textUser_pass;
    private AppCompatButton btnLogin;
    private AppCompatButton btnLinkToRegisterScreen;
    private AppCompatButton Returning;
    private CookieStore cookieStore;
    private DrawerLayout drawerLayout;
    private String user_name;
    private String user_email;
    public static String claveApi;
    private int estado;
    private String usuario;

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object

    private static final String LOGIN_URL = "http://wi-sen.esy.es/wisen/Sensores/v1/usuarios/login";

    //boolean variable to check user is logged in or not
    //initially it is false
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        agregarToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            prepararDrawer(navigationView);
            // Seleccionar item por defecto
            seleccionarItem(navigationView.getMenu().getItem(0));
        }

        // Find Error Msg Text View control by ID
        errorMsg = (TextView) findViewById(R.id.login_error);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);



        //Initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        textUser_pass = (EditText) findViewById(R.id.textUser_pass);


        btnLogin = (AppCompatButton) findViewById(R.id.btnLogin);
        btnLinkToRegisterScreen = (AppCompatButton) findViewById(R.id.btnLinkToRegisterScreen);
        Returning = (AppCompatButton) findViewById(R.id.Returning);

        //Adding click listener
        btnLogin.setOnClickListener(this);
        btnLinkToRegisterScreen.setOnClickListener(this);
        Returning.setOnClickListener(this);
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
                startActivity(new Intent(this, com.amg_eservices.miappwisen.ConfiguraSensores.ui.ActividadListaObjeto.class));
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
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);

    }


    @Override
    public void onClick(View v) {
        //Calling the login function
        if (v == btnLogin) {
            login(v);
        }
        if (v == btnLinkToRegisterScreen) {
            startActivity(new Intent(this, MainActivity.class));
        }
        if (v == Returning) {
            startActivity(new Intent(this, com.amg_eservices.miappwisen.MainActivity.class));
        }


    }


    private void login(View v) {
        //Getting values from edit texts
        //final String txt_umail = editTextEmail.getText().toString().trim();
        //final String txt_upass = textUser_pass.getText().toString().trim();

        final String user_email = editTextEmail.getText().toString().trim();
        final String contrasena = textUser_pass.getText().toString().trim();

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        //Creating a shared preference
        SharedPreferences sharedPreferences = Login.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Creating editor to store values to shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Adding values to editor
        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
        //editor.putString(Config.EMAIL_SHARED_PREF, user_mail);
        editor.putString(Config.EMAIL_SHARED_PREF, user_email);


        //Saving values to editor
        editor.commit();

        LoginLoopj(user_email, contrasena);




    }

    //LoginLoopj(user_email, contrasena);
    // TODO: 09/05/16 make the http call loopj here

    private void LoginLoopj(String user_email, String contrasena) {

        AsyncHttpClient client = new AsyncHttpClient();

        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;

        try {
            jsonParams.put(UtilitiesGlobal.USER_EMAIL, user_email);
            // Put Http parameter password with value of Password Edit View control
            jsonParams.put(UtilitiesGlobal.USER_PASSWORD, contrasena);

            entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));



        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // When Name Edit View, Email Edit View and Password Edit View have values other than Null

        if (Utility.isNotNull(user_email) && Utility.isNotNull(contrasena)) {
            if (Utility.validate(user_email)) {
                // Put Http parameter name with value of Name Edit View control

                LoginLoopj(user_email, contrasena);
            }

        } else {
            Toast.makeText(getApplicationContext(), "Please enter valid email or blank spaces", Toast.LENGTH_LONG).show();
        }

        // client.post(REGISTER_URL, params, new AsyncHttpResponseHandler() {
        // Invoke RESTful Web Service with Http parameters
        RequestHandle post = client.post(this, LOGIN_URL, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
            }



            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                JSONObject jsonobject = null;
                String claveApi = "";


                try {
                    jsonobject = new JSONObject(String.valueOf(response));
                    JSONObject usuarioiJSONbject = jsonobject.getJSONObject("usuario");
                    claveApi = usuarioiJSONbject.getString("claveApi");

                    //Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " + usuarioiJSONbject);
                    Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " + claveApi);

                    //Creating a shared preference
                    SharedPreferences sharedPreferences = Login.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    //Creating editor to store values to shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    //Adding values to editor
                    editor.putString(Config.API_SHARED_PREF, claveApi);

                    //Saving values to editor
                    editor.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Login.this, com.amg_eservices.miappwisen.ConfiguraSensores.ui.ActividadListaObjeto.class);
                startActivity(intent);

            }



            @Override

            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }

        });


    }
    public void navigatetoLoginActivity(View view) {
        Intent loginIntent = new Intent(getApplicationContext(), Login.class);
        // Clears History of Activity
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
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
