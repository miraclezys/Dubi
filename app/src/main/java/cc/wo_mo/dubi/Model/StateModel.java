package cc.wo_mo.dubi.Model;

import java.util.List;

/**
 * Created by shushu on 2016/12/21.
 */

public class StateModel {
    private String Name;
    private List<CityModel> CityList;

    public StateModel(){
        super();
    }

    public StateModel(String Name, List<CityModel> CityList){
        super();
        this.Name = Name;
        this.CityList = CityList;
    }

    public String getName(){
        return Name;
    }

    public void setName(String Name){
        this.Name = Name;
    }

    public List<CityModel> getCityList(){
        return CityList;
    }

    public void setCityList(List<CityModel> CityList){
        this.CityList = CityList;
    }

    @Override
    public String toString(){
        return "RegionModel [name=" + Name + ", CityList=" + CityList + "]";
    }

}
