package com.logic.service;

import com.logic.entity.ShopCategory;
import java.util.List;

public interface ShopCategoryService {
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
