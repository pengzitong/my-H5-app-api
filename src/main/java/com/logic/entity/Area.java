package com.logic.entity;

import java.util.Date;


// 区域实体类 注意：成员变量统一用引用类型（如integer） 而不是基础类型（如int），
// 如果用基本类型，当变量为空时会有默认值（如int类型，为空时默认值是0）
public class Area {
    private Integer areaId;   //id

    private String areaName;  //名称

    private Integer priority; //优先级（权重）

    private Date createTime;  //创建时间

    private Date lastEditTime;//更新时间

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }
}
