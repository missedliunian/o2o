package com.think.o2o.dao;

import com.think.o2o.BaseTest;
import com.think.o2o.entity.Shop;
import com.think.o2o.entity.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ShopCategoryDaoTest extends BaseTest {
    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Test
    public void testQueryShopCategory() {
        List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(new ShopCategory());
//        System.out.println(shopCategoryList);
        ShopCategory testShopCategory = new ShopCategory();
        ShopCategory parent = new ShopCategory();
        parent.setShopCategoryId(28L);
        testShopCategory.setParent(parent);
        shopCategoryList = shopCategoryDao.queryShopCategory(testShopCategory);
        System.out.println(shopCategoryList);

    }
}
