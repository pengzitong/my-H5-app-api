package com.logic.service.impl;

import com.logic.dao.ShopDao;
import com.logic.dto.ShopExecution;
import com.logic.entity.Shop;
import com.logic.enums.ShopStateEnum;
import com.logic.exceptions.ShopOperationException;
import com.logic.service.ShopService;
import com.logic.util.ImageUtil;
import com.logic.util.PageCaculator;
import com.logic.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;

    @Override
    public ShopExecution queryShopList(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCaculator.calculateRowIndex(pageIndex,pageSize);
        List<Shop> shopList = shopDao.queryShopList(shopCondition,pageIndex,pageSize);
        int count = shopDao.queryShopListCount(shopCondition);
        ShopExecution shopExecution = new ShopExecution();
        if(shopList != null){
            shopExecution.setShopList(shopList);
            shopExecution.setCount(count);
        }else{
            shopExecution.setState(ShopStateEnum.INNER_ERROR.getState());
        }

        return shopExecution;
    }

    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName) {
        //判断非空
        if(shop == null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try{
            //给店铺信息添加一些初始值
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            //添加店铺信息
            int effectedNum = shopDao.insertShop(shop);
            if(effectedNum <= 0){
                throw new ShopOperationException("店铺创建失败");
            }else{
                if(shopImgInputStream != null){
                    //添加图片
                    System.out.println(shopImgInputStream);
                    try{
                        addShopImg(shop,shopImgInputStream,fileName);

                    }catch (Exception e){
                        throw new ShopOperationException("addShopImg error:" + e.getMessage());
                    }
                    //更新商铺图片信息（把图片存入数据库）
                    effectedNum = shopDao.updateShop(shop);
                    if(effectedNum <= 0){
                        throw new ShopOperationException("更新图片地址失败");
                    }

                }
            }
            return new ShopExecution(ShopStateEnum.CHECK,shop);

        }catch (Exception e){
            throw new ShopOperationException("addShop error:" + e.getMessage());
        }

    }

    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    public ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName) {

       try {
           //1.判断店铺信息是否为null
           if(shop == null || shop.getShopId() == null){
               return new ShopExecution(ShopStateEnum.NULL_SHOP);
           }else{
               //2.判断图片信息
               if(shopImgInputStream != null && fileName != null && !"".equals(fileName)){
                   Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                   if(tempShop.getShopImg() != null){
                       ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                   }
                   addShopImg(shop,shopImgInputStream,fileName);
               }
               //3.更新图片后更新商铺信息
               shop.setLastEditTime(new Date());
               int effectedNum = shopDao.updateShop(shop);
               if(effectedNum <= 0){
                   return new ShopExecution(ShopStateEnum.INNER_ERROR);
               }else{
                   return new ShopExecution(ShopStateEnum.SUCCESS,shopDao.queryByShopId(shop.getShopId()));
               }
           }
       }catch (Exception e){
           throw new ShopOperationException("modifyShop error:" + e.getMessage());
       }

    }

    public void addShopImg(Shop shop, InputStream shopImgInputStram, String fileName){
        //获取shop图片目录的相对值路径
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        //将图片存到目标目录并返回图片地址
        String shopImgAddr = ImageUtil.generateThumbnail(shopImgInputStram,fileName,dest);
        System.out.println(shopImgAddr);
        shop.setShopImg(shopImgAddr);
    }
}
