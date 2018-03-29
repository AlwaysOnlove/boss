package com.qq.www.bos.service.base.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qq.www.bos.dao.base.CourierRepository;
import com.qq.www.bos.dao.base.FixedAreaRepository;
import com.qq.www.bos.dao.base.SubAreaRepository;
import com.qq.www.bos.dao.base.TakeTimeRepository;
import com.qq.www.bos.domain.base.Courier;
import com.qq.www.bos.domain.base.FixedArea;
import com.qq.www.bos.domain.base.SubArea;
import com.qq.www.bos.domain.base.TakeTime;
import com.qq.www.bos.service.base.FixedAreaService;

/**  
 * ClassName:FixedAreaServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月22日 下午8:53:20 <br/>       
 */
@Transactional
@Service
public class FixedAreaServiceImpl implements FixedAreaService {

    @Autowired
    private FixedAreaRepository fixedAreaRepository;

    @Override
    public void save(FixedArea model) {
          
        fixedAreaRepository.save(model);
    }

    @Override
    public Page<FixedArea> findAll(Pageable pageable) {
          
        return fixedAreaRepository.findAll(pageable);
    }

    @Autowired
    private CourierRepository courierRepository;
    
    @Autowired
    private TakeTimeRepository takeTimeRepository;
    
    @Override
    public void associationCourierToFixdArea(Long fixedAreaId, Long courierId, Long takeTimeId) {
          FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
          Courier courier = courierRepository.findOne(courierId);
          TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
          
          //绑定快递员和时间
          courier.setTakeTime(takeTime);
          //绑定快递员和定区
          //因为courier的fixedArea属性使用了mappedBy放弃外键维护 所以是定区绑定快递员
          fixedArea.getCouriers().add(courier);
    }
    
    @Autowired
    private SubAreaRepository subAreaRepository;
    
    //关联分区到指定的定区
    @Override
    public void assignSubAreas2FixedArea(Long id, Long[] subAreaIds) {
        //关系由分区来维护
        //先解绑 把当前定区绑定的所有分区全部解绑
        FixedArea fixedArea = fixedAreaRepository.findOne(id);
        Set<SubArea> subAreas = fixedArea.getSubareas();
        for (SubArea subArea : subAreas) {
            subArea.setFixedArea(null);
        }
        
        //在绑定
        for(Long subAreaId: subAreaIds){
            SubArea subArea = subAreaRepository.findOne(subAreaId);
            subArea.setFixedArea(fixedArea);
        }
    }

}
  
