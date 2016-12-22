package cc.wo_mo.dubi.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.wo_mo.dubi.R;
/**
 * Created by SHIN on 2016/12/12.
 */
public class MyNotifyAdapter extends RecyclerView.Adapter<MyNotifyAdapter.MyViewHolder>{
    private List<Map<String,Object>> notilist = new ArrayList<>();
    private Context myContext;
    private LayoutInflater inflater;

    public MyNotifyAdapter(Context context,List<Map<String,Object>>datas){
        this.myContext = context;
        this.notilist = datas;
        inflater = LayoutInflater.from(myContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_notify,viewGroup,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.user_name.setText(notilist.get(i).get("user_name").toString());
        myViewHolder.text.setText(notilist.get(i).get("text").toString());
    }

    @Override
    public int getItemCount() {
        return notilist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView user_name,text;
        public MyViewHolder(View itemView) {
            super(itemView);
            user_name = (TextView)itemView.findViewById(R.id.user_name);
            text = (TextView)itemView.findViewById(R.id.text);
        }
    }
}
