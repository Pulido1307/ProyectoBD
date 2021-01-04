package com.pulido.proyectobd.Helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Constantes {

    public static final String CLAVE = "ACD030330";

    public static String obtenerFecha() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

}
