package cc.wo_mo.dubi.data.Model;

/**
 * Created by shushu on 2016/12/21.
 */

public class Tools {
    public static long LastClickTime;
    public static final String KEY_PROVINCE = "PROVINCE";
    public static final String KEY_CITY = "CITY";
    public synchronized  static boolean isFastDoubeClick(){
        long time = System.currentTimeMillis();
        if(time - LastClickTime < 1000){
            return true;
        }
        LastClickTime = time;
        return false;
    }
}
