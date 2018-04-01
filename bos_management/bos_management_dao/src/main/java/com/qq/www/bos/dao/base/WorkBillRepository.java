package com.qq.www.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qq.www.bos.domain.take_delivery.WorkBill;

/**  
 * ClassName:WorkBillRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月30日 下午2:59:26 <br/>       
 */
public interface WorkBillRepository extends JpaRepository<WorkBill, Long>{
    
}
  
