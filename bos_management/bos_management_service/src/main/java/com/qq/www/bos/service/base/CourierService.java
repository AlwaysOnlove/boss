package com.qq.www.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.qq.www.bos.domain.base.Courier;

/**  
 * ClassName:CourierService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月15日 上午12:44:04 <br/>       
 */
public interface CourierService {

    void save(Courier courier);

    Page<Courier> findAll(Pageable pageable);

    void batchDel(String ids);

    Page<Courier> findAll(Specification<Courier> specification, Pageable pageable);

}
  
