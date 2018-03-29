package com.qq.www.crm.dao;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qq.www.crm.domain.Customer;

/**  
 * ClassName:CustomerRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月22日 下午4:59:42 <br/>       
 */
public interface CustomerRepository extends JpaRepository<Customer, Long>{

    //查询未关联定区客户
    List<Customer> findByFixedAreaIdIsNull();
    
    //查询已关联到指定定区的客户
    List<Customer> findByFixedAreaId(String fixedAreaId);
    
    //把关联到指定定区的客户进行解绑操作
    @Query("update Customer set fixedAreaId = null where fixedAreaId = ?")
    @Modifying
    void unbindCustomerByFixedArea(String fixedAreaId);
    
    //把客户绑定到指定的定区
    @Query("update Customer set fixedAreaId = ?2 where id = ?1")
    @Modifying
    void bindCustomerByFixedArea(Long customerId,String fixedAreaId);

    
    Customer findByTelephone(String telephone);

    Customer findByTelephoneAndPassword(String telephone, String password);
    
}
  
