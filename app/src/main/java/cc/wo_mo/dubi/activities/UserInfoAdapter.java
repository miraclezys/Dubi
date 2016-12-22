package cc.wo_mo.dubi.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cc.wo_mo.dubi.R;
import cc.wo_mo.dubi.TweetAdapter;
import cc.wo_mo.dubi.data.Model.Comment;
import cc.wo_mo.dubi.data.Model.Tweet;
import cc.wo_mo.dubi.data.Model.User;
import cc.wo_mo.dubi.utils.TimeTool;

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
    public void onBindViewHolder(RecyclerView.ViewHolder myViewHolder, int position) {
        if (position == 0) {
            InfomationViewHolder infoHolder = (InfomationViewHolder)myViewHolder;
            infoHolder.birth.setText(mUser.birth);
            infoHolder.gender.setText(mUser.gender);
            infoHolder.introduction.setText(mUser.introduction);
            infoHolder.region.setText(mUser.region);
        } else {
            TweetAdapter.setViewContent(mContext,
                    (TweetAdapter.TweetViewHolder)myViewHolder, mTweets.get(position-1));

        }
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