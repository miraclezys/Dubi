package cc.wo_mo.dubi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.util.List;

import cc.wo_mo.dubi.R;
import cc.wo_mo.dubi.data.ApiClient;
import cc.wo_mo.dubi.Model.BaseResponse;
import cc.wo_mo.dubi.Model.Tweet;
import cc.wo_mo.dubi.Model.User;
import cc.wo_mo.dubi.utils.ImageUtils;
import cc.wo_mo.dubi.utils.ProcessBitmap;
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
    UserInfoAdapter mAdapter;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    ImageView userPhoto;
    int lastVisiblePosition;
    List<Tweet> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_information);
        user = ApiClient.gson.fromJson(getIntent().getStringExtra("user"), User.class);
        initViews();
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
                        getResources().getDisplayMetrics()));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        refresh();
    }

    private void initViews() {
        Toolbar mToolbar = (Toolbar)findViewById(R.id.toolbar);
        CollapsingToolbarLayout cl = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        cl.setTitle(user.username);
        userPhoto = (ImageView) findViewById(R.id.user_pic_img);
        if (user.photo_url != null) {
            ImageUtils.with(this).load(ApiClient.BASE_URL+user.photo_url)
                    .transform(new ProcessBitmap(ProcessBitmap.MODE_CIRCLE, 200, null))
                    .into(userPhoto);
        }
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
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new UserInfoAdapter(this, null, user);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void refresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        ApiClient.getClient(this).getUserInfo(user.user_id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    user = response.body();
                    updateViews();
                    if (user.photo_url != null) {
                        ImageUtils.with(UserInfoActivity.this)
                                .load(ApiClient.BASE_URL+user.photo_url)
                                .transform(new ProcessBitmap(ProcessBitmap.MODE_CIRCLE, 200, null))
                                .into(userPhoto);
                    }
                    mAdapter.refreshUser(user);
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    try {
                        Log.d("response",response.errorBody().string());
                        mSwipeRefreshLayout.setRefreshing(false);
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

        ApiClient.getClient(this).listUserTweets(user.user_id, -1, 100)
                .enqueue(new Callback<List<Tweet>>() {
                    @Override
                    public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                        if (response.code() == 200) {
                            mAdapter.refreshTweets(response.body());
                            mSwipeRefreshLayout.setRefreshing(false);
                        } else {
                            try {
                                Log.d("response",response.errorBody().string());
                                mSwipeRefreshLayout.setRefreshing(false);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
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
                startActivityForResult(intent, 0);
                break;
            case R.id.return_button:
                finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refresh();
    }
}
