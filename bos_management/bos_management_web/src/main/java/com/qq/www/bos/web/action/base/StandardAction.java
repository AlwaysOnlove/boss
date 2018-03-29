package com.qq.www.bos.web.action.base;
//struts-plugin 插件jar包中 申明包名必须要有action,actions,struts,struts2四个中的其中任何一种

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
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

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.qq.www.bos.domain.base.Standard;
import com.qq.www.bos.service.base.StandardService;
import com.qq.www.bos.web.action.CommonAction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**  
 * ClassName:StandardAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月14日 下午5:32:33 <br/>
 * 使用注解的方式实现和配置文件相同的效果
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller//这是spring的注解  相当于spring配置文件的效果
@Scope("prototype")//多例
public class StandardAction extends CommonAction<Standard>{
    
    public StandardAction() {
          
        super(Standard.class);  
        
    }

    @Autowired
    private StandardService standardService;
    
    @Action(value="standardAction_save",results={@Result(name="success",location="/pages/base/standard.html",type="redirect")})
    //value相当于struts.xml中action节点的name属性值  多个结果就使用results里面的name是结果的返回值  location是跳转路径  type是跳方式(重定向和请求转发)
    public String save(){
        standardService.save(getModel());
        return SUCCESS;
    }
    
    
    @Action(value="standardAction_pageQuery")
    //value相当于struts.xml中action节点的name属性值  多个结果就使用results里面的name是结果的返回值  location是跳转路径  type是跳方式(重定向和请求转发)
    public String pageQuery() throws IOException{
        //因为EasyUI是从1开始查询  而SpringDataJPA是从0开始  所以page要-1
        Pageable pageable = new PageRequest(page-1, rows);
        Page<Standard> page = standardService.findAll(pageable);
        page2json(page, null);
        return NONE;
    }
    
    
    @Action(value="standard_findAll")
    public String findAll() throws IOException{
        Page<Standard> page = standardService.findAll(null);
        List<Standard> list = page.getContent();
        String json = JSONArray.fromObject(list).toString();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
        return NONE;
    }
}
  
