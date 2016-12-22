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
import cc.wo_mo.dubi.data.Model.Comment;
import cc.wo_mo.dubi.data.Model.Tweet;
import cc.wo_mo.dubi.utils.TimeTool;

/**
 * Created by shushu on 2016/12/14.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Comment> mComments;
    private Context mContext;
    private Tweet mTweet;

    public CommentAdapter(Context context,List<Comment> comments, Tweet tweet){
        this.mContext = context;
        mComments = comments;
        mTweet = tweet;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if (i == 0) {
            view = LayoutInflater.from(mContext).inflate(R.layout.tweet_item,viewGroup,false);
            return new TweetAdapter().new TweetViewHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.comment_item,viewGroup,false);
            return new CommentViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder myViewHolder, int position) {
        if (position == 0) {
            TweetAdapter.setViewContent(mContext, (TweetAdapter.TweetViewHolder)myViewHolder, mTweet);
            LinearLayout ll = new LinearLayout(mContext);
            ll.setOrientation(LinearLayout.VERTICAL);
            float density = mContext.getResources().getDisplayMetrics().density;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 10, 0, (int)(20*density));
            myViewHolder.itemView.setLayoutParams(layoutParams);
            ((TweetAdapter.TweetViewHolder) myViewHolder).deleteBtn.setVisibility(View.GONE);
            ((TweetAdapter.TweetViewHolder) myViewHolder).commentBtn.setOnClickListener(null);
        } else {
            final CommentViewHolder viewHolder = (CommentViewHolder)myViewHolder;
            if (position == mComments.size()) {
                viewHolder.lastItemHint.setVisibility(View.VISIBLE);
            } else {
                viewHolder.lastItemHint.setVisibility(View.GONE);
            }
            position--;

            Comment c = mComments.get(position);
            viewHolder.user_name.setText(c.from_user.username);
            viewHolder.text.setText(c.content);
            viewHolder.time.setText(TimeTool.getStandardTime(c.time));
        }
    }




    @Override
    public int getItemCount() {
        if (mComments == null)
            return 1;
        return mComments.size()+1;
    }

    public void refreshComments(List<Comment> comments) {
        mComments = comments;
        notifyDataSetChanged();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView user_name,text, lastItemHint, time;
        public CommentViewHolder(View itemView) {
            super(itemView);
            user_name = (TextView)itemView.findViewById(R.id.user_name);
            text = (TextView)itemView.findViewById(R.id.text);
            lastItemHint = (TextView) itemView.findViewById(R.id.last_item_hint);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
