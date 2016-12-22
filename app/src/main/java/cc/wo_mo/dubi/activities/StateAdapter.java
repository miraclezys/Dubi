package cc.wo_mo.dubi.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cc.wo_mo.dubi.R;
import cc.wo_mo.dubi.data.Model.CityModel;
import cc.wo_mo.dubi.data.Model.StateModel;
import cc.wo_mo.dubi.data.Model.Tools;


/**
 * Created by shushu on 2016/12/21.
 */

public class StateAdapter extends BaseAdapter{
    private Context Context;
    private LayoutInflater mInflater;
    private int Index = -1;
    private int SelectedIndex = 0;
    private boolean flag = true;
    private HashMap<String,Boolean> Status = new HashMap<String,Boolean>();
    private List<StateModel> States = new ArrayList<StateModel>();
    private List<CityModel> Cities = new ArrayList<CityModel>();
    private boolean defaultPosition = true;

    public int getSelectedIndex(){
        return SelectedIndex;
    }

    public void setSelectedIndex(int SelectedIndex){
        this.SelectedIndex = SelectedIndex;
    }

    public StateAdapter(Context context, List<StateModel> States){
        this.Context = context;
        this.States = States;
        this.mInflater = LayoutInflater.from(context);
    }

    public StateAdapter(Context context, List<CityModel> Cities, boolean flag){
        this.Context = context;
        this.Cities = Cities;
        this.flag = flag;
        this.mInflater = LayoutInflater.from(context);
    }

    class ViewHolder {
        private RadioButton StateItem;
        private RadioGroup StateRadioGroup;
    }

    @Override
    public int getCount(){
        System.out.println("!!! flag "+flag+" states "+States.size()+" city "+Cities.size());
        return flag ? States.size() : Cities.size();
    }

    @Override
    public Object getItem(int position){
        return flag? States.get(position) : Cities.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final ViewHolder mViewHolder;
        MSharePreferences sharePreferences = MSharePreferences.getInstance();
        sharePreferences.getSharedPreferences(Context);
        if(convertView == null){
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.state_item, null);
            mViewHolder.StateItem = (RadioButton) convertView.findViewById(R.id.radio_button);
            convertView.setTag(mViewHolder);
        }
        else{
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        if(flag){
            mViewHolder.StateItem.setText(States.get(position).getName());
        }
        else{
            mViewHolder.StateItem.setText(Cities.get(position).getName());
        }

        mViewHolder.StateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(String key : Status.keySet()){
                    Status.put(key, false);
                }
                defaultPosition = false;
                Status.put(String.valueOf(position), mViewHolder.StateItem.isChecked());
                StateAdapter.this.notifyDataSetChanged();
            }
        });

        boolean temp = false;
        if(Status.get(String.valueOf(position)) == null
                || Status.get(String.valueOf(position)) == false){
            temp = false;
            Status.put(String.valueOf(position),false);
        }
        else{
            SelectedIndex = position;
            temp = true;
        }

        if(flag){
            if(position == sharePreferences.getInt(Tools.KEY_PROVINCE, 0)
                    && defaultPosition){
              //  SelectedIndex = position;
               // mViewHolder.StateItem.setChecked(true);
            } else {
                if(position == 0 && defaultPosition){
                //    mViewHolder.StateItem.setChecked(true);
                }
            }
        }
        return convertView;
    }
}
