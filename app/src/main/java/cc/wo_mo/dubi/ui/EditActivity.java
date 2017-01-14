package cc.wo_mo.dubi.ui;

import android.content.Intent;
import android.net.Uri;
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


import cc.wo_mo.dubi.R;
import cc.wo_mo.dubi.utils.ApiClient;
import cc.wo_mo.dubi.Model.Tweet;
import cc.wo_mo.dubi.utils.ImageUtils;
import cc.wo_mo.dubi.utils.ProcessBitmap;
import cc.wo_mo.dubi.utils.UploadTarget;
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
    UploadTarget upload = new UploadTarget(this) {
        @Override
        public void onUploadSuccess() {
            send(imageUrl);
            showToast("上传成功");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
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
                    Log.d("send", "start");
                    ImageUtils.with(this.getApplicationContext()).load((Uri)mImage.getTag())
                            .transform(new ProcessBitmap(ProcessBitmap.MODE_NORMAL, 1080, null))
                            .into(upload);
                } else {
                    send(null);
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

    void send(String imageUrl) {
        String text = mEditText.getText().toString();
        ApiClient.getClient(EditActivity.this)
                .createTweet(ApiClient.user_id, new Tweet(text, imageUrl))
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
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            mImageLayout.setVisibility(View.GONE);
            mImage.setTag(null);
            final Uri selectedImage = data.getData();
            int width =  getResources().getDisplayMetrics().widthPixels;
            ImageUtils.with(this).load(selectedImage).resize(width, width*2/3).centerCrop()
                    .into(mImage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    mImage.setTag(selectedImage);
                    mImageLayout.setVisibility(View.VISIBLE);
                }
                @Override
                public void onError() {
                    Log.d("Error", "load iamge");
                }
            });
        }
    }

    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
