package cc.wo_mo.dubi.ui;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by shushu on 2016/12/21.
 */

public class MSharePreferences {
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private static final MSharePreferences slideSharedPreferences = new MSharePreferences();

    public static MSharePreferences getInstance(){
        return slideSharedPreferences;
    }

    public void getSharedPreferences(Context context){
        mSharedPreferences = context.getSharedPreferences(
                context.getPackageName() + "_preferences",
                context.MODE_WORLD_WRITEABLE);
        mEditor = mSharedPreferences.edit();
    }

    public synchronized void putString(String key, String value){
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public synchronized  void putInt(String key, int value){
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public synchronized void putBoolean(String key, Boolean value){
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public SharedPreferences getmSharedPreferences(){
        return mSharedPreferences;
    }

    public SharedPreferences.Editor getEdit(){
        return mEditor;
    }

    public void setEdit(SharedPreferences.Editor editor){
        this.mEditor = editor;
    }

    public  static MSharePreferences getSlideSharedPreferences(){
        return  slideSharedPreferences;
    }

    public synchronized String getString(String key, String value){
        String string = mSharedPreferences.getString(key, value);
        return string;
    }

    public synchronized int getInt(String key, int value){
        int number = mSharedPreferences.getInt(key, value);
        return number;
    }

    public synchronized  Boolean getBoolean(String key, Boolean value){
        Boolean flag = mSharedPreferences.getBoolean(key, value);
        return flag;
    }
}
