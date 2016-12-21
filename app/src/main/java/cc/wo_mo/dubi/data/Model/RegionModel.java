package cc.wo_mo.dubi.data.Model;

/**
 * Created by shushu on 2016/12/21.
 */

public class RegionModel {
    private String Name;
    private String ZipCode;

    public RegionModel(){
        super();
    }

    public RegionModel(String Name, String ZipCode){
        super();
        this.Name = Name;
        this.ZipCode = ZipCode;
    }

    public String getName(){
        return Name;
    }

    public void setName(String Name){
        this.Name = Name;
    }

    public String getZipCode(){
        return ZipCode;
    }

    public void setZipCode(String ZipCode){
        this.ZipCode = ZipCode;
    }

    @Override
    public String toString(){
        return "RegionModel [name=" + Name + ", ZipCode=" + ZipCode + "]";
    }

}
