package com.amg_eservices.miappwisen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Propietario on 04/07/2016.
 */
public class DatosSensor extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private static final String SENSORS_URL = "http://wi-sen.esy.es/wisen/Sensores/v1/controladores/dht11sensor.php";

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object
    private String temeperatura;
    ArrayList<Entry> temperature = new ArrayList<>();
    ArrayList<Entry> humidity = new ArrayList<>();
    LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_datos_sensor);
        String idObjeto = (String) getIntent().getExtras().getSerializable("IdentidadEnviada");

        CaptarParametros(idObjeto);


        mChart = (LineChart) findViewById(R.id.chart);



        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.LTGRAY);

        // add data
        setData();

        mChart.animateX(2500);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
//        l.setYOffset(11f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaxValue(100f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaxValue(100f);
        rightAxis.setAxisMinValue(0f);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);

        agregarToolbar();

    }


    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ÃƒÆ’Ã‚Â­cono del drawer toggle
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

    private void CaptarParametros(String idObjeto) {

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put(UtilitiesGlobal.SENSOR_ID, idObjeto);

        RequestHandle post = client.post(this, SENSORS_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                JSONObject jsonobject = null;



                try {

                    jsonobject = new JSONObject(String.valueOf(response));
                    //dht11JSONbject = jsonobject.getJSONObject("result");



                    JSONArray cast = jsonobject.getJSONArray("result");
                    for (int i=0; i<cast.length(); i++) {
                        JSONObject parametrosdht11 = cast.getJSONObject(i);
                        String temperatura = parametrosdht11.getString("temperatura");
                        String humedad = parametrosdht11.getString("humedad");

                        temperature.add(new Entry(Float.valueOf(temperatura),i));
                        humidity.add(new Entry(Float.valueOf(humedad),i));


                        Log.i("Entries", "tempeature " + temperature.size() + " " + "humidity " + humidity.size());
                        //Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " + usuarioiJSONbject);
                        Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " +"temperatura: "+ temperatura +" humedad: " +humedad);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override

            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                // Hide Progress Dialog
               /* prgDialog.hide();*/
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

    private void setData() {

        LineDataSet set1, set2;

            // create a dataset and give it a type
            set1 = new LineDataSet(temperature, "Temperature");

            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(ColorTemplate.getHoloBlue());
            set1.setCircleColor(Color.WHITE);
            set1.setLineWidth(2f);
            set1.setDrawCircleHole(false);
            set1.setFillAlpha(65);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            //set1.setFillFormatter(new MyFillFormatter(0f));
            //set1.setDrawHorizontalHighlightIndicator(false);
            //set1.setVisible(false);
            //set1.setCircleHoleColor(Color.WHITE);

            // create a dataset and give it a type
            set2 = new LineDataSet(humidity, "Humidity");
            set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
            set2.setCircleColor(Color.WHITE);
            set2.setLineWidth(2f);
            set2.setCircleRadius(3f);
            set2.setFillAlpha(65);
            set2.setDrawCircleHole(false);
            set2.setFillColor(Color.RED);
            set2.setDrawCircleHole(false);
            set2.setHighLightColor(Color.rgb(244, 117, 117));
            //set2.setFillFormatter(new MyFillFormatter(900f));

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets
            dataSets.add(set2);

            // create a data object with the datasets
            LineData data = new LineData(dataSets);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);

            // set data
            mChart.setData(data);
        }

}