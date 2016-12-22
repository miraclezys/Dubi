package cc.wo_mo.dubi.utils;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import cc.wo_mo.dubi.data.ApiClient;

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
