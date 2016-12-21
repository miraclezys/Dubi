package cc.wo_mo.dubi.activities;

import android.content.Intent;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import cc.wo_mo.dubi.R;
import cc.wo_mo.dubi.data.ApiClient;
import cc.wo_mo.dubi.utils.ImageUtils;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageActivity extends AppCompatActivity {
    ImageView image;
    PhotoViewAttacher mAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        image = (ImageView) findViewById(R.id.image);
        Uri uri = getIntent().getData();
        if (uri != null) {
            image.setImageURI(uri);
            mAttacher = new PhotoViewAttacher(image, true);
        } else {
            ImageUtils.with(this).load(getIntent().getStringExtra("url")).into(image, new Callback() {
                @Override
                public void onSuccess() {
                    mAttacher = new PhotoViewAttacher(image, true);
                }

                @Override
                public void onError() {

                }
            });
        }
    }
}
