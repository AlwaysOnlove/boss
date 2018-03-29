package com.qq.www.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.qq.www.crm.domain.Customer;

/**  
 * ClassName:CustomerService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月22日 下午5:00:40 <br/>       
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CustomerService {

    @GET
    @Path("/findAll")
    List<Customer> findAll();
    
    //查询未关联定区用户
    @GET
    @Path("/findUnAssociatedCustomers")
    List<Customer> findUnAssociatedCustomers();
    
    //查询已关联的用户
    @GET
    @Path("/findAssociatedCustomers")
    List<Customer> findAssociatedCustomers(@QueryParam("fixedAreaId") String fixedAreaId);
    
    //定区ID,要关联的数据
    //根据定区ID,把关联到这个定区的所有客户全部解绑
    //要关联的数据和定区Id进行绑定
    @PUT
    @Path("/assignCustomers2FixedArea")
    void assignCustomers2FixedArea(@QueryParam("customerIds") Long[] customerIds,@QueryParam("fixedAreaId") String fixedAreaId);
    
    @POST
    @Path("/save")
    void save(Customer customer);
    
    @GET
    @Path("/isActived")
    Customer isActived(@QueryParam("telephone") String telephone);
    
    @GET
    @Path("/login")
    Customer login(@QueryParam("telephone") String telephone,@QueryParam("password") String password);
}
  
