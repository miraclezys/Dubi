package cc.wo_mo.dubi.ui;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import cc.wo_mo.dubi.R;
import cc.wo_mo.dubi.data.ApiClient;
import cc.wo_mo.dubi.data.DubiService;
import cc.wo_mo.dubi.Model.BaseResponse;
import cc.wo_mo.dubi.Model.LoginResponse;
import cc.wo_mo.dubi.data.MSharePreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    final static int SIGN_IN = 0;
    final static int SIGN_UP = 1;
    MSharePreferences mSharePreferences;
    Button mSignInButton;
    Button mSignUpButton;
    EditText mUsername;
    EditText mPassword;
    TextInputLayout mNameInput;
    TextInputLayout mPasswordInput;
    DubiService client;
    int state = SIGN_IN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharePreferences = MSharePreferences.getInstance(this);
        if (mSharePreferences.getBoolean("isLogin", false)) {
            ApiClient.token = mSharePreferences.getString("token", "");
            ApiClient.user_id = mSharePreferences.getInt("user_id", -1);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        setContentView(R.layout.login_layout);
        client = ApiClient.getClient(this);
        init();
        setButtonListener();
    }

    private void init() {
       // mSubmitButton = (Button) findViewById(R.id.submit_button);
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignUpButton = (Button) findViewById(R.id.sign_up_button);
        mUsername = (EditText) findViewById(R.id.name_edit);
        mPassword = (EditText) findViewById(R.id.password_one_edit);
       // mSignInButton.setTextColor(getResources().getColor(R.color.colorAccent));
        mNameInput = (TextInputLayout)findViewById(R.id.name_text_input);
        mPasswordInput = (TextInputLayout)findViewById(R.id.password_text_input);
    }

    private void setButtonListener() {
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getEditableText().toString();
                String password = mPassword.getEditableText().toString();
                if(TextUtils.isEmpty(username)){
                    mNameInput.setErrorEnabled(true);
                    mNameInput.setError("用户名不能为空");
                }
                else if(TextUtils.isEmpty(password)){
                    mPasswordInput.setErrorEnabled(true);
                    mPasswordInput.setError("密码不能为空");
                }
                else{
                    mNameInput.setErrorEnabled(false);
                    mPasswordInput.setErrorEnabled(false);
                    client.login(username, password).enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call,
                                               Response<LoginResponse> response) {
                            LoginResponse res = response.body();
                            if (response.code() == 200) {
                              //  showToast("Sign in success");
                                Snackbar.make(mSignInButton,"Sign in success",Snackbar.LENGTH_SHORT)
                                        .setDuration(1000)
                                        .show();
                                ApiClient.token = res.token;
                                ApiClient.user_id = res.user_id;
                                mSharePreferences.putBoolean("isLogin", true);
                                mSharePreferences.putString("token", res.token);
                                mSharePreferences.putInt("user_id", res.user_id);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getEditableText().toString();
                String password = mPassword.getEditableText().toString();
                if(TextUtils.isEmpty(username)){
                    mNameInput.setErrorEnabled(true);
                    mNameInput.setError("用户名不能为空");
                }
                else if(TextUtils.isEmpty(password)){
                    mPasswordInput.setErrorEnabled(true);
                    mPasswordInput.setError("密码不能为空");
                }
                else{
                    mNameInput.setErrorEnabled(false);
                    mPasswordInput.setErrorEnabled(false);
                    client.registration(username, password).enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call,
                                               Response<BaseResponse> response) {
                            if (response.code() == 200) {
                                BaseResponse res = response.body();
                                Snackbar.make(mSignInButton,res.message,Snackbar.LENGTH_SHORT)
                                        .setDuration(1000)
                                        .show();
                            } else {
                                try {
                                    Log.d("error",response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

    /*    mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getEditableText().toString();
                String password = mPassword.getEditableText().toString();
                if(TextUtils.isEmpty(username)){
                    mNameInput.setErrorEnabled(true);
                    mNameInput.setError("用户名不能为空");
                }
                else if(TextUtils.isEmpty(password)){
                    mPasswordInput.setErrorEnabled(true);
                    mPasswordInput.setError("密码不能为空");
                }
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
        });*/
    }

    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
