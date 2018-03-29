package com.qq.www.bos.web.action.base;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.qq.www.bos.domain.base.Courier;
import com.qq.www.bos.domain.base.Standard;
import com.qq.www.bos.service.base.CourierService;
import com.qq.www.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**  
 * ClassName:CourierAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月15日 上午12:32:57 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class CourierAction extends CommonAction<Courier>{

    
    public CourierAction() {
        super(Courier.class);  
    }

    @Autowired
    private CourierService courierService;
    
    @Action(value="courierAction_save",results={
            @Result(name="success" ,location="/pages/base/courier.html",type="redirect")
    })
    public String save(){
        courierService.save(getModel());
        return SUCCESS;
    }
    
    @Action("courierAction_pageQuery")
    public String pageQuery() throws IOException{
        
        //specification用来分页查询 (需要的参数)
        Specification<Courier> specification = new Specification<Courier>() {

            //root:根对象简单理解成泛型的对象     cb:构建查询条件的对象
            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                //查询的请求数据(都被封装到了模型驱动里面 )
                String company = getModel().getCompany();
                String courierNum = getModel().getCourierNum();
                String type = getModel().getType();
                Standard standard = getModel().getStandard();
                
                //因为后面根据数据进行查询操作 将这些数据放到list集合中
                ArrayList<Predicate> list = new ArrayList<>();
                if(StringUtils.isNotEmpty(courierNum)){
                    Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courierNum);
                    list.add(p1);
                }
                
                if(StringUtils.isNotEmpty(company)){
                    Predicate p2 = cb.equal(root.get("company").as(String.class), company);
                    list.add(p2);
                }
                
                if(StringUtils.isNotEmpty(type)){
                    Predicate p3 = cb.equal(root.get("type").as(String.class), type);
                    list.add(p3);
                }
                
                //根据用户的名字进行查询(要想判断名字是否为空 首先判断standard对象是否为null)
                if(standard!=null){
                    String name = standard.getName();
                    if(StringUtils.isNotEmpty(name)){
                        //连表查询(可以理解为将standard这个表加入到了courier这张表中) 得到的返回值(join)可以理解为standard对象
                        Join<Object, Object> join = root.join("standard");
                        Predicate p4 = cb.equal(join.get("name").as(String.class), name);
                        list.add(p4);
                    }
                }
                
                //以上所有都是判断用户填写的内容  得到的list集合判断用户是否有填写内容
                if(list.size()==0){
                    return null;
                }
                
                //将list集合导入为predicate数组(用户填写的每一条查询条件)
                Predicate[] arr = new Predicate[list.size()]; 
                //and方法理解为查询的结果满足用户查询的每一个条件要求
                
                //将list集合中的数据传给数组
                list.toArray(arr);
                return cb.and(arr);
            }};
        
        
        Pageable pageable = new PageRequest(page-1, rows);
        Page<Courier> page = courierService.findAll(specification,pageable);
        
        
        
        //实际开发中为了提高服务器性能在前台页面不需要的数据上都应该忽略掉
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"fixedAreas","takeTime"});
        page2json(page, jsonConfig);
        return NONE;
    }
    
    private String ids;
    public void setIds(String ids) {
        this.ids = ids;
    }
    
    
    //批量删除
    @Action(value="courierAction_batchDel",results={
            @Result(name="success" ,location="/pages/base/courier.html",type="redirect")
    })
    public String batchDel(){
        courierService.batchDel(ids);
        return SUCCESS;
    }
    
    //查询所有的在职快递员
    @Action("courierAction_listajax")
    public String listajax() throws IOException{
        Specification<Courier> specification = new Specification<Courier>() {

            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                  Predicate predicate = cb.isNull(root.get("deltag").as(Character.class));
                return predicate;
               }
           };
        //findAll方法属于分页查询
        Page<Courier> page = courierService.findAll(specification , null);
        List<Courier> list = page.getContent();
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"fixedAreas","takeTime"});
        list2json(list, jsonConfig);
        return NONE;
    }
    
    
    
    
}
  
