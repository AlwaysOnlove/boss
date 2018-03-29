package com.qq.www.bos.service.base;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qq.www.bos.domain.base.Area;

/**  
 * ClassName:AreaService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月15日 下午10:00:58 <br/>       
 */
public interface AreaService {

    void save(List<Area> list);

    Page<Area> findAll(Pageable pageable);

    List<Area> findByQ(String q);

}
  
