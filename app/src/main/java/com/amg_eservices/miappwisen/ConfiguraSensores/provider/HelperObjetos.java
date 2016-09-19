package com.amg_eservices.miappwisen.ConfiguraSensores.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.amg_eservices.miappwisen.ConfiguraSensores.utilidades.UTiempo;

/**
 * Clase auxiliar para controlar accesos a la base de datos SQLite
 */
public class HelperObjetos extends SQLiteOpenHelper {

    static final int VERSION = 1;

    static final String NOMBRE_BD = "wisen.db";


    interface Tablas {
        String OBJETO = "objeto";
    }

    public HelperObjetos(Context context) {

        super(context, NOMBRE_BD, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + Tablas.OBJETO + "("
                        + Contrato.Objetos._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + Contrato.Objetos.ID_OBJETO + " TEXT UNIQUE,"
                        + Contrato.Objetos.DESCRIPCION_NOMBRE + " TEXT NOT NULL,"
                        + Contrato.Objetos.MARCA_MARCA + " TEXT,"
                        + Contrato.Objetos.MODELO + " TEXT,"
                        + Contrato.Objetos.CORREO + " TEXT,"
                        + Contrato.Objetos.CAJA + " TEXT,"
                        + Contrato.Objetos.SECTOR + " TEXT,"
                        + Contrato.Objetos.VERSION + " DATE DEFAULT CURRENT_TIMESTAMP,"
                        + Contrato.Objetos.INSERTADO + " INTEGER DEFAULT 1,"
                        + Contrato.Objetos.MODIFICADO + " INTEGER DEFAULT 0,"
                        + Contrato.Objetos.ELIMINADO + " INTEGER DEFAULT 0)");

        // Registro ejemplo #1
        ContentValues valores = new ContentValues();
        valores.put(Contrato.Objetos.ID_OBJETO, Contrato.Objetos.generarIdObjeto());
        valores.put(Contrato.Objetos.DESCRIPCION_NOMBRE, "Lampara");
        valores.put(Contrato.Objetos.MARCA_MARCA, "philips");
        valores.put(Contrato.Objetos.MODELO, "4444444");
        valores.put(Contrato.Objetos.CORREO, "robertico@mail.com");
        valores.put(Contrato.Objetos.CAJA, "Caja-WiSen-Atmph");
        valores.put(Contrato.Objetos.SECTOR, "prueba");
        valores.put(Contrato.Objetos.VERSION, UTiempo.obtenerTiempo());

        db.insertOrThrow(Tablas.OBJETO, null, valores);

        // Registro ejemplo #2
        valores.clear();
        valores.put(Contrato.Objetos.ID_OBJETO, Contrato.Objetos.generarIdObjeto());
        valores.put(Contrato.Objetos.DESCRIPCION_NOMBRE, "Parbol");
        valores.put(Contrato.Objetos.MARCA_MARCA, "abeto");
        valores.put(Contrato.Objetos.MODELO, "0001");
        valores.put(Contrato.Objetos.CORREO, "pablito@mail.com");
        valores.put(Contrato.Objetos.CAJA, "prueba");
        valores.put(Contrato.Objetos.SECTOR, "prueba");
        valores.put(Contrato.Objetos.VERSION, UTiempo.obtenerTiempo());
        db.insertOrThrow(Tablas.OBJETO, null, valores);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.OBJETO);
        } catch (SQLiteException e) {
            // Manejo de excepciones
        }
        onCreate(db);
    }
}
