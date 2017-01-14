package cc.wo_mo.dubi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.List;

import cc.wo_mo.dubi.R;
import cc.wo_mo.dubi.utils.ApiClient;
import cc.wo_mo.dubi.Model.Comment;
import cc.wo_mo.dubi.Model.Tweet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shushu on 2016/12/14.
 */
public class CommentActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    Tweet tweet;
    CommentAdapter mAdapter;
    Toolbar tb;
    SwipeRefreshLayout mSwipeRefreshLayout;
    EditText mEditText;
    Button returnButton;
    Button sendButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_layout);
        Log.d("fuck", getIntent().getStringExtra("tweet"));
        tweet = ApiClient.gson.fromJson(getIntent().getStringExtra("tweet"), Tweet.class);
        init();
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

    private void refresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        ApiClient.getClient(this).getComments(tweet.tweet_id, -1, 100)
                .enqueue(new Callback<List<Comment>>() {
                    @Override
                    public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        if (response.code() == 200) {
                            mAdapter.refreshComments(response.body());
                            reset();
                            mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount()-1);
                            Log.d("comment", ApiClient.gson.toJson(response.body()));
                        } else {
                            try {
                                Log.d("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Comment>> call, Throwable t) {
                        t.printStackTrace();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    void reset() {
        // 关闭输入法
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken()
                ,InputMethodManager.HIDE_NOT_ALWAYS);
        // 清空输入框
        mEditText.getText().clear();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        super.onBackPressed();
    }

    private void init() {
        mRecyclerView = (RecyclerView)findViewById(R.id.comment_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new CommentAdapter(this, null, tweet);
        mRecyclerView.setAdapter(mAdapter);
        mEditText = (EditText) findViewById(R.id.comment_edit);
        sendButton = (Button) findViewById(R.id.send_button);
        returnButton =(Button)findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = mEditText.getText().toString();
                if (!"".equals(content)) {
                    ApiClient.getClient(CommentActivity.this).createComment(tweet.tweet_id,
                            new Comment(ApiClient.user_id, content, null))
                            .enqueue(new Callback<Comment>() {
                                @Override
                                public void onResponse(Call<Comment> call, Response<Comment> response) {
                                    if (response.code() == 200) {
                                        refresh();
                                    } else {
                                        try {
                                            Log.d("error", response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Comment> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });
                }
            }
        });
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }
}
