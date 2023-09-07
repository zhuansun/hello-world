package com.zspc.hw.common.entity.base;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class BaseModel implements Serializable {
    private static final long serialVersionUID = 0L;

    public static final Long DEFAULT_OPERATOR_ID = 1L;
    public static final String DEFAULT_OPERATOR = "数据中心";

    public static final String PRIMARY_KEY = "id";
    public static final String CREATE_TIME = "createTime";
    public static final String UPDATE_TIME = "updateTime";
    public static final String CREATOR_ID = "creatorId";
    public static final String UPDATOR_ID = "updaterId";
    public static final String CREATOR = "creator";
    public static final String UPDATOR = "updater";
    public static final String VERSION = "version";

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 创建人id
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 修改人id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updaterId;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;

    /**
     * 备注
     */
    private String reMark;

    /**
     * version
     */
    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;

}
