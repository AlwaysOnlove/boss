package com.qq.www.bos.service.take_delivery.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qq.www.bos.dao.base.AreaRepository;
import com.qq.www.bos.dao.base.FixedAreaRepository;
import com.qq.www.bos.dao.base.WorkBillRepository;
import com.qq.www.bos.dao.take_delivery.OrderRepository;
import com.qq.www.bos.domain.base.Area;
import com.qq.www.bos.domain.base.Courier;
import com.qq.www.bos.domain.base.FixedArea;
import com.qq.www.bos.domain.base.SubArea;
import com.qq.www.bos.domain.take_delivery.Order;
import com.qq.www.bos.domain.take_delivery.WorkBill;
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
    
    @Autowired
    private FixedAreaRepository fixedAreaRepository;
    
    @Autowired
    private WorkBillRepository workBillRepository;
    
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
        
        order.setOrderNum(UUID.randomUUID().toString().replaceAll("-", ""));
        order.setOrderTime(new Date());
         orderRepository.save(order);
         
         
         //自动分单
         String sendAddress = order.getSendAddress();
         if(StringUtils.isNotEmpty(sendAddress)){
             //根据发件地址完全匹配
             //让crm系统根据地址查询定区id
             //做这个测试之前先关联一个客户  ,下单的地址必须和客户的地址相同
             String fixedAreaId = WebClient.create("http://localhost:8180/crm/webService/customerService/findByFixedAreaIdByAddress")
                 .accept(MediaType.APPLICATION_JSON)
                 .type(MediaType.APPLICATION_JSON)
                 .query("address", sendAddress)
                 .get(String.class);
             //根据定区id查询定区
             if(StringUtils.isNotEmpty(fixedAreaId)){
                 FixedArea fixedArea = fixedAreaRepository.findOne(Long.parseLong(fixedAreaId));
                 if(fixedArea!=null){
                     Set<Courier> couriers = fixedArea.getCouriers();
                     if(!couriers.isEmpty()){
                         //进行迭代  查询快递员
                         Iterator<Courier> iterator = couriers.iterator();
                         Courier courier = iterator.next();//查询第一个快递员
                         order.setCourier(courier);//指派快递员
                         
                         //生成工单
                         WorkBill workBill = new WorkBill();
                         workBill.setAttachbilltimes(0);//追单次数
                         workBill.setBuildtime(new Date());
                         workBill.setCourier(courier);
                         workBill.setOrder(order);
                         workBill.setPickstate("新单");//取件状态
                         workBill.setRemark(order.getRemark());//订单备注
                         workBill.setSmsNumber("9527");//短信序号
                         workBill.setType("新");
                         
                         
                         workBillRepository.save(workBill);
                         //发送短信给快递员一个消息  推送通知
                         //中断代码的执行(如果中断 就会走下面的那些代码)
                         order.setOrderType("自动分单");
                         return;
                     }
                 }
             }else{
                 //定区关联分区,在页面上填写的发件地址,必须是对应的分区的关键字或者辅助关键字
                 Area sendArea2 = order.getSendArea();
                 if(sendArea2!=null){
                     Set<SubArea> subareas = sendArea2.getSubareas();
                     for (SubArea subArea : subareas) {
                        String keyWords = subArea.getKeyWords();//关键字
                        String assistKeyWords = subArea.getAssistKeyWords();//辅助关键字
                        if(sendAddress.contains(keyWords)||sendAddress.contains(assistKeyWords)){
                            //如果发件人地址包含关键字(通过分区查询定区)
                            FixedArea fixedArea = subArea.getFixedArea();
                            if(fixedArea!=null){
                                Set<Courier> couriers = fixedArea.getCouriers();
                                if(!couriers.isEmpty()){
                                    Iterator<Courier> iterator = couriers.iterator();
                                    Courier courier = iterator.next();
                                    order.setCourier(courier);//指派快递员
                                    
                                    //生成工单
                                    WorkBill workBill = new WorkBill();
                                    workBill.setAttachbilltimes(0);//追单次数
                                    workBill.setBuildtime(new Date());
                                    workBill.setCourier(courier);
                                    workBill.setOrder(order);
                                    workBill.setPickstate("新单");//取件状态
                                    workBill.setRemark(order.getRemark());//订单备注
                                    workBill.setSmsNumber("9527");//短信序号
                                    workBill.setType("新");
                                    
                                    workBillRepository.save(workBill);
                                    order.setOrderType("自动分单");
                                    return;
                                }
                            }
                        }
                    }
                 }
             }
         }
         //根据发件地址进行模糊匹配
         order.setOrderType("人工分单");
    }

}
  
