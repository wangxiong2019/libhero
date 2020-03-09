package com.hero.libhero.mydb;


import java.util.List;
import java.util.Map;

/**
 * 本地数据库
 */
public interface DbDao<T> {

    //查询某个类

    //一个参数
    T selectClassByKey(Class<T> clazz, String key, String value);

    //多个参数
    List<T> selectClassByMap(Class<T> clazz, String key, String value, Map<String, Object> map);


    //查询list

    List<T> selectList(Class<T> clazz);

    List<T> selectListByKey(Class<T> clazz, String key, String value);

    List<T> selectClassByKeyLike(Class<T> clazz, String key, String value);

    List<T> selectListByOrder(Class<T> clazz, String order);

    void saveOrUpdate(Object t);

    void deleteById(Class<T> clazz, Object id);


    void deleteList(Class<T> clazz);

    //update


}
