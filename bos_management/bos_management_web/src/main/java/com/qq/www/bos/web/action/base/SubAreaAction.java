package com.qq.www.bos.web.action.base;

import java.io.IOException;
import java.util.List;

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
import com.qq.www.bos.domain.base.SubArea;
import com.qq.www.bos.service.base.SubAreaService;
import com.qq.www.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**  
 * ClassName:SubAreaAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月19日 下午3:20:34 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class SubAreaAction extends CommonAction<SubArea>{

    public SubAreaAction() {
        super(SubArea.class);  
    }

    @Autowired
    private SubAreaService subAreaService;
    
    @Action(value="subareaAction_save",results={@Result(name="success",location="/pages/base/sub_area.html",type="redirect")})
    public String save(){
        subAreaService.save(getModel());
        return SUCCESS;
    }

    
    @Action("subAction_pageQuery")
    public String pageQuery() throws IOException{
        Pageable pageable = new PageRequest(page-1, rows);
        Page<SubArea>  page = subAreaService.findAll(pageable);
        //因为subArea在序列化的时候调用了Area但是这里面Area里面的有subArea属性  所以嵌套查询导致懒加载
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"subareas","couriers"});
        page2json(page, jsonConfig);
        return NONE;
    }
    
    //查询未关联定区的分区
    @Action("subAreaAction_findUnAssociatedSubAreas")
    public String findUnAssociatedSubAreas() throws IOException{
        List<SubArea> list = subAreaService.findUnAssociatedSubAreas();
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"subareas"});
        list2json(list, jsonConfig);
         return NONE;
    }
    
    //查询已关联指定定区的分区
    @Action("subAreaAction_findAssociatedSubAreas")
    public String findAssociatedSubAreas() throws IOException{
        List<SubArea> list = subAreaService.findAssociatedSubAreas(getModel().getId());
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"subareas","couriers"});
        list2json(list, jsonConfig);
        return NONE;
    }
}
  
