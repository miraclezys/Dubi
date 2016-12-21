package cc.wo_mo.dubi.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by womo on 2016/12/21.
 */

public class TimeTool {

    private static SimpleDateFormat fromFomatter =
            new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss", Locale.ENGLISH);
    private static SimpleDateFormat StdFomatter =
            new SimpleDateFormat("yyyy年M月d日 HH:mm");

    public static String getFineTime(String from) {
        try {
            Date date = fromFomatter.parse(from);
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStandardTime(String from) {
        try {
            Log.d("log", from);
            Date date = fromFomatter.parse(from);

            return StdFomatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
