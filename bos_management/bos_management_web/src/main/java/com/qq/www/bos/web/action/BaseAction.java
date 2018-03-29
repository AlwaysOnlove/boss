package com.qq.www.bos.web.action;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**  
 * ClassName:CommonAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午2:56:38 <br/>       
 */
//CommonAction这种方法是因为类的泛型只有一个  BaseAction这个方法泛型是个数组(以后遇到了多个泛型 可以使用这种方法)
public class BaseAction<T> extends ActionSupport implements ModelDriven<T>{

    private T model;
    
    @Override
    public T getModel() {
        //子类调用这个方法的时候得到的就是子类的字节码对象
        Class<? extends BaseAction> childClazz = this.getClass();
        //获取到的是父类的字节码对象
        //clazz.getSuperclass();
        //获取到的是父类字节码文件对象(含有泛型)
        Type genericSuperclass = childClazz.getGenericSuperclass();
        //进行强转
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        //获取泛型的数组
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        //泛型只有一个 所以得到的类型之后进行强转
        Class<T> clazz = (Class<T>) actualTypeArguments[0];
        try {
            model = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();  
        }
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
}
  
