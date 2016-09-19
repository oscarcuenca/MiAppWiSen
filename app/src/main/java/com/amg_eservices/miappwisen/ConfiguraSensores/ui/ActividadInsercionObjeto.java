package com.amg_eservices.miappwisen.ConfiguraSensores.ui;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.amg_eservices.miappwisen.ConfiguraSensores.provider.Contrato.Objetos;
import com.amg_eservices.miappwisen.ConfiguraSensores.utilidades.UConsultas;
import com.amg_eservices.miappwisen.ConfiguraSensores.utilidades.UTiempo;
import com.amg_eservices.miappwisen.GeneradorCharts.GraficaBarometro;
import com.amg_eservices.miappwisen.GeneradorCharts.GraficaHumedadTemperatura;
import com.amg_eservices.miappwisen.MisSensores.TablasDatos.Barometro.Barometro;
import com.amg_eservices.miappwisen.MisSensores.TablasDatos.HumedadTemperatura.TermomentroDht22;
import com.amg_eservices.miappwisen.R;

import java.io.Serializable;




@TargetApi(Build.VERSION_CODES.M)
public class ActividadInsercionObjeto extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    // Referencias UI
    private EditText campodescripcionNombre;
    private EditText campoMarca;
    private EditText campoModelo;
    private EditText campoCorreo;
    private EditText campoIdObjeto;
    private EditText campoCaja;
    private EditText campoSector;
    private ImageButton accesodata;
    private ImageButton accesotabla;
    public final static String EXTRA_ID = "idObjeto";
    public EditText IdentidadObjeto;

    // Clave del uri del objeto como extra
    public static final String URI_OBJETO = "extra.uriObjeto";

    private Uri uriObjeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_insercion_objeto);

        //agregarToolbar();

        // Encontrar Referencias UI
        campodescripcionNombre = (EditText) findViewById(R.id.campo_descripcion_nombre);
        campoMarca = (EditText) findViewById(R.id.campo_marca);
        campoModelo = (EditText) findViewById(R.id.campo_modelo);
        campoCorreo = (EditText) findViewById(R.id.campo_correo);
        campoCaja = (EditText) findViewById (R.id.campo_descripcion_caja);
        campoSector = (EditText) findViewById(R.id.campo_descripcion_sector);
        campoIdObjeto = (EditText) findViewById(R.id.campo_idObjeto);


        accesodata = (ImageButton) findViewById(R.id.accesodata);
        accesodata.setOnClickListener(this);
        accesotabla = (ImageButton) findViewById(R.id.accesotabla);
        accesotabla.setOnClickListener(this);

        // Determinar si es detalle
        String uri = getIntent().getStringExtra(URI_OBJETO);
        if (uri != null) {
            setTitle(R.string.titulo_actividad_editar_objeto);
            uriObjeto = Uri.parse(uri);
            getSupportLoaderManager().restartLoader(1, null, this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_insercion_objeto, menu);

        // Verificación de visibilidad acción eliminar
        if (uriObjeto != null) {
            menu.findItem(R.id.accion_eliminar).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.accion_confirmar:
                insertar();
                break;
            case R.id.accion_eliminar:
                eliminar();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void insertar() {

        // Extraer datos de UI
        String descripcionNombre = campodescripcionNombre.getText().toString();
        String marca = campoMarca.getText().toString();
        String modelo = campoModelo.getText().toString();
        String correo = campoCorreo.getText().toString();
        String IdentidadObjeto = campoIdObjeto.getText().toString();
        String caja = campoCaja.getText().toString();
        String sector = campoSector.getText().toString();

        // Validaciones y pruebas de cordura
        if (!esNombreValido(descripcionNombre)) {
            TextInputLayout mascaraCampoNombre = (TextInputLayout)findViewById(R.id.mascara_campo_nombre);

            // esta linea la he añadido, si da fallo eliminar. Sujerida por corrector
            assert mascaraCampoNombre != null;
            // esta linea la he añadido, si da fallo eliminar. Sujerida por corrector fin
            mascaraCampoNombre.setError("este campo no puede quedar vacio");
        } else {

            ContentValues valores = new ContentValues();

            // Verificación: ¿Es necesario generar un id?
            if (uriObjeto == null) {
                valores.put(Objetos.ID_OBJETO, Objetos.generarIdObjeto());
            }
            valores.put(Objetos.DESCRIPCION_NOMBRE, descripcionNombre);
            valores.put(Objetos.MARCA_MARCA, marca);
            valores.put(Objetos.MODELO, modelo);
            valores.put(Objetos.CORREO, correo);
            valores.put(Objetos.CAJA, caja);
            valores.put(Objetos.SECTOR, sector);
            valores.put(Objetos.VERSION, UTiempo.obtenerTiempo());

            // Iniciar inserción|actualización
            new TareaAnadirObjeto(getContentResolver(), valores).execute(uriObjeto);

            finish();
        }
    }

    private boolean esNombreValido(String nombre) {
        return !TextUtils.isEmpty(nombre);
    }

    private void eliminar() {
        if (uriObjeto != null) {
            // Iniciar eliminación
            new TareaEliminarObjeto(getContentResolver()).execute(uriObjeto);
            finish();
        }
    }


    private void poblarViews(Cursor data) {
        if (!data.moveToNext()) {
            return;
        }

        // Asignar valores a UI
        campodescripcionNombre.setText(UConsultas.obtenerString(data, Objetos.DESCRIPCION_NOMBRE));
        campoMarca.setText(UConsultas.obtenerString(data, Objetos.MARCA_MARCA));
        campoModelo.setText(UConsultas.obtenerString(data, Objetos.MODELO));
        campoCorreo.setText(UConsultas.obtenerString(data, Objetos.CORREO));
        campoCaja.setText(UConsultas.obtenerString(data, Objetos.CAJA));
        campoSector.setText(UConsultas.obtenerString(data, Objetos.SECTOR));
        campoIdObjeto.setText(UConsultas.obtenerString(data, Objetos.ID_OBJETO));

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, uriObjeto, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        poblarViews(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onClick(View v) {



        if (v == accesodata) {

            Class g = null;
            String nombre = campodescripcionNombre.getText().toString();
            if (nombre.equals("Barometro")) {
                g = GraficaBarometro.class;
            }else {
                g = GraficaHumedadTemperatura.class;
            }

            Intent i = new Intent(ActividadInsercionObjeto.this, g);

            i.putExtra("IdentidadEnviada", (Serializable) campoIdObjeto.getText().toString());
            startActivity(i);
        }

        if (v== accesotabla) {
            Class c = null;
            String nombre = campodescripcionNombre.getText().toString();
            if (nombre.equals("Barometro")) {
                c = Barometro.class;
            }else {
                c = TermomentroDht22.class;
                }


            Intent i = new Intent(ActividadInsercionObjeto.this, c);
            i.putExtra("IdentidadEnviada", (Serializable) campoIdObjeto.getText().toString());
            startActivity(i);
        }

    }


    static class TareaAnadirObjeto extends AsyncTask<Uri, Void, Void> {
        private final ContentResolver resolver;
        private final ContentValues valores;

        public TareaAnadirObjeto(ContentResolver resolver, ContentValues valores) {
            this.resolver = resolver;
            this.valores = valores;
        }

        @Override
        protected Void doInBackground(Uri... args) {
            Uri uri = args[0];
            if (null != uri) {
                /*
                Verificación: Si el cobjeto que se va a actualizar aún no ha sido sincronizado,
                es decir su columna 'insertado' = 1, entonces la columna 'modificado' no debe ser
                alterada
                 */
                Cursor c = resolver.query(uri, new String[]{Objetos.INSERTADO}, null, null, null);

                if (c != null && c.moveToNext()) {

                    // Verificación de sincronización
                    if (UConsultas.obtenerInt(c, Objetos.INSERTADO) == 0) {
                        valores.put(Objetos.MODIFICADO, 1);
                    }

                    valores.put(Objetos.VERSION, UTiempo.obtenerTiempo());
                    resolver.update(uri, valores, null, null);
                }

            } else {
                resolver.insert(Objetos.URI_CONTENIDO, valores);
            }
            return null;
        }

    }

    static class TareaEliminarObjeto extends AsyncTask<Uri, Void, Void> {
        private final ContentResolver resolver;

        public TareaEliminarObjeto(ContentResolver resolver) {
            this.resolver = resolver;
        }

        @Override
        protected Void doInBackground(Uri... args) {

            /*
            Verificación: Si el registro no ha sido sincronizado aún, entonces puede eliminarse
            directamente. De lo contrario se marca como 'eliminado' = 1
             */
            Cursor c = resolver.query(args[0], new String[]{Objetos.INSERTADO}
                    , null, null, null);

            int insertado;

            if (c != null && c.moveToNext()) {
                insertado = UConsultas.obtenerInt(c, Objetos.INSERTADO);
            } else {
                return null;
            }

            if (insertado == 1) {
                resolver.delete(args[0], null, null);
            } else if (insertado == 0) {
                ContentValues valores = new ContentValues();
                valores.put(Objetos.ELIMINADO, 1);
                resolver.update(args[0], valores, null, null);
            }

            return null;
        }
    }

}