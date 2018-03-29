package com.qq.www.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.qq.www.bos.domain.base.Standard;

/**  
 * ClassName:StandardRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月13日 下午11:22:14 <br/>       
 */
//泛型1:封装数据的对象类型
//泛型2:对象的主键类型
public interface StandardRepository extends JpaRepository<Standard, Long>{

    //SpringDataJPA提供了一套命名规范,遵循这一套规范定义查询类方法(只能是查询)
    //定义方法必须要求以findBy开头  后面跟着属性名,首字母大写 如果有多个条件就是用对应的sql关键字
    List<Standard> findByName(String name);
    List<Standard> findByNameLike(String name);
    List<Standard> findByNameAndMaxWeight(String name,Integer maxWeight);
    
    //以上是遵循命名规范 如果不想遵循命名规范
    //这种查询语句是JPQL  相当于我们的HQL 引号里面自己定义查询语句
    @Query("from Standard where name = ? and maxWeight = ?")
    List<Standard> findByNameAndMaxWeight123(String name,Integer maxWeight);
    
    //在问号的后面加上数字改变匹配参数的顺序
    @Query("from Standard where name = ?2 and maxWeight = ?1")
    List<Standard> findByNameAndMaxWeight123(Integer maxWeight,String name);
    
    //原生SQL
    @Query(value = "select * from T_STANDARD where C_NAME = ? and MAXWEIGHT = ?" ,nativeQuery=true)
    List<Standard> findByNameAndMaxWeight12(Integer maxWeight,String name);
    
    //自定义方法(通过名字更新最大重量) 更新类方法没有遵循命名规范
    /*@Transactional  在这里打上注解是防止事务回滚的一种解决方法*/
    @Modifying//备注一下这是更改类的操作
    @Query("update Standard set maxWeight = ? where name = ?")
     void updateWeightByName(Integer maxWeight,String name);
    
    /*@Transactional  在这里打上注解是防止事务回滚的一种解决方法*/
    @Modifying
    @Query("delete from Standard where name = ?")
    void deleteByName(String name);
}
  
