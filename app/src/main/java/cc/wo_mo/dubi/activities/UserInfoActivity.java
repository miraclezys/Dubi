package cc.wo_mo.dubi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.List;

import cc.wo_mo.dubi.R;
import cc.wo_mo.dubi.TweetAdapter;
import cc.wo_mo.dubi.data.ApiClient;
import cc.wo_mo.dubi.data.Model.BaseResponse;
import cc.wo_mo.dubi.data.Model.Tweet;
import cc.wo_mo.dubi.data.Model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shushu on 2016/12/13.
 */

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener{
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    Button follwButton;
    Button editButton;
    Button returnButton;
    User user;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    LinearLayoutManager layoutManager;
    int lastVisiblePosition;
    TweetAdapter mAdapter;
    List<Tweet> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_information);
        user = ApiClient.gson.fromJson(getIntent().getStringExtra("user"), User.class);
        initViews();
        refresh();
//        Resources resource=(Resources)getBaseContext().getResources();
//        ColorStateList csl=(ColorStateList)resource.getColorStateList(R.color.colorToolBar);

//        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

    }

    private void initViews() {
        Toolbar mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        CollapsingToolbarLayout cl = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        cl.setTitle(user.username);
        follwButton = (Button) findViewById(R.id.follow_button);
        editButton = (Button) findViewById(R.id.edit_button);
        returnButton = (Button) findViewById(R.id.return_button);
        follwButton.setOnClickListener(this);
        editButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);
        if (user.user_id == ApiClient.user_id) {
            follwButton.setVisibility(View.GONE);
        } else {
            editButton.setVisibility(View.GONE);
        }
    }

    private void refresh() {
        ApiClient.getClient(this).getUserInfo(user.user_id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    user = response.body();
                } else {
                    try {
                        Log.d("response",response.errorBody().string());
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

    private void updateViews() {
        if (user.isFriend) {
            follwButton.setText("取消关注");
        } else {
            follwButton.setText("关注");
        }
        // Todo: 更新其他信息
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.follow_button:
                if (user.isFriend == true) {
                    ApiClient.getClient(this).unfollow(ApiClient.user_id, user.user_id)
                            .enqueue(new Callback<BaseResponse>() {
                                @Override
                                public void onResponse(Call<BaseResponse> call,
                                                       Response<BaseResponse> response) {
                                    if (response.code() == 200) {
                                        user.isFriend = false;
                                        follwButton.setText("关注");
                                    } else {
                                        try {
                                            Log.d("response",response.errorBody().string());
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
                } else {
                    ApiClient.getClient(this).follow(ApiClient.user_id, user.user_id)
                            .enqueue(new Callback<BaseResponse>() {
                                @Override
                                public void onResponse(Call<BaseResponse> call,
                                                       Response<BaseResponse> response) {
                                    if (response.code() == 200) {
                                        user.isFriend = true;
                                        follwButton.setText("取消关注");
                                    } else {
                                        try {
                                            Log.d("response",response.errorBody().string());
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
                break;
            case R.id.edit_button:
                Intent intent  = new Intent(this, EditInfoActivity.class);
                intent.putExtra("user", ApiClient.gson.toJson(user));
                startActivity(intent);
                break;
            case R.id.return_button:
                Intent intent2 = new Intent(this,MainActivity.class);
                startActivity(intent2);
        }
    }
}
