package com.qq.www.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qq.www.bos.dao.base.AreaRepository;
import com.qq.www.bos.domain.base.Area;
import com.qq.www.bos.service.base.AreaService;

/**  
 * ClassName:AreaServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月15日 下午10:01:22 <br/>       
 */
@Service
@Transactional
public class AreaServiceImpl implements AreaService{

    @Autowired
    private AreaRepository areaRepository;

    @Override
    public void save(List<Area> list) {
          areaRepository.save(list);
    }

    @Override
    public Page<Area> findAll(Pageable pageable) {
      
        return   areaRepository.findAll(pageable);
    }

    @Override
    public List<Area> findByQ(String q) {
          q ="%"+q.toUpperCase()+"%";
          //这里不能写findByQ  会误认为Q是属性名
        return areaRepository.findQ(q);
    }

}
  
