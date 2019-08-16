package com.think.o2o.service;

import com.think.o2o.BaseTest;
import com.think.o2o.dto.ShopExecution;
import com.think.o2o.entity.Area;
import com.think.o2o.entity.PersonInfo;
import com.think.o2o.entity.Shop;
import com.think.o2o.entity.ShopCategory;
import com.think.o2o.enums.ShopStateEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Date;

public class ShopServiceTest extends BaseTest {
    @Autowired
    private ShopService shopService;

    @Test
    public void testAddShop() {
        Shop shop = new Shop();
        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        owner.setUserId(8L);
        area.setAreaId(3);
        shopCategory.setShopCategoryId(10L);
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试一下店铺3");
        shop.setShopDesc("test啊3");
        shop.setShopAddr("店铺地址3");
        shop.setPhone("13555555553");
        shop.setPriority(1);
        shop.setAdvice("审核中");
        File shopImg = new File("D:/download/image.jpg");
        ShopExecution shopExecution = shopService.addShop(shop, shopImg);
        System.out.println(shopExecution);
    }
}
