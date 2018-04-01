package com.qq.www.bos.service.take_delivery.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qq.www.bos.dao.take_delivery.WaybillRepository;
import com.qq.www.bos.domain.take_delivery.WayBill;
import com.qq.www.bos.service.take_delivery.WaybillService;

/**  
 * ClassName:WaybillServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年4月1日 上午12:59:32 <br/>       
 */
@Service
@Transactional
public class WaybillServiceImpl implements WaybillService{

    @Autowired
    private WaybillRepository waybillRepository;

    @Override
    public void save(WayBill model) {
          
        
    }
}
  
