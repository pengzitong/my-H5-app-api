package com.logic.dao;

import com.logic.BaseTest;
import com.logic.entity.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopCategoryDaoTest extends BaseTest {
    @Autowired
    private ShopCategoryDao shopCategoryDao;


    @Test
    public void  testQueryShopCategory(){
        ShopCategory shopCategory = new ShopCategory();
        ShopCategory parent = new ShopCategory();
        parent.setShopCategoryId(1L);
        shopCategory.setParent(parent);
        List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(new ShopCategory());
        for (ShopCategory s:shopCategoryList) {
            System.out.println(s.getShopCategoryId());
        }
        assertEquals(1,shopCategoryList.size());
    }

}
