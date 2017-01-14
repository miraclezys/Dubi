package cc.wo_mo.dubi.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cc.wo_mo.dubi.R;
import cc.wo_mo.dubi.Model.StateModel;
import cc.wo_mo.dubi.utils.DoubleClickTool;
import cc.wo_mo.dubi.utils.XmlParserHandler;

/**
 * Created by shushu on 2016/12/21.
 */

public class StateDialog extends Dialog implements
        android.view.View.OnClickListener {
    private TextView mTitle;
    private ListView StateListView;
    private Button CancelButton;
    private Button SureButton;
    private MSharePreferences mSharePreferences;
    private List<StateModel> States = new ArrayList<StateModel>();
    private StateAdapter mStateAdapter;
    private StateAdapter mCityAdapter;

    public interface OnStateSelectListener {
        void onStateSelect(String State, String City);
    }
    private OnStateSelectListener mLister;
/*
    public StateDialog(Context context, OnStateSelectListener onStateSelectListener){
        //this(context, onStateSelectListener);
    }
*/
    public StateDialog(Context context, int theme, OnStateSelectListener onStateSelectListener){
        super(context, theme);
        mLister = onStateSelectListener;
    }

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.state_list, null);
        mTitle = (TextView)mView.findViewById(R.id.title);
        StateListView = (ListView)mView.findViewById(R.id.list);
        CancelButton = (Button)mView.findViewById(R.id.cancel_button);
        SureButton = (Button)mView.findViewById(R.id.sure_button);
        CancelButton.setOnClickListener(this);
        SureButton.setOnClickListener(this);

        setContentView(mView);
        setCancelable(true);

        mSharePreferences = MSharePreferences.getInstance();
        mSharePreferences.getSharedPreferences(getContext());
        initStateDatas();
        initState();
        initDialogSize();
    }

    private boolean isCity;

    public void initState(){
        mTitle.setText("选择省份");
        mStateAdapter = new StateAdapter(getContext(), States);
        StateListView.setAdapter(mStateAdapter);
        StateListView.setSelection(mSharePreferences.getInt(DoubleClickTool.KEY_PROVINCE,0));
        isCity = true;
    }

    public void initDialogSize(){
        Window dialogWindow = getWindow();
        DisplayMetrics d = getContext().getResources().getDisplayMetrics();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.height = LayoutParams.WRAP_CONTENT;
        p.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(p);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.cancel_button:
                dismiss();
                break;
            case R.id.sure_button:
                int StatePosition = mStateAdapter.getSelectedIndex();
                String State = States.get(StatePosition).getName();
                String City;
                if(isCity){
                    mTitle.setText("选择城市");
                    mCityAdapter = new StateAdapter(getContext(), States.get(StatePosition).getCityList(), false);
                    StateListView.setAdapter(mCityAdapter);
                    isCity = false;
                }
                else{
                    City = States.get(StatePosition).getCityList().get(mCityAdapter.getSelectedIndex()).getName();
                    mSharePreferences.putInt(DoubleClickTool.KEY_PROVINCE, StatePosition);
                    mLister.onStateSelect(State, City);
                    dismiss();
                }
                break;
            default:
                break;
        }
    }

    public void initStateDatas(){
        AssetManager asset = getContext().getAssets();
        try {
            InputStream input = asset.open("china.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml //取得SAXParserFactory实例
            SAXParser parser = spf.newSAXParser();
            //从factory获取SAXParser实例
            XmlParserHandler handler = new XmlParserHandler();
            //实例化自定义Handler
            parser.parse(input, handler);
            //根据自定义Handler规则解析输入流
            input.close();
            // 获取解析出来的数据
            States = handler.getDataList();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
