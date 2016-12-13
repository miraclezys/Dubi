package cc.wo_mo.dubi.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import cc.wo_mo.dubi.R;
import cc.wo_mo.dubi.data.ApiClient;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ImageView image = (ImageView) findViewById(R.id.image);
        Uri uri = getIntent().getData();
        Picasso.Builder builder = new Picasso.Builder(this);
        Picasso picasso = builder.downloader(new OkHttp3Downloader(ApiClient.sHttpClient)).build();
        picasso.load(uri).into(image);
    }
}
