package com.logic.service;

import com.logic.dto.ShopExecution;
import com.logic.entity.Shop;
import com.logic.exceptions.ShopOperationException;

import java.io.InputStream;

public interface ShopService {
    /**
     * 根据前端页码和长度查询店铺
     * @param shopCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ShopExecution queryShopList(Shop shopCondition,int pageIndex,int pageSize);
    /**
     * 新增店铺
     * @param shop
     * @param shopImgInputStream
     * @param fileName
     * @return
     */
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName) throws ShopOperationException;

    /**
     * 根据shopId获取shop信息
     * @param shopId
     * @return
     */
    Shop getByShopId(long shopId);

    /**
     * 修改店铺信息
     * @param shop
     * @param shopImgInputStream
     * @param fileName
     * @return
     */
    ShopExecution modifyShop(Shop shop,InputStream shopImgInputStream,String fileName) throws ShopOperationException;
}
