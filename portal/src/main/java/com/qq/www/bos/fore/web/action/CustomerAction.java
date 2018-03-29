package com.qq.www.bos.fore.web.action;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import com.aliyuncs.exceptions.ClientException;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.qq.www.crm.domain.Customer;
import com.qq.www.utils.MailUtils;
import com.qq.www.utils.SmsUtils;

/**  
 * ClassName:CustomerAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月25日 下午10:35:15 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class CustomerAction extends ActionSupport implements ModelDriven<Customer>{

    private Customer model = new Customer();

    @Override
    public Customer getModel() {
          
        return model;
    }
    
    @Action("customerAction_sendSMS")
    public String sendSMS(){
        String code = RandomStringUtils.randomNumeric(6);
        System.out.println(code);
        ServletActionContext.getRequest().getSession().setAttribute("serverCode", code);
        try {
            SmsUtils.sendSms(model.getTelephone(), code);
        } catch (ClientException e) {
            e.printStackTrace();  
        }
        return NONE;
    }
    
    //根据属性驱动获取前端的属性
    private String checkcode;
    
    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }
    
    //使用redis进行保存数据
    @Autowired
    private RedisTemplate<String , String > redisTemplate;
    
    @Action(value="customerAction_regist",results={@Result(name="success",location="/signup-success.html",type="redirect"),@Result(name="error",location="/signup-fail.html",type="redirect")})
    public String regist(){
        //校验验证码(注册页面要校验电话号码,验证码,注册密码和登录密码是否一致,邮件地址等等)
        //比较服务器存在session里面的验证码是否和用户输入的验证码一致
        String serverCode = (String) ServletActionContext.getRequest().getSession().getAttribute("serverCode");
        
        if(StringUtils.isNotEmpty(serverCode)&&StringUtils.isNotEmpty(checkcode)&&checkcode.equals(serverCode)){
            //两个验证码是相同的时候进行注册保存的工作
            WebClient.create("http://localhost:8180/crm/webService/customerService/save")
            .accept(MediaType.APPLICATION_JSON)
            .type(MediaType.APPLICATION_JSON)
            .post(model);
            
            //生成验证码
            String activeCode = RandomStringUtils.randomNumeric(32);
            //通过redis将验证码保存起来(时间限制)
            redisTemplate.opsForValue().set(model.getTelephone(), activeCode, 1,TimeUnit.DAYS);
            
            String emailBody = "请在24小时内点击<a href='http://localhost:8280/portal/customerAction_active.action?activeCode="+activeCode+"&telephone="+model.getTelephone()+"'>本链接</a>进行激活账号";
            MailUtils.sendMail(emailBody, "激活邮件",model.getEmail());
            return SUCCESS;
        }
        return ERROR;
    }
    
    
    
    // 使用属性驱动获取激活码
    private String activeCode;

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    @Action(value = "customerAction_active",
            results = {
                    @Result(name = "success", location = "/login.html",
                            type = "redirect"),
                    @Result(name = "error", location = "/signup-fail.html",
                            type = "redirect")})
    public String active() {

        // 比对激活码
        String serverCode =
                redisTemplate.opsForValue().get(model.getTelephone());

        if (StringUtils.isNotEmpty(serverCode)
                && StringUtils.isNotEmpty(activeCode)
                && serverCode.equals(activeCode)) {
            // 判断用户是否已经激活
            // 激活
            WebClient.create(
                    "http://localhost:8180/crm/webService/customerService/active")
                    .type(MediaType.APPLICATION_JSON)
                    .query("telephone", model.getTelephone())
                    .accept(MediaType.APPLICATION_JSON).put(null);

            return SUCCESS;
        }

        return ERROR;

    }
    
    
    
    @Action(value="customerAction_login",results={@Result(name="success",location="/index.html",type="redirect"),@Result(name="error",location="/login.html",type="redirect")})
    public String login(){
        String serverCode = (String) ServletActionContext.getRequest().getSession().getAttribute("validateCode");
        //首先判断验证码是否为空
        if(StringUtils.isNotEmpty(serverCode)&&StringUtils.isNotEmpty(checkcode)&&serverCode.equals(checkcode)){
            //根据电话验证用户是否激活
            Customer customer1 = WebClient
            .create("http://localhost:8180/crm/webService/customerService/isActived")
            .accept(MediaType.APPLICATION_JSON)
            .type(MediaType.APPLICATION_JSON)
            .query("telephone", model.getTelephone())
            .get(Customer.class);
            
            //为什么不直接进行判断是否等于1  因为会报空指针异常(integer和int区别)
            if(customer1!=null&&customer1.getType()!=null){
                if(customer1.getType()==1){
                    //已激活
                    Customer customer2 = WebClient
                    .create("http://localhost:8180/crm/webService/customerService/login")
                    .accept(MediaType.APPLICATION_JSON)
                    .type(MediaType.APPLICATION_JSON)
                    .query("telephone", model.getTelephone())
                    .query("password", model.getPassword())
                    .get(Customer.class);
                    
                    if(customer2!=null){
                        ServletActionContext.getRequest().getSession().setAttribute("user", customer2);
                        return SUCCESS;
                    }else{
                        return ERROR;
                    }
                }else{
                    //用户已经注册成功 但是没有进行激活
                    return "unactived";
                }
            }
        }
        return ERROR;
    }
}
  
