package com.qq.www.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qq.www.bos.domain.base.Area;
import com.qq.www.bos.domain.base.FixedArea;
import com.qq.www.bos.domain.base.SubArea;

/**  
 * ClassName:SubAreaRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月19日 下午3:17:28 <br/>       
 */
public interface SubAreaRepository extends JpaRepository<SubArea, Long>{

    List<SubArea> findByFixedAreaIsNull();
    
    List<SubArea> findByFixedArea(FixedArea fixedArea);
}
  
