package com.zspc.hw.web.aspect;

import com.zspc.hw.common.entity.base.BaseModel;
import com.zspc.hw.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class OptimisticLockerExceptionAspect {

    // 针对mybatis-plus中全部的int返回类型作为切点
    @Pointcut("execution(* com.baomidou.mybatisplus.core.mapper.BaseMapper.updateById(..))")
    public void updateByIdPoint() {
    }

    @Around("updateByIdPoint()")
    public Object aroundProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args.length == 0 || !(args[0] instanceof BaseModel)) {
            return joinPoint.proceed();
        }
        BaseModel baseModel = (BaseModel) args[0];
        if (baseModel.getVersion() == null) {
            throw BusinessException.get("入参未携带版本号");
        }
        Object resRow = joinPoint.proceed();
        if (resRow instanceof Integer && (int) resRow == 0) {
            throw BusinessException.get("版本号更新失败");
        }
        return resRow;
    }
}
