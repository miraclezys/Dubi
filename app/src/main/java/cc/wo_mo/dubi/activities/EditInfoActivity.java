package cc.wo_mo.dubi.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import cc.wo_mo.dubi.R;

/**
 * Created by shushu on 2016/12/15.
 */

public class EditInfoActivity extends AppCompatActivity {
    EditText birth;
    public String birthstr="0000/00/00";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        birth=(EditText)findViewById(R.id.birth);
        birth.setFocusable(false);
        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditInfoActivity.this, "点击生日", Toast.LENGTH_SHORT).show();
                    //自定义对话框
                    LayoutInflater factory = LayoutInflater.from(EditInfoActivity.this);
                    View view2 = factory.inflate(R.layout.dialog_edit_birth,null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditInfoActivity.this);//this
                    //初始化
                    final DatePicker mDatePicker=(DatePicker)view2.findViewById(R.id.datePicker);
                    final EditText mEditText=(EditText)view2.findViewById(R.id.edit);
                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    //设置电话号码
                    mDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker arg0, int year, int month, int day) {
                            month=month+1;
                            birthstr = year + "." + month + "." + day;

                        }
                    });

                    builder.setView(view2).setTitle("o(≧v≦)o")
                            .setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    birth.setText(birthstr);
                                    Toast.makeText(EditInfoActivity.this,"修改成功!"+birthstr, Toast.LENGTH_SHORT).show();

                                }
                            }).setNegativeButton("放弃修改", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(EditInfoActivity.this,"取消修改!", Toast.LENGTH_SHORT).show();
                        }
                    }).create().show();
                }



        });


    }
}
