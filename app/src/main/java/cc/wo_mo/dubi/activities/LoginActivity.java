package cc.wo_mo.dubi.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import cc.wo_mo.dubi.R;
import cc.wo_mo.dubi.data.ApiClient;
import cc.wo_mo.dubi.data.DubiService;
import cc.wo_mo.dubi.data.Model.BaseResponse;
import cc.wo_mo.dubi.data.Model.LoginResponse;
import cc.wo_mo.dubi.data.Model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    final static int SIGN_IN = 0;
    final static int SIGN_UP = 1;
    Button mSubmitButton;
    Button mSignInButton;
    Button mSignUpButton;
    EditText mUsername;
    EditText mPassword;
    EditText mConfirmPassword;
    DubiService client = ApiClient.getClient();
    int state = SIGN_IN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        init();
        setButtonListener();
    }

    private void init() {
        mSubmitButton = (Button) findViewById(R.id.submit);
        mSignInButton = (Button) findViewById(R.id.sign_in);
        mSignUpButton = (Button) findViewById(R.id.sign_up);
        mUsername = (EditText) findViewById(R.id.name);
        mPassword = (EditText) findViewById(R.id.password_one);
        mConfirmPassword = (EditText) findViewById(R.id.password_two);
        mSignInButton.setTextColor(getResources().getColor(R.color.colorAccent));
        mConfirmPassword.setVisibility(View.GONE);
    }

    private void setButtonListener() {
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignInButton.setTextColor(getResources().getColor(R.color.colorAccent));
                mSignUpButton.setTextColor(getResources().getColor(R.color.colorGray));
                mConfirmPassword.setVisibility(View.GONE);
                state = SIGN_IN;
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignInButton.setTextColor(getResources().getColor(R.color.colorGray));
                mSignUpButton.setTextColor(getResources().getColor(R.color.colorAccent));
                mConfirmPassword.setVisibility(View.VISIBLE);
                state = SIGN_UP;
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getEditableText().toString();
                String password = mPassword.getEditableText().toString();
                if (state == SIGN_IN) {
                    client.login(username, password).enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call,
                                               Response<LoginResponse> response) {
                            LoginResponse res = response.body();
                            if (response.code() == 200) {
                                showToast("Sign in success");
                                ApiClient.token = res.token;
                                ApiClient.user_id = res.user_id;
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                } else {
                    client.registration(username, password).enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call,
                                               Response<BaseResponse> response) {
                            BaseResponse res = response.body();
                            if (res == null)
                                try {
                                    res = ApiClient.gson.fromJson(response.errorBody().string(),
                                            BaseResponse.class);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            showToast(res.message);
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
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