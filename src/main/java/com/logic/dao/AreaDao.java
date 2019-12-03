package com.logic.dao;

import com.logic.entity.Area;
import java.util.List;

public interface AreaDao {
    /**
     * 列出（查询）区域列表
     * @return  areaList
     */
    List<Area> queryArea();
}
