package cc.wo_mo.dubi.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import cc.wo_mo.dubi.R;
import cc.wo_mo.dubi.data.ApiClient;
import cc.wo_mo.dubi.data.DubiService;
import cc.wo_mo.dubi.data.Model.Tweet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {
    EditText mEditText;
    ImageButton mSendBtn;
    DubiService client = ApiClient.getClient();
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
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString();
                if (!text.equals("")) {
                    client.createTweet(ApiClient.user_id,
                            new Tweet(text, null)).enqueue(new Callback<Tweet>() {
                        @Override
                        public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                            if (response.code()==200) {
                                showToast("发表成功");
                                EditActivity.this.setResult(200);
                                EditActivity.this.finish();
                            } else {
                                showToast("发表成功");
                            }
                        }

                        @Override
                        public void onFailure(Call<Tweet> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        });
    }
    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
