package com.coffeers.app.framework.bean;

import com.coffeers.app.framework.annotation.entity.Column;
import com.coffeers.app.framework.annotation.entity.ColumnType;
import com.coffeers.app.framework.annotation.entity.Comment;
import com.coffeers.app.framework.enums.ColType;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/25 0025.
 */

public abstract class BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Comment("操作人")
    @ColumnType(type = ColType.VARCHAR,width = 32)
    private String opBy;
    @Column
    @Comment("操作时间")
    @ColumnType(type = ColType.INT,width = 32)
    private Integer opAt;
    @Column
    @Comment("是否删除")
    @ColumnType(type = ColType.BOOLEAN)
    private Boolean delFlag;

    public BaseModel() {
    }

    public Boolean flag() {
        return Boolean.valueOf(false);
    }

    public Integer now() {
        return Integer.valueOf((int)(System.currentTimeMillis() / 1000L));
    }

    public String uid() {
        return null;
    }

    public String getOpBy() {
        return this.opBy;
    }

    public void setOpBy(String opBy) {
        this.opBy = opBy;
    }

    public Integer getOpAt() {
        return this.opAt;
    }

    public void setOpAt(Integer opAt) {
        this.opAt = opAt;
    }

    public Boolean getDelFlag() {
        return this.delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

}
