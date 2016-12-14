package cc.wo_mo.dubi.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.wo_mo.dubi.R;

/**
 * Created by shushu on 2016/12/14.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Map<String,Object>> list = new ArrayList<>();
    private Context myContext;
    private LayoutInflater inflater;

    public CommentAdapter(Context context,List<Map<String,Object>>datas){
        this.myContext = context;
        this.list = datas;
        inflater = LayoutInflater.from(myContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if (i == 0) {
            view = inflater.inflate(R.layout.tweet_item,viewGroup,false);
            return new TweetViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.comment_item,viewGroup,false);
            return new CommentViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder myViewHolder, int i) {
        if (i == 0) {
            LinearLayout ll = new LinearLayout(myContext);
            ll.setOrientation(LinearLayout.VERTICAL);
            DisplayMetrics dm = new DisplayMetrics();
            float density = myContext.getResources().getDisplayMetrics().density;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 10, 0, (int)(20*density));
            myViewHolder.itemView.setLayoutParams(layoutParams);
        } else {
            i--;
            ((CommentViewHolder)myViewHolder).user_name.setText(list.get(i).get("user_name").toString());
            ((CommentViewHolder)myViewHolder).text.setText(list.get(i).get("text").toString());
        }
    }



    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView user_name,text;
        public CommentViewHolder(View itemView) {
            super(itemView);
            user_name = (TextView)itemView.findViewById(R.id.user_name);
            text = (TextView)itemView.findViewById(R.id.text);
        }
    }

    public class TweetViewHolder extends RecyclerView.ViewHolder {
        ImageView userPhoto;
        TextView username;
        TextView time;
        TextView text;
        ImageView picture;
        ImageButton deleteBtn;
        ImageButton commentBtn;

        public TweetViewHolder(View itemView) {
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
