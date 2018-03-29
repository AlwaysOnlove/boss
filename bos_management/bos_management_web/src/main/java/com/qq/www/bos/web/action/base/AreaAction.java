package com.qq.www.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.qq.www.bos.domain.base.Area;
import com.qq.www.bos.service.base.AreaService;
import com.qq.www.bos.web.action.CommonAction;
import com.qq.www.utils.PinYin4jUtils;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;


/**  
 * ClassName:AreaAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月15日 下午10:01:54 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class AreaAction extends CommonAction<Area>{
    
    //自动生成的方法是有参方法(但是创建函数的时候需要的是无参构造方法)
    //通过字节码在父类中可以把对象进行实例化 
    public AreaAction() {
        super(Area.class);  
    }

    
    @Autowired
    private AreaService areaService;
    
    //因为从页面上面获取到的是file文件 模型驱动不能进行封装获取  所以使用属性驱动
    private File file;
    
    //用户搜索内容为q
    private String q;
    
    public void setQ(String q) {
        this.q = q;
    }
    
    //为什么使用set方法
    public void setFile(File file) {
        this.file = file;
    }
    
    @Action( value="areaAction_importXLS",results={@Result(name="success",location="/pages/base/area.html",type="redirect")}) 
    public String importXLS(){
        HSSFWorkbook hssfWorkbook;
        List<Area> list = new ArrayList<>();
        try {
            hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
            //读取第一个工作簿
            HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
            
            for (Row row : sheet) {
                //排除第一行的数据
                if(row.getRowNum()==0){
                    continue;
                }
                
                String province = row.getCell(1).getStringCellValue();
                String city = row.getCell(2).getStringCellValue();
                String district = row.getCell(3).getStringCellValue();
                String postcode = row.getCell(4).getStringCellValue();
                
                province = province.substring(0, province.length()-1);
                city = city.substring(0, city.length()-1);
                district = district.substring(0,district.length()-1);
                
                String citycode = PinYin4jUtils.hanziToPinyin(city, "").toUpperCase();//城市编码
                String[] headByString = PinYin4jUtils.getHeadByString(province+city+district);//简码
                String shortcode = PinYin4jUtils.stringArrayToString(headByString);//将数组转换为string类型
                
                Area area = new Area();
                
                area.setCity(city);
                area.setCitycode(citycode);
                area.setDistrict(district);
                area.setPostcode(postcode);
                area.setProvince(province);
                area.setShortcode(shortcode);
                //将获取到的area对象保存到service层处理  
                //service开启了事务 每一个城市的数据都要开启一次和关闭一次事务 占用服务器
                //前面save方法既可以保存对象又可以保存集合 所以将area放在集合里面一起执行这个方法
                list.add(area);
            }
            
            areaService.save(list);
            //释放资源
            hssfWorkbook.close();
        } catch(Exception e) {
            e.printStackTrace();  
        } 
       
        return SUCCESS;
    }

    //areaAction_pageQuery
    
    
    
    
    @Action(value="areaAction_pageQuery")
    //value相当于struts.xml中action节点的name属性值  多个结果就使用results里面的name是结果的返回值  location是跳转路径  type是跳方式(重定向和请求转发)
    public String pageQuery() throws IOException{
        //因为EasyUI是从1开始查询  而SpringDataJPA是从0开始  所以page要-1
        Pageable pageable = new PageRequest(page-1, rows);
        Page<Area> page = areaService.findAll(pageable);
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"subareas"});
        
        page2json(page, jsonConfig);
        return NONE;
    }
    
    @Action(value="areaAction_findAll")
    public String findAll() throws IOException{
        
        List<Area> list;
        if(StringUtils.isNotEmpty(q)){
            list = areaService.findByQ(q);
        }else{
            Page<Area> page = areaService.findAll(null);
            list = page.getContent();
        }
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"subareas"});
        list2json(list, jsonConfig);
        return NONE;
    }
    
    
}
  
