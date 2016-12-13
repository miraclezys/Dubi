package cc.wo_mo.dubi.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cc.wo_mo.dubi.R;
/**
 * Created by SHIN on 2016/12/12.
 */
public class MYNotifyActivity extends AppCompatActivity {
    RecyclerView rv1;
    List<Map<String,Object>> notifyData;
    ListView lv1;
    Toolbar tb;
    TextView tbtittle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mynotify);

        rv1 = (RecyclerView)findViewById(R.id.Notiftlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv1.setLayoutManager(layoutManager);

        tb = (Toolbar) findViewById(R.id.toolbar);
        tbtittle = (TextView) findViewById(R.id.TBtittle);
        tbtittle.setText("消息通知");
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        notifyData = new ArrayList<>();
        for(int i = 1;i<=10;i++){
            Map<String,Object>temp = new LinkedHashMap<>();
            temp.put("user_name","user_name"+i);
            for(int j = 1;j<=5;j++) {
                temp.put("text", "This is the text " + i);
            }
            notifyData.add(temp);
        }
        MyNotifyAdapter notifyAdapter = new MyNotifyAdapter(MYNotifyActivity.this,notifyData);
        rv1.setAdapter(notifyAdapter);
    }
}
