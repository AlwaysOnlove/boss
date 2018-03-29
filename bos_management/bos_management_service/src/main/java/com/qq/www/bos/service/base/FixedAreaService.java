package com.qq.www.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qq.www.bos.domain.base.FixedArea;

/**  
 * ClassName:FixedAreaService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月22日 下午8:52:49 <br/>       
 */
public interface FixedAreaService {

    void save(FixedArea model);

    Page<FixedArea> findAll(Pageable pageable);

    void associationCourierToFixdArea(Long fixedAreaId, Long courierId, Long takeTimeId);

    void assignSubAreas2FixedArea(Long id, Long[] subAreaIds);

}
  
