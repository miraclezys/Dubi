package cc.wo_mo.dubi.ui;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cc.wo_mo.dubi.R;

/**
 * Created by SHIN on 2016/12/14.
 */
public class FansActivty extends AppCompatActivity{
    List<Map<String,Object>> fansData;
    ListView lv1;
    Toolbar tb;
    TextView tbtittle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        tb = (Toolbar) findViewById(R.id.toolbar);
        tbtittle = (TextView) findViewById(R.id.TBtittle);
        tbtittle.setText("我的粉丝");
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fansData = new ArrayList<>();
        for(int i = 1;i<=10;i++){
            Map<String,Object>temp = new LinkedHashMap<>();
            temp.put("user_name","user_name"+i);
            fansData.add(temp);
        }
        lv1 = (ListView) findViewById(R.id.user_list);
        final SimpleAdapter simpleAdapter = new SimpleAdapter(this, fansData,R.layout.item_friends,
                new String[] {"user_name"},new int[] {R.id.user_name} );
        lv1.setAdapter(simpleAdapter);
    }
}
