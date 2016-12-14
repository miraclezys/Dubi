package cc.wo_mo.dubi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import cc.wo_mo.dubi.R;

/**
 * Created by shushu on 2016/12/15.
 */

public class MyInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.i_information);
        Button mEditButton = (Button)findViewById(R.id.edit_button);
        mEditButton.setOnClickListener(new View.OnClickListener(){
            @Override
             public void onClick(View view){
                Intent intent = new Intent(MyInfoActivity.this,MyInfoEditActivity.class);
                startActivity(intent);
            }
        });
    }
}
