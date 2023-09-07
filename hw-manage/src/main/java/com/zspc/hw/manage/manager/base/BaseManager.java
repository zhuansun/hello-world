package com.zspc.hw.manage.manager.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zspc.hw.common.entity.base.BaseModel;
import com.zspc.hw.common.util.ColumnUtils;

import java.io.Serializable;

/**
 * @author 01404120
 * @date 2021/2/6 13:32
 */
public interface BaseManager<T extends BaseModel> extends IService<T> {

    /**
     * 获取新的查询条件构造器
     * @return
     */
    QueryWrapper<T> newQueryWrapper();

    /**
     * 插入并返回结果
     *
     * @param entity
     * @return
     */
    T saveAndReturn(T entity);

    /**
     * 根据id更新并返回结果
     *
     * @param entity
     * @return
     */
    T updateByIdAndReturn(T entity);

    /**
     * 更新并返回结果
     *
     * @param entity
     * @param updateWrapper
     * @return
     */
    T updateAndReturn(T entity, QueryWrapper<T> updateWrapper);

    /**
     * 根据id查询并行锁
     *
     * @param id
     * @return
     */
    T getByIdForUpdate(Serializable id);

    /**
     * 获取最小值
     * 日期默认响应TimeStamp
     *
     * @param column
     * @param queryWrapper
     * @return
     */
    <R> R min(ColumnUtils.Fun<T, R> column, QueryWrapper<T> queryWrapper);

    /**
     * 获取最大值
     * 日期默认响应TimeStamp
     *
     * @param column
     * @param queryWrapper
     * @return
     */
    <R> R max(ColumnUtils.Fun<T, R> column, QueryWrapper<T> queryWrapper);

    /**
     * 求和
     *
     * @param column
     * @param queryWrapper
     * @return
     */
    <R> Double sum(ColumnUtils.Fun<T, R> column, QueryWrapper<T> queryWrapper);
}
