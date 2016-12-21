package cc.wo_mo.dubi.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cc.wo_mo.dubi.activities.EditActivity;
import cc.wo_mo.dubi.data.ApiClient;
import cc.wo_mo.dubi.data.Model.Tweet;
import cc.wo_mo.dubi.data.Model.UploadResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by womo on 2016/12/14.
 */

public class ImageUtils {
    private static volatile Picasso singleton;
    public static Picasso with(Context context) {
        if (singleton == null) {
            synchronized (ImageUtils.class) {
                if (singleton == null) {
                    singleton = new Picasso.Builder(context)
                            .downloader(new OkHttp3Downloader(ApiClient.getsHttpClient(context)))
//                            .loggingEnabled(true)
                            .build();
//                    singleton.setIndicatorsEnabled(true);
                }
            }
        }
        return singleton;
    }




}
