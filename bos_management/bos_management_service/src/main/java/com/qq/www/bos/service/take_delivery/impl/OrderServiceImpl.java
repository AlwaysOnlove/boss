package com.qq.www.bos.service.take_delivery.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qq.www.bos.dao.base.AreaRepository;
import com.qq.www.bos.dao.take_delivery.OrderRepository;
import com.qq.www.bos.domain.base.Area;
import com.qq.www.bos.domain.take_delivery.Order;
import com.qq.www.bos.service.take_delivery.OrderService;

/**  
 * ClassName:OrderServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月29日 下午5:54:40 <br/>       
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;
    
    //因为用户order 里面的area是new出来的 是瞬时态  要通过用户写的order(关于省市区area的信息)从数据库里面查询area(持久态信息) 然后在保存在order里面
    @Autowired
    private AreaRepository areaRepository;
    
    @Override
    public void saveOrder(Order order) {
        Area sendArea = order.getSendArea();
        if(sendArea!=null){
            Area sendAreaDB = areaRepository.findByProvinceAndCityAndDistrict(sendArea.getProvince(), sendArea.getCity(), sendArea.getDistrict());
            order.setSendArea(sendAreaDB);
        }
        
        Area recArea = order.getRecArea();
        if(recArea!=null){
            Area recAreaDB = areaRepository.findByProvinceAndCityAndDistrict(recArea.getProvince(), recArea.getCity(), recArea.getDistrict());
            order.setRecArea(recAreaDB);
        }
        
         orderRepository.save(order);
    }

}
  
