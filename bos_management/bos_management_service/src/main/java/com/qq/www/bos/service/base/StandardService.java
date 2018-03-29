package com.qq.www.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qq.www.bos.domain.base.Standard;

/**  
 * ClassName:StandardService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月14日 下午7:47:40 <br/>       
 */
public interface StandardService {

    void save(Standard standard);

    Page<Standard> findAll(Pageable pageable);

}
  
