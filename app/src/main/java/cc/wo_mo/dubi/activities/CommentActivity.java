package cc.wo_mo.dubi.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cc.wo_mo.dubi.R;

/**
 * Created by shushu on 2016/12/14.
 */

public class CommentActivity extends AppCompatActivity {
    RecyclerView rv1;
    List<Map<String,Object>> notifyData;
    ListView lv1;
    Toolbar tb;
    TextView tbtittle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_layout);
        rv1 = (RecyclerView)findViewById(R.id.comment_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv1.setLayoutManager(layoutManager);


        notifyData = new ArrayList<>();
        for(int i = 1;i<=10;i++){
            Map<String,Object>temp = new LinkedHashMap<>();
            temp.put("user_name","user_name"+i);
            for(int j = 1;j<=5;j++) {
                temp.put("text", "英国是个可爱的国家那里的景色超级美的，旅馆也很雅致，生活如此多娇 " + i);
            }
            notifyData.add(temp);
        }
        CommentAdapter notifyAdapter = new CommentAdapter(CommentActivity.this,notifyData);
        rv1.setAdapter(notifyAdapter);
        final View layout = (View)findViewById(R.id.tweet);
        ImageButton mSend = (ImageButton)layout.findViewById(R.id.comment_button);

        mSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                System.out.println("!!!!!");
            }
        });
    }
}
