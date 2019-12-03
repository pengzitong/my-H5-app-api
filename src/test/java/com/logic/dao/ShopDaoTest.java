package com.logic.dao;

import com.logic.BaseTest;
import com.logic.entity.Area;
import com.logic.entity.PersonInfo;
import com.logic.entity.Shop;
import com.logic.entity.ShopCategory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopDaoTest extends BaseTest {

    @Autowired
    private ShopDao shopDao;


    @Test
    public void testQueryShopListAndCount(){
        Shop shopCondition = new Shop();
        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);
        shopCondition.setOwner(owner);
        List<Shop> shopList = shopDao.queryShopList(shopCondition,0,5);
        System.out.println("筛选后商铺大小：" + shopList.size());
        int count = shopDao.queryShopListCount(shopCondition);
        System.out.println("筛选到商铺总数（不计分页）：" + count);

        shopCondition.setShopName("测试店铺");
        shopList = shopDao.queryShopList(shopCondition,0,5);
        System.out.println("新筛选后商铺大小：" + shopList.size());
        count = shopDao.queryShopListCount(shopCondition);
        System.out.println("新筛选到商铺总数（不计分页）：" + count);

        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(1L);
        shopCondition.setShopCategory(shopCategory);
        shopList = shopDao.queryShopList(shopCondition,0,5);
        System.out.println("xin筛选后商铺大小：" + shopList.size());
        count = shopDao.queryShopListCount(shopCondition);
        System.out.println("xin筛选到商铺总数（不计分页）：" + count);
    }

    @Test
    @Ignore
    public void testQueryByShopId(){
        Shop shop = shopDao.queryByShopId(70L);
        System.out.println("areaId:" + shop.getArea().getAreaId());
        System.out.println("areaName:" + shop.getArea().getAreaName());
    }

    @Test
    @Ignore
    public void testInsertShop(){
        Shop shop = new Shop();
        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);
        Area area = new Area();
        area.setAreaId(3);
        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(1L);
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试店铺1111");
        shop.setShopDesc("测试店铺描述");
        shop.setShopAddr("test");
        shop.setPhone("test");
        shop.setShopImg("test");
        shop.setEnableStatus(1);
        shop.setAdvice("审核中");
        shop.setCreateTime(new Date());
        int effectedNum = shopDao.insertShop(shop);
        assertEquals(1,effectedNum);
    }

    @Test
    @Ignore
    public void testUpdateShop(){
        Shop shop = new Shop();
        shop.setShopId(2L);
        shop.setShopDesc("测试更新的描述");
        shop.setShopAddr("测试更新的地址");
        shop.setLastEditTime(new Date());
        int effectedNum = shopDao.updateShop(shop);
        assertEquals(1,effectedNum);
    }
}
