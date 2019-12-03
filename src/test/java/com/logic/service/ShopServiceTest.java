package com.logic.service;

import com.logic.BaseTest;
import com.logic.dto.ShopExecution;
import com.logic.entity.Area;
import com.logic.entity.PersonInfo;
import com.logic.entity.Shop;
import com.logic.entity.ShopCategory;
import com.logic.enums.ShopStateEnum;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ShopServiceTest extends BaseTest {
    @Autowired
    private ShopService shopService;

    @Test
    public void testQueryShopListAndCount(){
        Shop shopCondition = new Shop();
        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(1L);
        shopCondition.setShopCategory(shopCategory);
        ShopExecution shopExecution = shopService.queryShopList(shopCondition,0,5);
        System.out.println("店铺列表大小为：" + shopExecution.getShopList().size());
        System.out.println("店铺总数为：" + shopExecution.getCount());
    }

    @Test
    @Ignore
    public void testAddShop() throws FileNotFoundException {
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
        shop.setShopName("测试店铺--service改造后");
        shop.setShopDesc("描述");
        shop.setShopAddr("test1");
        shop.setPhone("test1");
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("审核中");
        shop.setCreateTime(new Date());

        File shopImg = new File("/Users/pzt/workspace/demo/java-project/testImg/car.jpg");
        InputStream is = new FileInputStream(shopImg);
        ShopExecution se = shopService.addShop(shop,is,shopImg.getName());
        assertEquals(ShopStateEnum.CHECK.getState(),se.getState());

    }

    @Test
    @Ignore
    public void testModifyShop() throws FileNotFoundException {
        Shop shop = new Shop();
        shop.setShopId(7L);
        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);
        Area area = new Area();
        area.setAreaId(3);
        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(1L);
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试店铺--service改造后");
        shop.setShopDesc("描述");
        shop.setShopAddr("test修改店铺");
        shop.setPhone("test1");
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("审核中");

        File shopImg = new File("/Users/pzt/workspace/demo/java-project/testImg/car.jpg");
        InputStream is = new FileInputStream(shopImg);
        ShopExecution se = shopService.modifyShop(shop,is,shopImg.getName());
        assertEquals("操作成功",se.getStateInfo());

    }




}
