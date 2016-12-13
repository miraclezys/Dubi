package cc.wo_mo.dubi.data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by womo on 2016/12/8.
 */

public class ApiClient {
    private static final String PICASSO_CACHE = "picasso-cache";//缓存图片的存放文件夹名
    private static final int MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024;
    public final static String BASE_URL = "http://dubi.wo-mo.cc";
    public static String token = "";
    public static int user_id = 0;
    private static DubiService instance = null;
    public static OkHttpClient sHttpClient;
    public static Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();

    public static DubiService getClient(Context context) {
        if (instance == null) {
            synchronized (ApiClient.class) {
                File cacheDir = new File(context.getApplicationContext().getCacheDir(), PICASSO_CACHE);
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                }
                sHttpClient = new OkHttpClient.Builder()
                        .addNetworkInterceptor(new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                Request authRequest = chain.request().newBuilder()
                                        .addHeader("Authorization", "Token "+token)
                                        .build();
                                return chain.proceed(authRequest);
                            }
                        })
                        .cache(new Cache(cacheDir, MAX_DISK_CACHE_SIZE))
                        .build();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(sHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                instance = retrofit.create(DubiService.class);
                return instance;
            }
        } else {
            return instance;
        }
    }

    public static boolean isLogin() {
        return !"".equals(token);
    }

}
