package com.qq.www.bos.web.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**  
 * ClassName:CommonAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午2:56:38 <br/>       
 */
public class CommonAction<T> extends ActionSupport implements ModelDriven<T>{

    private T model;
    
    private Class<T> clazz;
    
    //根据字节码创建对象
    public CommonAction(Class<T> clazz){
        this.clazz = clazz;
        try {
            model=clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();  
        }
    }
    
    @Override
    public T getModel() {
          
        return model;
    }
    
    protected int page;
    protected int rows;
    
    public void setPage(int page) {
        this.page = page;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }
    public void page2json(Page<T> page,JsonConfig jsonConfig) throws IOException{
        
        long total = page.getTotalElements();
        List<T> list = page.getContent();
        
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("rows", list);
     
        //JSONArray 将数组和list集合转换成json数据
        //JSONObject 将map集合和对象转换成json数据
        //抽取standardaction这里面抽取的时候  如果不先判断jsonconfig是否为null的情况下直接传入进去会报空指针异常
        String json;
        if(jsonConfig!=null){
             json = JSONObject.fromObject(map,jsonConfig).toString();
        }else{
             json = JSONObject.fromObject(map).toString();
        }
        
        //将数据写到写到页面上面
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
        
    }
    
    public void list2json(List list,JsonConfig jsonConfig) throws IOException{
        String json;
        if(jsonConfig!=null){
            json=JSONArray.fromObject(list,jsonConfig).toString();
        }else{
            json=JSONArray.fromObject(list).toString();
        }
        
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }
}
  
