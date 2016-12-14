package cc.wo_mo.dubi.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
                LayoutInflater factory = LayoutInflater.from(MyInfoActivity.this);
                final View view1 = factory.inflate(R.layout.i_information_edit, null);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MyInfoActivity.this);
                builder1.setView(view1);
                builder1.setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                        .setNegativeButton("取消修改", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create();
                builder1.show();
            }
        });
    }
}
