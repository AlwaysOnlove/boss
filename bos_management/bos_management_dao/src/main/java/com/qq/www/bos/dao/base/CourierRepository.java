package com.qq.www.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qq.www.bos.domain.base.Courier;

/**  
 * ClassName:CourierRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月15日 上午12:47:19 <br/>       
 */
//使用JpaSpecificationExecutor这个接口不能单独使用   必须和JpaRepository这个接口一起使用
public interface CourierRepository extends JpaRepository<Courier, Long>,JpaSpecificationExecutor<Courier>{
    
  //因为这是一个更新类的操作
    @Modifying
    @Query("update Courier set deltag = 1 where id = ?")
    void updateDelTagById(Long id);
}
  
