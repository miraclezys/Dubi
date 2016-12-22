package cc.wo_mo.dubi.Model;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shushu on 2016/12/21.
 */

public class XmlParserHandler extends DefaultHandler{
    private List<StateModel> StateList = new ArrayList<StateModel>();

    StateModel StateModel = new StateModel();
    CityModel CityModel = new CityModel();
    RegionModel RegionModel = new RegionModel();

    public XmlParserHandler(){

    };

    public  List<StateModel> getDataList(){
        return StateList;
    }

    // 当读取第一个开始标签时，会触发这个方法
    @Override
    public void startDocument() throws SAXException{

    }

    // 当遇到开始标志时，调用这个方法
    @Override
    public  void startElement(String uri, String localName, String qName,
                              Attributes arrtibutes)throws  SAXException{
        if(qName.equals("State")){
            StateModel = new StateModel();
            StateModel.setName(arrtibutes.getValue(0));
            StateModel.setCityList(new ArrayList<CityModel>());
        }
        else if(qName.equals("City")){
            CityModel = new CityModel();
            CityModel.setName(arrtibutes.getValue(0));
            CityModel.setRegionList(new ArrayList<RegionModel>());
        }
        else if(qName.equals("Region")){
            RegionModel = new RegionModel();
            RegionModel.setName(arrtibutes.getValue(0));
            RegionModel.setZipCode(arrtibutes.getValue(1));
        }
    }

    // 当遇到结束标志时，调用这个方法
    @Override
    public void endElement(String uri, String localName, String qName)
                            throws SAXException{
       // System.out.println("!!! "+qName);
        if(qName.equals("Region")){
            CityModel.getRegionList().add(RegionModel);
        }
        else if(qName.equals("City")){
            StateModel.getCityList().add(CityModel);
        }
        else if(qName.equals("State")){
            StateList.add(StateModel);
        }
    }

    @Override
    public void characters (char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        String s = new String(ch, start, length);

    }
}
