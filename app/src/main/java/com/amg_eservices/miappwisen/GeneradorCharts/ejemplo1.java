package com.amg_eservices.miappwisen.GeneradorCharts;

import java.util.Date;

public class ejemplo1 {
    //nombre obtenido en base a la temperatura
    private int idTemperatura;
    private int temperatura;
    private Date insertadoTemp;
    private int idPresion;
    private int presion;
    private Date insertadoPresion;
    private int idAltitud;
    private int altitud;
    private Date insertadoAltitud;
    private int id;

    //getters y setters...

    @Override
    public int hashCode() {
        //hashcode basado en la fecha de inserción de temperatura
        return insertadoTemp != null ? insertadoTemp.hashCode() : idTemperatura;
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o != null && o.getClass().equals(this.getClass())) {
            ejemplo1 otro = (ejemplo1)o;
            //asumiendo que utilizas Java 7 usa el código de abajo
            //result = Objects.equals(this.insertadoTemp, otro.insertadoTemp);
            //si usas Java 6 y otros
            result = (this.insertadoTemp != null && otro.insertadoTemp != null
                    && this.insertadoTemp.equals(otro.insertadoTemp))
                    || (this.insertadoTemp == null && otro.insertadoTemp == null);
        }
        return result;
    }

    @Override
    public String toString() {
        return String.format("%d-%s", idTemperatura, insertadoTemp.toString());
    }



}