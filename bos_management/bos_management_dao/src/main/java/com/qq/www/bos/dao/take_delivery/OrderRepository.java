package com.qq.www.bos.dao.take_delivery;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qq.www.bos.domain.take_delivery.Order;

/**  
 * ClassName:OrderRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月29日 下午5:52:39 <br/>       
 */
public interface OrderRepository extends JpaRepository<Order, Long>{

    
}
  
