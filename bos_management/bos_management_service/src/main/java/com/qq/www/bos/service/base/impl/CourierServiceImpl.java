package com.qq.www.bos.service.base.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qq.www.bos.dao.base.CourierRepository;
import com.qq.www.bos.domain.base.Courier;
import com.qq.www.bos.service.base.CourierService;

/**  
 * ClassName:CourierServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月15日 上午12:44:29 <br/>       
 */
@Service
@Transactional
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierRepository courierRepository;

    @Override
    public void save(Courier courier) {
          courierRepository.save(courier);
        
    }

    @Override
    public Page<Courier> findAll(Pageable pageable) {
        
        return courierRepository.findAll(pageable);
    }

    @Override
    public void batchDel(String ids) {
          //真实开发中只有逻辑删除  不能使用delete方法(这是物理删除磁盘里面的数据都会删除掉)
        if(StringUtils.isNotEmpty(ids)){
            String[] list = ids.split(",");
            for (String id : list) {
                courierRepository.updateDelTagById(Long.parseLong(id));
            }
        }
    }

    @Override
    public Page<Courier> findAll(Specification<Courier> specification, Pageable pageable) {
          
        return courierRepository.findAll(specification, pageable);
    }

}
  
