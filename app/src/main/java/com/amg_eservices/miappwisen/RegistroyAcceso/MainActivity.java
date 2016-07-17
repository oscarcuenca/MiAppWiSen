package com.amg_eservices.miappwisen.RegistroyAcceso;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amg_eservices.miappwisen.UtilitiesGlobal;
import com.amg_eservices.miappwisen.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

//import com.amg_eservices.miappwisen.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private Button btnRegister;
    private Button btnLinkToLoginScreen;
    private EditText editTextUserName;
    private EditText editTextEmail;
    private EditText user_pass;
    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Name Edit View Object
    private AppCompatButton btnReturning2;


    private static final String REGISTER_URL = "http://wi-sen.esy.es/wisen/Sensores/v1/usuarios/registro";
    //private static final String REGISTER_URL = "http://wi-sen.esy.es/wisen/App/RegisterLogin/sign-up.php?";
    //private static final String REGISTER_URL = "http://wi-sen.esy.es/wisen/Sensores/v1/controladores/usuarios.php?";
    //private static final String REGISTER_URL = "http://prueba.wi-sen.esy.es/wp-login.php?action=register";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        agregarToolbar();



        if (navigationView != null) {
            prepararDrawer(navigationView);
            // Seleccionar item por defecto
            seleccionarItem(navigationView.getMenu().getItem(0));
        }

        // Find Error Msg Text View control by ID
        errorMsg = (TextView) findViewById(R.id.register_error);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        user_pass = (EditText) findViewById(R.id.user_pass);


        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLoginScreen = (Button) findViewById(R.id.btnLinkToLoginScreen);


        btnRegister.setOnClickListener(this);
        btnLinkToLoginScreen.setOnClickListener(this);

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
    public void onClick(View v) {
        if (v == btnRegister) {
            registerUser(v);
        }
        if (v == btnLinkToLoginScreen) {
            startActivity(new Intent(this, Login.class));
        }
    }


    private void registerUser(View view) {
        // Get Name ET control value
        String user_name = editTextUserName.getEditableText().toString().trim().toLowerCase();
        // Get Email ET control value
        String user_email = editTextEmail.getEditableText().toString().trim().toLowerCase();
        // Get Password ET control value
        String contrasena = user_pass.getEditableText().toString().trim().toLowerCase();

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);


        registerLoopj(user_name, user_email, contrasena);

    }


    //registerLoopj(user_name, user_email, user_password);
    // TODO: 09/05/16 make the http call loopj here


    private void registerLoopj(String user_name, String user_email, String contrasena) {

        AsyncHttpClient client = new AsyncHttpClient();


        // When Email entered is Valid
//        RequestParams params = new RequestParams();
//        params.put(UtilitiesGlobal.USER_NAME, user_name);
//        params.put(UtilitiesGlobal.USER_EMAIL, user_email);
//        params.put(UtilitiesGlobal.USER_PASSWORD, user_password);

        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;
        try {
            // Put Http parameter name with value of Name Edit View control
            jsonParams.put(UtilitiesGlobal.USER_NAME, user_name);
            // Put Http parameter username with value of Email Edit View control
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


        // When Email Edit View and Password Edit View have values other than Null

        if (Utility.isNotNull(user_name) && Utility.isNotNull(user_email) && Utility.isNotNull(contrasena)) {
            if (Utility.validate(user_email)) {
                // Put Http parameter name with value of Name Edit View control

                registerLoopj(user_name, user_email, contrasena);
            }

        } else {
            Toast.makeText(getApplicationContext(), "Please enter valid email or blank spaces", Toast.LENGTH_LONG).show();
        }


        // client.post(REGISTER_URL, params, new AsyncHttpResponseHandler() {
        // Invoke RESTful Web Service with Http parameters
        client.post(this, REGISTER_URL, entity, "application/json", new AsyncHttpResponseHandler() {


                    @Override
                    public void onStart() {
                        // called before request is started
                    }


                    @Override

                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // called when response HTTP status is "200 OK"
                        String responseStr = null;



                        try {
                            responseStr = new String(response, "UTF-8");

                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        }
                        Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " + responseStr);

                        Toast toast1 =
                                Toast.makeText(getApplicationContext(),
                                        R.string.Registro_exitoso, Toast.LENGTH_LONG);

                        toast1.show();

                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        String response = null;
                        try {
                            response = new String(errorResponse, "UTF-8");
                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        }
                        Log.i(UtilitiesGlobal.TAG, "onFailure: loopj " + response);
                        Toast toast2 =
                                Toast.makeText(getApplicationContext(),
                                        R.string.Registro_fallido, Toast.LENGTH_LONG);
                        toast2.show();
                        finish();


                    }


                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }


                }

        );


    }

    //startActivity(new Intent(this, Login.class)

    //  );
    /**
     * Method which navigates from Register Activity to Login Activity
     */

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

    /*
    private void register(String user_name, String user_email, String contrasena) {

        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait", null, true, true);
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String, String>();
                // es mejor guardar las keys para identificar los campos como constantes en una
                // clase aparte.
                data.put(UtilitiesGlobal.USER_NAME, params[0]);
                data.put(UtilitiesGlobal.USER_EMAIL, params[1]);
                data.put(UtilitiesGlobal.USER_PASSWORD, params[2]);

                String result = ruc.sendPostRequest(REGISTER_URL, data);

                return result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(user_name, user_email, contrasena);

        startActivity(new Intent(this, Login.class));
    }
*/


