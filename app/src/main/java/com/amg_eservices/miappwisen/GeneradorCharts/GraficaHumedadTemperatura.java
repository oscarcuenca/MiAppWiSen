package com.amg_eservices.miappwisen.GeneradorCharts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import com.amg_eservices.miappwisen.ConfiguraSensores.ui.ActividadListaObjeto;
import com.amg_eservices.miappwisen.R;
import com.amg_eservices.miappwisen.RegistroyAcceso.MainActivity;
import com.amg_eservices.miappwisen.SaltoWeb.WebOficial;
import com.amg_eservices.miappwisen.UtilitiesGlobal;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Propietario on 04/07/2016.
 */
public class GraficaHumedadTemperatura extends AppCompatActivity implements OnLoopjCompleted {

    private DrawerLayout drawerLayout;

    private OnLoopjCompleted loopjListener;

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object

    private String temeperatura;
    private Timestamp timestamp;


    List<Entry> temperature = new ArrayList<>();

    List<Entry> humidity = new ArrayList<>();
    List<String> dates = new ArrayList<>();


    LineChart mChart;

    LoopjTasks loopjTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_datos_sensor);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        String idObjeto = (String) getIntent().getExtras().getSerializable("IdentidadEnviada");

        loopjTasks = new LoopjTasks(this, this);
        loopjTasks.CaptarParametros(idObjeto);



        mChart = (LineChart) findViewById(R.id.chart);

        int currentTime = (int) System.currentTimeMillis();
        timestamp = new Timestamp(currentTime);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(true);
        mChart.setHighlightPerDragEnabled(true);

        // limit the number of visible entries
        mChart.setVisibleXRangeMaximum(5);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.LTGRAY);



        mChart.animateX(2500);

// to draw X-axis for our graph
        ;

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setEnabled(true);
        xAxis.setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);
        //xAxis.setAxisMaxValue(125f);
        xAxis.setAxisMinValue(0f);
        xAxis.setTextColor(Color.DKGRAY);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        // to draw axis line

        //modify leftYaxis range similarly others
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaxValue(50f);
        leftAxis.setAxisMinValue(10f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularityEnabled(true);


        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaxValue(50f);
        rightAxis.setAxisMinValue(10f);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);


/*
        // add data
        for (int i = 0; i < 480; i++) {
            labels.add("value "+ String.valueOf(i));
        }
        setData();
  */

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

                startActivity(new Intent(this, WebOficial.class));
                break;

            //fragmentoGenerico = new BlankFragment();


            case R.id.item_categorias:
                startActivity(new Intent(this, ActividadListaObjeto.class));
                break;

            case R.id.item_acceso:
                startActivity(new Intent(this, MainActivity.class));
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
    /*
    public String parseHours(long millis){
        String currentDate = new SimpleDateFormat("hh:mm").format(new Date(millis));
        Log.i(UtilitiesGlobal.TAG, "parseHours: " + currentDate);
        return currentDate;
    }
*/

    private void setData() {
//data set represents a lin
        LineDataSet set1, set2;

        // create a dataset and give it a type
        //modifications with colour and stuf
        set1 = new LineDataSet(temperature, "temperature");

        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setCircleColor(Color.WHITE);
        set1.setLineWidth(2f);
        set1.setCircleRadius(2f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);


        //set1.setFillFormatter(new MyFillFormatter(0f));
        //set1.setDrawHorizontalHighlightIndicator(false);
        //set1.setVisible(false);
        //set1.setCircleHoleColor(Color.WHITE);

        // create a dataset and give it a type
        // similar above
        set2 = new LineDataSet(humidity, "humidity");
        set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set2.setColor(Color.RED);
        set2.setCircleColor(Color.WHITE);
        set2.setLineWidth(2f);
        set2.setCircleRadius(2f);
        set2.setFillAlpha(65);
        set2.setFillColor(Color.RED);
        set2.setDrawCircleHole(false);
        set2.setHighLightColor(Color.rgb(244, 117, 117));
        //set2.setFillFormatter(new MyFillFormatter(900f));


        mChart.getXAxis().setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return dates.get((int) value);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets
        dataSets.add(set2);



        // create a data object with the datasets
        LineData data = new LineData(dataSets);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        // set data
        Log.i("Lists Sizedata",temperature.size() + " and " + humidity.size());
        mChart.setData(data);
        // move to the latest entry
        mChart.moveViewToX(data.getEntryCount());


    }

    @Override
    public void onLoopjTaskCompleted(JSONObject parametrosdht11, int i) {
        String temperatura = null;
        String humedad = null;
        String fecha = null;
        try {
            temperatura = parametrosdht11.getString("temperatura");
            humedad = parametrosdht11.getString("humedad");
            fecha = parametrosdht11.getString("Insertado");
            //"fecha" is date and time
        } catch (JSONException e) {
            e.printStackTrace();
        }
        temperature.add(new Entry(Float.valueOf(i),Float.valueOf(temperatura)));
        humidity.add(new Entry(Float.valueOf(i), Float.valueOf(humedad)));
        dates.add(fecha); // reduce the string to just 12:13 etc
        Log.i(UtilitiesGlobal.TAG, "onSuccess: FECHA " + fecha);
        //labels.add(new Entry(toString(fecha)));
        // XAxis.add(parseHours(timestamp.getTime()));
        // java.lang.NullPointerException: Attempt to invoke virtual method 'long java.sql.Timestamp.getTime()' on a null object reference



        //rrefresh we don't need to refresh since we are setting data after completing task
        mChart.notifyDataSetChanged();
        // mChart.setVisibleXRangeMaximum(12);

        //Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " + usuarioiJSONbject);
        Log.i(UtilitiesGlobal.TAG, "onSuccess: loopj " +"temperatura: "+ temperatura +" humedad: " +humedad +" Fecha Inserción: " + fecha);
    }

    @Override
    public void onLoopComplete() {
        setData();
        // it takes time to recieve time. so we set the map after loop is complete okay?
        //mChart.setVisibleXRangeMaximum(5);
    }
}