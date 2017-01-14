package cc.wo_mo.dubi.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import cc.wo_mo.dubi.R;
import cc.wo_mo.dubi.data.ApiClient;
import cc.wo_mo.dubi.Model.BaseResponse;
import cc.wo_mo.dubi.Model.Tweet;
import cc.wo_mo.dubi.Model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by womo on 2016/12/22.
 */
public class UserInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Tweet> mTweets;
    private Context mContext;
    private User mUser;

    public UserInfoAdapter(Context context,List<Tweet> tweets, User user){
        mContext = context;
        mTweets = tweets;
        mUser = user;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if (i == 0) {
            view = LayoutInflater.from(mContext).inflate(R.layout.infomation_card,viewGroup,false);
            return new InfomationViewHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.tweet_item,viewGroup,false);
            return new TweetAdapter().new TweetViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder myViewHolder, final int position) {
        if (position == 0) {
            InfomationViewHolder infoHolder = (InfomationViewHolder)myViewHolder;
            infoHolder.birth.setText(mUser.birth);
            infoHolder.gender.setText(mUser.gender);
            infoHolder.introduction.setText(mUser.introduction);
            infoHolder.region.setText(mUser.region);
        } else {
            TweetAdapter.setViewContent(mContext,
                    (TweetAdapter.TweetViewHolder)myViewHolder, mTweets.get(position-1));
            if (mUser.user_id == ApiClient.user_id) {
                ((TweetAdapter.TweetViewHolder)myViewHolder).deleteBtn.setVisibility(View.VISIBLE);
                ((TweetAdapter.TweetViewHolder)myViewHolder).deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(mContext)
                                .setTitle("是否删除？")
                                .setNegativeButton("取消", null)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteItem(position-1);
                                    }
                                }).create().show();
                    }
                });
            } else {
                ((TweetAdapter.TweetViewHolder)myViewHolder).deleteBtn.setVisibility(View.INVISIBLE);
            }

        }
    }

    private void deleteItem(final int position) {
        ApiClient.getClient(mContext).deleteTweet(ApiClient.user_id, mTweets.get(position).tweet_id)
                .enqueue(new Callback<BaseResponse>() {
                             @Override
                             public void onResponse(Call<BaseResponse> call,
                                                    Response<BaseResponse> response) {
                                 if (response.code() == 200) {
                                     mTweets.remove(position);
                                     notifyDataSetChanged();
                                 } else {
                                     try {
                                         Log.d("error in delete", response.errorBody().string());
                                     } catch (IOException e) {
                                         e.printStackTrace();
                                     }
                                 }
                             }

                             @Override
                             public void onFailure(Call<BaseResponse> call, Throwable t) {
                                 t.printStackTrace();
                             }
                         }
                );
    }


    @Override
    public int getItemCount() {
        if (mTweets == null)
            return 1;
        return mTweets.size()+1;
    }

    public void refreshTweets(List<Tweet> tweet) {
        mTweets = tweet;
        notifyDataSetChanged();
    }

    public void refreshUser(User user) {
        mUser = user;
        notifyDataSetChanged();
    }

    public class InfomationViewHolder extends RecyclerView.ViewHolder {
        TextView introduction, gender, birth, region;
        public InfomationViewHolder(View itemView) {
            super(itemView);
            introduction = (TextView)itemView.findViewById(R.id.introduction_text);
            gender = (TextView)itemView.findViewById(R.id.gender);
            birth = (TextView) itemView.findViewById(R.id.birth);
            region = (TextView) itemView.findViewById(R.id.region);
        }
    }
}