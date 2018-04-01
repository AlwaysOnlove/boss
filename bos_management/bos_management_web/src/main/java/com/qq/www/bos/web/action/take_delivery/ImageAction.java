package com.qq.www.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONObject;

/**  
 * ClassName:ImageAction <br/>  
 * Function:  <br/>  
 * Date:     2018年4月1日 上午3:01:42 <br/>       
 */
//这里不需要进行模型封装 所以只需继承actionSupport
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class ImageAction extends ActionSupport{

    //使用属性驱动获取上传文件的路径
    private File imgFile;
    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }
    
    private String imgFileFileName;
    public void setImgFileFileName(String imgFileFileName) {
        this.imgFileFileName = imgFileFileName;
    }
    
    @Action("imageAction_upload")
    public String upload() throws IOException{
        //因为后面保存后会报错  一定得反馈是否保存成功或者失败
        Map<String , Object> map = new HashMap<>();
        try {
            String dirPath = "/upload";
            ServletContext servletContext = ServletActionContext.getServletContext();
            String realPath = servletContext.getRealPath(dirPath);//获取绝对路径
            System.out.println(imgFileFileName);
            String subName = imgFileFileName.substring(imgFileFileName.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString().replace("-", "")+subName;
            
            //使用uuid以免用户使用相同的文件名
            File destFile = new File(realPath+"/"+fileName);
            FileUtils.copyFile(imgFile, destFile );//前面参数是上传路径 后面参数是保存路径(使用绝对路径)
            
            String contextPath = servletContext.getContextPath();
            
            map.put("error", 0);
            map.put("url", contextPath+"/upload/"+fileName);
        } catch (IOException e) {
              
            // TODO Auto-generated catch block  
            e.printStackTrace();  
            map.put("error", 1);
            map.put("message", e.getMessage());
        }
        
        String  json = JSONObject.fromObject(map).toString();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
        return NONE;
    }
}
  
