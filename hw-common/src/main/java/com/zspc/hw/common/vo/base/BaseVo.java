package com.zspc.hw.common.vo.base;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseVo implements Serializable {
    private Long id;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 创建人id
     */
    private Long creatorId;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 修改人id
     */
    private Long updatorId;
    /**
     * 修改人
     */
    private String updator;
    /**
     * 备注
     */
    private String reMark;
}
