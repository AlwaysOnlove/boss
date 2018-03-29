package com.qq.www.bos.web.action.base;

import java.io.IOException;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.qq.www.bos.domain.base.TakeTime;
import com.qq.www.bos.service.base.TakeTimeService;
import com.qq.www.bos.web.action.CommonAction;

/**  
 * ClassName:TakeTimeAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月24日 下午10:10:45 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class TakeTimeAction extends CommonAction<TakeTime>{

    public TakeTimeAction() {
        super(TakeTime.class);  
    }

    @Autowired
    private TakeTimeService takeTimeService;
    
    @Action("takeTimeAction_listajax")
    public String findAll() throws IOException{
        List<TakeTime> content = takeTimeService.findAll();
        list2json(content, null);
        return NONE;
    }
}
  
