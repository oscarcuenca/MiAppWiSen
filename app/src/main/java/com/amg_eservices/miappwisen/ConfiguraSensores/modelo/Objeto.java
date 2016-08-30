package com.amg_eservices.miappwisen.ConfiguraSensores.modelo;

import com.amg_eservices.miappwisen.ConfiguraSensores.utilidades.UTiempo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * POJO de los contactos
 */
public class Objeto {

    public String idObjeto;
    public String descripcionNombre;
    public String marca;
    public String modelo;
    public String correo;
    public String version;
    public int modificado;


    public Objeto(String idObjeto, String descripcionNombre,
                  String marca, String modelo, String correo, String version, int modificado) {

        this.idObjeto = idObjeto;
        this.descripcionNombre = descripcionNombre;
        this.marca = marca;
        this.modelo = modelo;
        this.correo = correo;
        this.version = version;
        this.modificado = modificado;
    }

    public void aplicarSanidad() {

        idObjeto = idObjeto == null ? "" : idObjeto;
        descripcionNombre = descripcionNombre == null ? "" : descripcionNombre;
        marca = marca == null ? "" : marca;
        modelo = modelo == null ? "" : modelo;
        correo = correo == null ? "" : correo;
        version = version == null ? UTiempo.obtenerTiempo() : version;
        modificado = 0;


    }

    public int esMasReciente(Objeto match) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date fechaA = formato.parse(version);
            Date fechaB = formato.parse(match.version);

            return fechaA.compareTo(fechaB);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean compararCon(Objeto otro) {
        return idObjeto.equals(otro.idObjeto) &&
                descripcionNombre.equals(otro.descripcionNombre) &&
                marca.equals(otro.marca) &&
                modelo.equals(otro.modelo) &&
                correo.equals(otro.correo);
    }
}


