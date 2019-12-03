package com.logic.dao;

import com.logic.entity.Shop;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ShopDao {
    /**
     * 根据用户传入的条件筛选商铺信息，可输入条件：店铺名（模糊查询），店铺状态，店铺类别，区域id，owner
     * @param shopCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,@Param("rowIndex") int rowIndex,
                             @Param("pageSize") int pageSize);


    /**
     * 根据用户传入的筛选条件查询商铺的总数
     * @param shopCondition
     * @return
     */
    int queryShopListCount(@Param("shopCondition") Shop shopCondition);
    /**
     * 根据店铺id查询店铺信息
     * @param shopId
     * @return
     */
    Shop queryByShopId(@Param(value="shopId") long shopId);
    /**
     * 新增店铺
     * @param shop
     * @return
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺
     * @param shop
     * @return
     */
    int updateShop(Shop shop);
}
