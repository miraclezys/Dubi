package cc.wo_mo.dubi.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cc.wo_mo.dubi.Model.UploadResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by womo on 2016/12/21.
 */

public abstract class UploadTarget implements Target {

    public String imageUrl;
    private Context mContext;
    public UploadTarget(Context context) {
        mContext = context;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        Log.d("path", Environment.getExternalStorageDirectory().toString());
        File file = new File(Environment.getExternalStorageDirectory(),"test.png");

        try {
            file.createNewFile();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            int options = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            while (baos.toByteArray().length/1024 > 512) {
                // Clean up os
                baos.reset();
                // interval 10
                options -= 10;
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            }
            bos.write(baos.toByteArray());
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final RequestBody imageBody = RequestBody.create(
                MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imageBodyPart = MultipartBody.Part
                .createFormData("image", file.getName(), imageBody);
        ApiClient.getClient(mContext)
                .uploadImage(imageBodyPart, ApiClient.user_id)
                .enqueue(new Callback<UploadResponse>() {
                    @Override
                    public void onResponse(Call<UploadResponse> call,
                                           Response<UploadResponse> response) {
                        if (response.code() == 200) {
                            Log.d("ok", "upload success");
                            imageUrl = response.body().url;
                            onUploadSuccess();
                        } else {
                            try {
                                Log.d("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UploadResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        Log.d("upload target", "onBitmapFailed");
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        Log.d("upload target", "onPrepareLoad");
    }
    public abstract void onUploadSuccess();
}
