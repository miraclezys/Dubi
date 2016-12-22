package cc.wo_mo.dubi.Model;

import java.util.List;

/**
 * Created by shushu on 2016/12/21.
 */

public class CityModel {
    private String Name;
    private List<RegionModel> RegionList;

    public CityModel(){
        super();
    }

    public CityModel(String Name, List<RegionModel> RegionList){
        this.Name = Name;
        this.RegionList = RegionList;
    }

    public String getName(){
        return Name;
    }

    public void setName(String Name){
        this.Name = Name;
    }

    public List<RegionModel> getRegionList(){
        return RegionList;
    }

    public void setRegionList(List<RegionModel> RegionList){
        this.RegionList = RegionList;
    }

    @Override
    public String toString(){
        return "RegionModel [name=" + Name + ", ReginList=" + RegionList + "]";
    }

}
