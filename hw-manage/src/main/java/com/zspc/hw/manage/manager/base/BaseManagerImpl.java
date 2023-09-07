package com.zspc.hw.manage.manager.base;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zspc.hw.common.entity.base.BaseModel;
import com.zspc.hw.common.exception.BusinessException;
import com.zspc.hw.common.util.ColumnUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.SqlSession;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BaseManagerImpl<M extends BaseMapper<T>, T extends BaseModel> extends ServiceImpl<M, T> implements BaseManager<T> {

    @Override
    public QueryWrapper<T> newQueryWrapper() {
        return new QueryWrapper<>();
    }

    /**
     * 插入并返回结果
     *
     * @param entity
     * @return
     */
    @Override
    public T saveAndReturn(T entity) {
        this.save(entity);
        return this.getById(entity.getId());
    }

    /**
     * 根据id更新并返回结果
     *
     * @param entity
     * @return
     */
    @Override
    public T updateByIdAndReturn(T entity) {
        this.updateById(entity);
        return this.getById(entity.getId());
    }

    /**
     * 更新并返回结果
     *
     * @param entity
     * @param updateWrapper
     * @return
     */
    @Override
    public T updateAndReturn(T entity, QueryWrapper<T> updateWrapper) {
        this.update(entity, updateWrapper);
        return this.getById(entity.getId());
    }

    /**
     * 根据id查询并行锁
     *
     * @param id
     * @return
     */
    @Override
    public T getByIdForUpdate(Serializable id) {
        QueryWrapper<T> queryWrapper = newQueryWrapper();
        queryWrapper.eq(BaseModel.PRIMARY_KEY, id).last(" for update");
        return this.getOne(queryWrapper);
    }

    /**
     * 获取最小值
     * 日期默认响应TimeStamp
     *
     * @param column
     * @param queryWrapper
     * @return
     */
    @Override
    public <R> R min(ColumnUtils.Fun<T, R> column, QueryWrapper<T> queryWrapper) {
        queryWrapper.select("MIN(" + ColumnUtils.resolveColumn(column) + ") as min");
        Map<String, Object> map = this.getMap(queryWrapper);
        Object sqlResult = map == null ? null : map.get("min");
        return ColumnUtils.convertSqlResultToPatternReturnType(column, sqlResult);
    }

    /**
     * 获取最大值
     * 日期默认响应TimeStamp
     *
     * @param column
     * @param queryWrapper
     * @return
     */
    @Override
    public <R> R max(ColumnUtils.Fun<T, R> column, QueryWrapper<T> queryWrapper) {
        queryWrapper.select("MAX(" + ColumnUtils.resolveColumn(column) + ") as max");
        Map<String, Object> map = this.getMap(queryWrapper);
        Object sqlResult = map == null ? null : map.get("max");
        return ColumnUtils.convertSqlResultToPatternReturnType(column, sqlResult);
    }

    /**
     * 求和
     *
     * @param column
     * @param queryWrapper
     * @return
     */
    @Override
    public <R> Double sum(ColumnUtils.Fun<T, R> column, QueryWrapper<T> queryWrapper) {
        // 查询并获取结果
        queryWrapper.select("SUM(" + ColumnUtils.resolveColumn(column) + ") as sum");
        Map<String, Object> map = this.getMap(queryWrapper);
        if (map == null) {
            return null;
        }

        // 结果转化
        Object sqlResultObj = map.get("sum");
        if (sqlResultObj instanceof BigDecimal) {
            BigDecimal sqlResult = (BigDecimal) sqlResultObj;
            return sqlResult == null ? null : sqlResult.doubleValue();
        } else if (sqlResultObj instanceof Long) {
            Long sqlResult = (Long) sqlResultObj;
            return sqlResult == null ? null : sqlResult.doubleValue();
        } else {
            throw BusinessException.get("求和计算错误");
        }
    }

    /**
     * 获取泛型T的类
     *
     * @return T泛型的Class
     */
    private Class<T> getActualTypeArgument() {
        Type type = getClass().getGenericSuperclass();
        Class<T> result = null;
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            result = (Class<T>) pType.getActualTypeArguments()[1];
        }
        if (result == null) {
            throw BusinessException.get("获取不到泛型类导致异常发生，请检查是否使用了代理等例如（SpyBean）或者遇到了泛型擦除等问题");
        }
        return result;
    }

    @Override
    public boolean updateBatchById(Collection<T> entityList, int batchSize) {
        Assert.notEmpty(entityList, "error: entityList must not be empty");
        String sqlStatement = sqlStatement(SqlMethod.UPDATE_BY_ID);
        List<BatchResult> batchResults;
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int i = 0;
            for (T anEntityList : entityList) {
                if (anEntityList.getVersion() == null) {
                    throw BusinessException.get("批量更新中部分数据未携带版本号");
                }
                MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
                param.put(Constants.ENTITY, anEntityList);
                batchSqlSession.update(sqlStatement, param);
                if (i >= 1 && i % batchSize == 0) {
                    batchResults = batchSqlSession.flushStatements();
                    checkBatchResults(batchResults);
                }
                i++;
            }
            batchResults = batchSqlSession.flushStatements();
            checkBatchResults(batchResults);
        }
        return true;
    }

    private void checkBatchResults(List<BatchResult> batchResultList) {
        if (CollectionUtils.isEmpty(batchResultList)) {
            return;
        }
        batchResultList.forEach(
                bl -> {
                    if (Arrays.stream(bl.getUpdateCounts()).anyMatch(v -> v == 0)) {
                        throw BusinessException.get("批量更新中部分数据更新失败");
                    }
                }
        );
    }
}
