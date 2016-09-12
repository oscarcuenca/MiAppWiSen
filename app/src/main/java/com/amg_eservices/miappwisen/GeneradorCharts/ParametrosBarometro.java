package com.amg_eservices.miappwisen.GeneradorCharts;

/**
 * Created by Propietario on 10/09/2016.
 */
public class ParametrosBarometro {
    int i;
    String Id_temperatura;
    String temperatura;
    String fecha_temperatura;
    String presion;
    String altitud;


    public ParametrosBarometro(String Id_temperatura, String temperatura, String fecha_temperatura, String presion, String altitud) {

        this.Id_temperatura = Id_temperatura;
        this.temperatura = temperatura;
        this.fecha_temperatura = fecha_temperatura;
        this.presion = presion;
        this.altitud = altitud;

    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ParametrosBarometro) {
            ParametrosBarometro p = (ParametrosBarometro) o;
            return this.Id_temperatura.equals(p.Id_temperatura);
        } else {
            return false;
        }

    }

    @Override
    public int hashCode() {
        return this.Id_temperatura.length() * this.Id_temperatura.length();
    }

}
