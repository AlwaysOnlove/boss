package com.qq.www.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qq.www.bos.domain.base.Area;
import com.qq.www.bos.domain.base.SubArea;

/**  
 * ClassName:SubAreaService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月19日 下午3:19:32 <br/>       
 */
public interface SubAreaService {

    void save(SubArea model);

    Page<SubArea> findAll(Pageable pageable);

    List<SubArea> findUnAssociatedSubAreas();

    List<SubArea> findAssociatedSubAreas(Long id);

}
  
