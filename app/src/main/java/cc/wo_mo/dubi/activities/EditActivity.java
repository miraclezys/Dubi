package cc.wo_mo.dubi.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Target;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PublicKey;

import cc.wo_mo.dubi.R;
import cc.wo_mo.dubi.data.ApiClient;
import cc.wo_mo.dubi.data.DubiService;
import cc.wo_mo.dubi.data.Model.BaseResponse;
import cc.wo_mo.dubi.data.Model.Tweet;
import cc.wo_mo.dubi.data.Model.UploadResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity implements View.OnClickListener{
    EditText mEditText;
    ImageButton mSendBtn;
    ImageButton mSelectPhotoBtn;
    CoordinatorLayout mImageLayout;
    FloatingActionButton mCancelButton;
    ImageView mImage;
    Picasso picasso;
    UploadTarget mUploadTarget = new UploadTarget();
    DubiService client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        client = ApiClient.getClient(this);
        init();
        Picasso.Builder builder = new Picasso.Builder(this);
        picasso = builder.downloader(new OkHttp3Downloader(ApiClient.sHttpClient)).build();
    }

    private void init() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("编辑");
        mEditText = (EditText)findViewById(R.id.edit_text);
        mSendBtn = (ImageButton)findViewById(R.id.send_button);
        mSelectPhotoBtn = (ImageButton)findViewById(R.id.select_photo);
        mImage = (ImageView) findViewById(R.id.image);
        mImageLayout = (CoordinatorLayout) findViewById(R.id.image_layout);
        mCancelButton = (FloatingActionButton) findViewById(R.id.cancel_fab);
        mImageLayout.setVisibility(View.GONE);
        mImage.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
        mSendBtn.setOnClickListener(this);
        mSelectPhotoBtn.setOnClickListener(this);
        Log.d("padding", ""+mImage.getPaddingBottom());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_button:
                if (mImage.getTag() != null) {
                    picasso.load((Uri)mImage.getTag()).into(mUploadTarget);
                    Log.d("upload", "complete");
                    if (mUploadTarget.imageUrl != null)
                        Log.d("image url", mUploadTarget.imageUrl);
                }
                break;
            case R.id.select_photo:
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
                break;
            case R.id.cancel_fab:
                mImage.setTag(null);
                mImageLayout.setVisibility(View.GONE);
                break;
            case R.id.image:
                Intent imageIntent = new Intent(this, ImageActivity.class);
                imageIntent.setData((Uri)v.getTag());
                startActivity(imageIntent);
                break;
        }
    }

    class UploadTarget implements Target {

        public String imageUrl;
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Log.d("path", Environment.getExternalStorageDirectory().toString());
            File file = new File(Environment.getExternalStorageDirectory(),"test.png");

            try {
                file.createNewFile();
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                bos.flush();
                bos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            final RequestBody imageBody = RequestBody.create(
                    MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("image", file.getName(), imageBody);
            client.uploadImage(imageBodyPart, ApiClient.user_id)
                    .enqueue(new Callback<UploadResponse>() {
                @Override
                public void onResponse(Call<UploadResponse> call,
                                       Response<UploadResponse> response) {
                    if (response.code() == 200) {
                        imageUrl = response.body().url;
                        showToast("上传成功");
                        String text = mEditText.getText().toString();
                        client.createTweet(ApiClient.user_id, new Tweet(text, imageUrl))
                                .enqueue(new Callback<Tweet>() {
                            @Override
                            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                                if (response.code()==200) {
                                    showToast("发表成功");
                                    EditActivity.this.setResult(200);
                                    EditActivity.this.finish();
                                } else {
                                    showToast("发表失败");
                                }
                            }
                            @Override
                            public void onFailure(Call<Tweet> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
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

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            mImageLayout.setVisibility(View.GONE);
            mImage.setTag(null);
            final Uri selectedImage = data.getData();
            int width =  getResources().getDisplayMetrics().widthPixels;
            picasso.load(selectedImage).resize(width, width*2/3).centerCrop()
                    .into(mImage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    mImage.setTag(selectedImage);
                    mImageLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
