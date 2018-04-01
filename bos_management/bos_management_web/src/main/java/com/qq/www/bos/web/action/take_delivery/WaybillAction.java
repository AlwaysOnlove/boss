package com.qq.www.bos.web.action.take_delivery;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.qq.www.bos.domain.take_delivery.WayBill;
import com.qq.www.bos.service.take_delivery.WaybillService;
import com.qq.www.bos.web.action.CommonAction;

/**  
 * ClassName:WaybillAction <br/>  
 * Function:  <br/>  
 * Date:     2018年4月1日 上午1:03:21 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class WaybillAction extends CommonAction<WayBill>{

    public WaybillAction() {
          
        super(WayBill.class);  
        
    }

    @Autowired
    private WaybillService waybillService;
    
    @Action(value="waybillAction_save")
    public String save() throws IOException{
        String msg = "0";
        try {
            waybillService.save(getModel());
        } catch (Exception e) {
              msg="1";
            e.printStackTrace();  
        }
        //如果存储失败了 回显给用户
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(msg);
        return NONE;
    }
    
}
  
