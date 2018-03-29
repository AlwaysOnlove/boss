package com.qq.www.bos.dao.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.qq.www.bos.dao.base.StandardRepository;
import com.qq.www.bos.domain.base.Standard;

/**  
 * ClassName:StandardRepositoryTest <br/>  
 * Function:  <br/>  
 * Date:     2018年3月13日 下午11:27:23 <br/>       
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class StandardRepositoryTest {

    @Autowired
    private StandardRepository standardRepository;
    @Test//查询所有的数据
    public void test1() {
        List<Standard> standards = standardRepository.findAll();
        for (Standard standard : standards) {
            System.out.println(standard);
        }
    }
    
    @Test//增
    public void test2() {
        Standard standard = new Standard();
        standard.setName("zhangsan");
        standard.setMaxWeight(100);
        standardRepository.save(standard);
    }
    
    @Test//改
    public void test3() {
        Standard standard = new Standard();
        standard.setId(2L);//因为是Long数据类型
        standard.setName("lisi");
        standard.setMaxWeight(100);
        //save方法有增加和修改的功能,如果是修改的话 必须要添加一个id
        standardRepository.save(standard);
    }
    
    @Test//查询单个数据
    public void test4() {
        Standard one = standardRepository.findOne(2L);
        System.out.println(one);
    }

    @Test//删除
    public void test5() {
        //方法里面可以删除所有也可以删除一个 根据id和对象都行
        standardRepository.delete(2L);
    }
    
    @Test//条件查询
    public void test6() {
        //根据名字进行查询
        List<Standard> list = standardRepository.findByName("张三");
        for (Standard standard : list) {
            System.out.println(standard);
        }
    }
    
    @Test//模糊查询
    public void test7() {
        //根据名字进行查询
        List<Standard> list = standardRepository.findByNameLike("%张三%");
        for (Standard standard : list) {
            System.out.println(standard);
        }
    }
    @Test//多个条件查询
    public void test8() {
        //根据名字进行查询
        List<Standard> list = standardRepository.findByNameAndMaxWeight("%张三%",100);
        for (Standard standard : list) {
            System.out.println(standard);
        }
    }
    @Test//多个条件自定义查询
    public void test9() {
        //根据名字进行查询
        List<Standard> list = standardRepository.findByNameAndMaxWeight123("%张三%",100);
        for (Standard standard : list) {
            System.out.println(standard);
        }
    }
    @Test//多个条件自定义查询
    public void test10() {
        //根据名字进行查询
        List<Standard> list = standardRepository.findByNameAndMaxWeight123(100,"%张三%");
        for (Standard standard : list) {
            System.out.println(standard);
        }
    }
    @Test//原生SQL查询
    public void test11() {
        //根据名字进行查询
        List<Standard> list = standardRepository.findByNameAndMaxWeight123(100,"%张三%");
        for (Standard standard : list) {
            System.out.println(standard);
        }
    }
    
    @Rollback(false)
    @Transactional//在测试用例中使用事务注解,方法执行完了以后事务回滚了(解决方案有两种:在测试类方法上面打注解rollback为false
                  //另外一种方法就是将事务注解打在)
    @Test//自定义更新数据
    public void test12() {
        standardRepository.updateWeightByName(200, "张三");
    }
    
    
    @Rollback(false)
    @Transactional//在测试用例中使用事务注解,方法执行完了以后事务回滚了(解决方案有两种:在测试类方法上面打注解rollback为false
    //另外一种方法就是将事务注解打在)
    @Test//自定义更新数据
    public void test13() {
        standardRepository.deleteByName("张三");
    }
}
  
