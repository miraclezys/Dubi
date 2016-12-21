package cc.wo_mo.dubi.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;

import cc.wo_mo.dubi.R;
import cc.wo_mo.dubi.data.ApiClient;
import cc.wo_mo.dubi.data.Model.User;
import cc.wo_mo.dubi.utils.ImageUtils;
import cc.wo_mo.dubi.utils.ProcessBitmap;
import cc.wo_mo.dubi.utils.UploadTarget;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shushu on 2016/12/15.
 */

public class EditInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText birth;
    private Button selectPhotoButton;
    private Button commitButton;
    private ImageView userPhoto;
    private EditText introduction;
    private String birthStr ="0000/00/00";
    private UploadTarget mUploadTarget;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        mUploadTarget = new UploadTarget(this) {
            @Override
            public void onUploadSuccess() {
                updateInfo(imageUrl);
            }
        };
        user = ApiClient.gson.fromJson(getIntent().getStringExtra("user"), User.class);
        selectPhotoButton = (Button) findViewById(R.id.select_photo_button);
        selectPhotoButton.setOnClickListener(this);
        commitButton = (Button) findViewById(R.id.submit_btn);
        commitButton.setOnClickListener(this);
        userPhoto = (ImageView) findViewById(R.id.user_pic_img);
        userPhoto.setOnClickListener(this);
        introduction = (EditText) findViewById(R.id.introduction_text);
        introduction.setOnClickListener(this);

        birth=(EditText)findViewById(R.id.birth);
        birth.setFocusable(false);
        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditInfoActivity.this, "点击生日", Toast.LENGTH_SHORT).show();
                    //自定义对话框
                    LayoutInflater factory = LayoutInflater.from(EditInfoActivity.this);
                    View view2 = factory.inflate(R.layout.dialog_edit_birth, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditInfoActivity.this);//this
                    //初始化
                    final DatePicker mDatePicker=(DatePicker)view2.findViewById(R.id.datePicker);
                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    //设置电话号码
                    mDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker arg0, int year, int month, int day) {
                            month=month+1;
                            birthStr = year + "." + month + "." + day;

                        }
                    });

                    builder.setView(view2).setTitle("o(≧v≦)o")
                            .setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    birth.setText(birthStr);
                                    Toast.makeText(EditInfoActivity.this,"修改成功!"+ birthStr, Toast.LENGTH_SHORT).show();

                                }
                            }).setNegativeButton("放弃修改", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(EditInfoActivity.this,"取消修改!", Toast.LENGTH_SHORT).show();
                        }
                    }).create().show();
                }



        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_photo_button:
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
                break;
            case R.id.submit_btn:
                Uri selectedImage = (Uri) userPhoto.getTag();
                if (selectedImage != null) {
                    ImageUtils.with(this).load(selectedImage)
                            .transform(new ProcessBitmap(ProcessBitmap.MODE_CIRCLE, 200, null))
                            .into(mUploadTarget);
                } else {
                    updateInfo(null);
                }
                break;
            case R.id.introduction_text:
                break;
            case R.id.user_pic_img:
                break;
        }
    }

    void updateInfo(String photoUrl) {
        user.photo_url = photoUrl;
        user.birth = birthStr;
        user.introduction = introduction.getText().toString();
        ApiClient.getClient(this).updateUserInfo(ApiClient.user_id, user)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 200) {
                            Log.d("update", "ok");
                        } else {
                            try {
                                Log.d("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            userPhoto.setTag(null);
            final Uri selectedImage = data.getData();
            userPhoto.setImageDrawable(null);
            ImageUtils.with(this).load(selectedImage)
                    .transform(new ProcessBitmap(ProcessBitmap.MODE_CIRCLE, 200, null))
                    .into(userPhoto, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            userPhoto.setTag(selectedImage);
                        }
                        @Override
                        public void onError() {
                            Log.d("Error", "load image");
                        }
                    });
        }
    }
}
