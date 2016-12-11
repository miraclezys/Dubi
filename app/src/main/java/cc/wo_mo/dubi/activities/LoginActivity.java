package cc.wo_mo.dubi.activities;

import android.app.ActionBar;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    View mSignInView;
    View mSignUpView;
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
        mSubmitButton = (Button) findViewById(R.id.submit_button);
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignUpButton = (Button) findViewById(R.id.sign_up_button);
        mUsername = (EditText) findViewById(R.id.name_edit);
        mPassword = (EditText) findViewById(R.id.password_one_edit);
        mConfirmPassword = (EditText) findViewById(R.id.password_two_edit);
        mSignInView = (View) findViewById(R.id.in_view);
        mSignUpView = (View) findViewById(R.id.up_view);
        mSignInView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mSignUpView.setVisibility(View.INVISIBLE);
        mSignInButton.setTextColor(getResources().getColor(R.color.colorAccent));
        mConfirmPassword.setVisibility(View.GONE);
    }

    private void setButtonListener() {
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignInButton.setTextColor(getResources().getColor(R.color.colorAccent));
                mSignUpButton.setTextColor(getResources().getColor(R.color.colorGray));
                mSignInView.setVisibility(View.VISIBLE);
                mSignUpView.setVisibility(View.INVISIBLE);
                mSignInView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                mConfirmPassword.setVisibility(View.GONE);
                state = SIGN_IN;
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignInButton.setTextColor(getResources().getColor(R.color.colorGray));
                mSignUpButton.setTextColor(getResources().getColor(R.color.colorAccent));
                mSignInView.setVisibility(View.INVISIBLE);
                mSignUpView.setVisibility(View.VISIBLE);
                mSignUpView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
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
