package com.qq.www.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qq.www.bos.dao.base.SubAreaRepository;
import com.qq.www.bos.domain.base.Area;
import com.qq.www.bos.domain.base.FixedArea;
import com.qq.www.bos.domain.base.SubArea;
import com.qq.www.bos.service.base.SubAreaService;

/**  
 * ClassName:SubAreaServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月19日 下午3:20:10 <br/>       
 */
@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService {

    @Autowired
    private SubAreaRepository subAreaRepository;

    @Override
    public void save(SubArea model) {
        subAreaRepository.save(model); 
    }

    @Override
    public Page<SubArea> findAll(Pageable pageable) {
          
        return subAreaRepository.findAll(pageable);
    }

    @Override
    public List<SubArea> findUnAssociatedSubAreas() {
          
        return subAreaRepository.findByFixedAreaIsNull();
    }

    @Override
    public List<SubArea> findAssociatedSubAreas(Long id) {
        FixedArea fixedArea = new FixedArea();
        fixedArea.setId(id);
        return subAreaRepository.findByFixedArea(fixedArea);
    }
    

}
  
