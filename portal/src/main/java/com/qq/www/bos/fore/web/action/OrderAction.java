package com.qq.www.bos.fore.web.action;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.qq.www.bos.domain.base.Area;
import com.qq.www.bos.domain.take_delivery.Order;

/**  
 * ClassName:OrderAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月29日 下午4:20:48 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class OrderAction extends ActionSupport implements ModelDriven<Order>{

    private Order model = new Order();
    
    @Override
    public Order getModel() {
          
        return model;
    }

    //因为寄件人地址和收件人地址 的属性不能在模型驱动里面进行封装  所以单独使用属性驱动进行封装
    private String sendAreaInfo;
    private String recAreaInfo;
    
    public void setSendAreaInfo(String sendAreaInfo) {
        this.sendAreaInfo = sendAreaInfo;
    }
    
    public void setRecAreaInfo(String recAreaInfo) {
        this.recAreaInfo = recAreaInfo;
    }
    
    @Action(value="orderAction_add",results={@Result(name="success",location="/index.html",type="redirect")})
    public String saveOrder(){
        if(StringUtils.isNotEmpty(sendAreaInfo)){
            //将寄件人地址进行分割   得到的是省市区
            String[] split = sendAreaInfo.split("/");
            String province = split[0];
            String city = split[1];
            String district = split[2];
            //得到的里面还有关键字省市区 所以进行截取
            province = province.substring(0, province.length()-1);
            city = city.substring(0, city.length()-1);
            district = district.substring(0, district.length()-1);
            //将省市区封装到area里面
            Area area = new Area();
            area.setProvince(province);
            area.setCity(city);
            area.setDistrict(district);
            //将area封装到order里面
            model.setSendArea(area);
        }
        if(StringUtils.isNotEmpty(recAreaInfo)){
            //将寄件人地址进行分割   得到的是省市区
            String[] split = recAreaInfo.split("/");
            String province = split[0];
            String city = split[1];
            String district = split[2];
            //得到的里面还有关键字省市区 所以进行截取
            province = province.substring(0, province.length()-1);
            city = city.substring(0, city.length()-1);
            district = district.substring(0, district.length()-1);
            //将省市区封装到area里面
            Area area = new Area();
            area.setProvince(province);
            area.setCity(city);
            area.setDistrict(district);
            //将area封装到order里面
            model.setRecArea(area);
        }
        
        WebClient.create("http://localhost:8080/bos_management_web/webService/orderService/saveOrder")
            .accept(MediaType.APPLICATION_JSON)
            .type(MediaType.APPLICATION_JSON)
            .post(model);
        return SUCCESS;
    }
}
  
