package cc.wo_mo.dubi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import cc.wo_mo.dubi.activities.CommentActivity;
import cc.wo_mo.dubi.activities.ImageActivity;
import cc.wo_mo.dubi.activities.UserInfoActivity;
import cc.wo_mo.dubi.data.ApiClient;
import cc.wo_mo.dubi.data.Model.BaseResponse;
import cc.wo_mo.dubi.data.Model.Tweet;
import cc.wo_mo.dubi.utils.ImageUtils;
import cc.wo_mo.dubi.utils.ProcessBitmap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by womo on 2016/12/10.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.MyViewHolder> {

    public List<Tweet> mData;
    private Context mContex;
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Tweet tweet = mData.get(position);
        setViewContent(mContex, holder, mData.get(position));
        if (tweet.user.user_id == ApiClient.user_id) {
            holder.deleteBtn.setVisibility(View.VISIBLE);
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mContex)
                            .setTitle("是否删除？")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteItem(position);
                                }
                            }).create().show();
                }
            });
        } else {
            holder.deleteBtn.setVisibility(View.INVISIBLE);
        }
    }

    public static void setViewContent(final Context context, MyViewHolder holder, final Tweet tweet) {
        holder.username.setText(tweet.user.username);
        holder.text.setText(tweet.description);
        if (tweet.image_url != null) {
            int width =  context.getResources().getDisplayMetrics().widthPixels-
                    ((ViewGroup.MarginLayoutParams)(holder.itemView.getLayoutParams())).leftMargin*2;
            holder.picture.setVisibility(View.VISIBLE);
            ImageUtils.with(context)
                    .load(ApiClient.BASE_URL+tweet.image_url)
                    .transform(new ProcessBitmap(ProcessBitmap.MODE_FIX_SIZE,width, width*2/3))
                    .into(holder.picture);
            holder.picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent imageIntent = new Intent(context, ImageActivity.class);
                    imageIntent.putExtra("url", ApiClient.BASE_URL+tweet.image_url);
                    context.startActivity(imageIntent);
                }
            });
        } else {
            holder.picture.setVisibility(View.GONE);
        }
        holder.userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra("user", ApiClient.gson.toJson(tweet.user));
                context.startActivity(intent);
            }
        });
        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("tweet", ApiClient.gson.toJson(tweet));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mData != null)
            return mData.size();
        return 0;
    }

    private void deleteItem(final int position) {
        ApiClient.getClient(mContex).deleteTweet(ApiClient.user_id, mData.get(position).tweet_id)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call,
                                           Response<BaseResponse> response) {
                        if (response.code() == 200) {
                            mData.remove(position);
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

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView userPhoto;
        TextView username;
        TextView time;
        TextView text;
        ImageView picture;
        ImageButton deleteBtn;
        ImageButton commentBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            userPhoto = (ImageView)itemView.findViewById(R.id.user_pic_img);
            username = (TextView)itemView.findViewById(R.id.user_name);
            time = (TextView)itemView.findViewById(R.id.time);
            text = (TextView)itemView.findViewById(R.id.text);
            picture = (ImageView)itemView.findViewById(R.id.picture);
            deleteBtn = (ImageButton) itemView.findViewById(R.id.delete_button);
            commentBtn = (ImageButton) itemView.findViewById(R.id.comment_button);
        }
    }
}
