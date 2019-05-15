package br.com.aloisio.controledeponto.util;

import android.text.format.Time;
import java.util.Calendar;

public class Util{

    public static int getDateDiff(Time t1, Time t2) {
        return t2.hour * 60 + t2.minute - (t1.hour * 60 + t1.minute);
    }

    public static int getAno(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getDia(){
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getHora() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getMes() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    public static int getMinuto()  {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }
}
