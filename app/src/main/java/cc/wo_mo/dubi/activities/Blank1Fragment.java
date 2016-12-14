package cc.wo_mo.dubi.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

import cc.wo_mo.dubi.R;
import cc.wo_mo.dubi.data.ApiClient;
import cc.wo_mo.dubi.data.Model.Tweet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Blank1Fragment extends BlankFragment {
    @Override
    void getData() {
        mSwipeRefreshLayout.setRefreshing(true);
        Log.d("header token", ApiClient.token);
        client.listFriendsTweets(ApiClient.user_id,-1, 100).enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (response.code() == 200) {
                    mAdapter.mData = response.body();
                    mAdapter.notifyDataSetChanged();
                    Log.d("tweet", ApiClient.gson.toJson(data));
                } else {
                    try {
                        Log.d("error", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
