package com.qq.www.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qq.www.bos.dao.base.TakeTimeRepository;
import com.qq.www.bos.domain.base.TakeTime;
import com.qq.www.bos.service.base.TakeTimeService;

/**  
 * ClassName:TakeTimeServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月24日 下午10:09:39 <br/>       
 */
@Transactional
@Service
public class TakeTimeServiceImpl implements TakeTimeService{

    @Autowired
    private TakeTimeRepository takeTimeRepository;
    
    @Override
    public List<TakeTime> findAll() {
          
        return takeTimeRepository.findAll();
    }

}
  
