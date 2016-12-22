package cc.wo_mo.dubi.data;



import android.content.Context;
import android.content.SharedPreferences;

public class MSharePreferences {
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor edit;
    private static MSharePreferences slideSharePreferences;
   

    public static MSharePreferences getInstance(Context context) {
        if (slideSharePreferences == null) {
            synchronized (MSharePreferences.class) {
                if (slideSharePreferences == null) {
                    slideSharePreferences = new MSharePreferences(context);
                }
            }
        }
        return slideSharePreferences;
    }

    MSharePreferences(Context context) {
        mSharedPreferences = context.getSharedPreferences(
                context.getPackageName() + "_preferences",
                Context.MODE_PRIVATE);
        edit = mSharedPreferences.edit();
    }

    public synchronized void putString(String key, String value) {
        edit.putString(key, value);
        edit.commit();

    }
    public synchronized void putInt(String key, int value) {
        edit.putInt(key, value);
        edit.commit();

    }
    public synchronized void putBoolean(String key, Boolean value) {
        edit.putBoolean(key, value);
        edit.commit();
    }
    public SharedPreferences getmSharedPreferences() {
        return mSharedPreferences;
    }

    public void setmSharedPreferences(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    public SharedPreferences.Editor getEdit() {
        return edit;
    }

    public void setEdit(SharedPreferences.Editor edit) {
        this.edit = edit;
    }

    public static MSharePreferences getSlidesharepreferences() {
        return slideSharePreferences;
    }

    public synchronized String getString(String key, String value) {
        String string = mSharedPreferences.getString(key, value);
        return string;
    }
    public synchronized int getInt(String key, int value) {
        int number = mSharedPreferences.getInt(key, value);
        return number;
    }
    public synchronized Boolean getBoolean(String key, Boolean value) {
        Boolean flag = mSharedPreferences.getBoolean(key, value);
        return flag;
    }
   
}