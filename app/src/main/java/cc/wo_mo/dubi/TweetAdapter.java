package cc.wo_mo.dubi;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import cc.wo_mo.dubi.data.ApiClient;
import cc.wo_mo.dubi.data.Model.Tweet;
import cc.wo_mo.dubi.utils.ImageUtils;

/**
 * Created by womo on 2016/12/10.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.MyViewHolder> {

    public List<Tweet> mData;
    Context mContex;
    public TweetAdapter(Context context, List<Tweet> data) {
        mData = data;
        mContex = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContex).inflate(R.layout.tweet_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Tweet tweet = mData.get(position);
        holder.username.setText(tweet.user.username);
        holder.text.setText(tweet.description);
        if (tweet.image_url != null) {
            holder.picture.setVisibility(View.VISIBLE);
            ImageUtils.with(mContex)
                    .load(ApiClient.BASE_URL+tweet.image_url)
                    .fit().into(holder.picture);
        } else {
            holder.picture.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (mData != null)
            return mData.size();
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView userPhoto;
        TextView username;
        TextView time;
        TextView text;
        ImageView picture;

        public MyViewHolder(View itemView) {
            super(itemView);
            userPhoto = (ImageView)itemView.findViewById(R.id.user_pic_img);
            username = (TextView)itemView.findViewById(R.id.user_name);
            time = (TextView)itemView.findViewById(R.id.time);
            text = (TextView)itemView.findViewById(R.id.text);
            picture = (ImageView)itemView.findViewById(R.id.picture);
        }
    }
}
