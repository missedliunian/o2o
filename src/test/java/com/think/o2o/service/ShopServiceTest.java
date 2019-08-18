package com.think.o2o.service;

import com.think.o2o.BaseTest;
import com.think.o2o.dto.ShopExecution;
import com.think.o2o.entity.Area;
import com.think.o2o.entity.PersonInfo;
import com.think.o2o.entity.Shop;
import com.think.o2o.entity.ShopCategory;
import com.think.o2o.enums.ShopStateEnum;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class ShopServiceTest extends BaseTest {
    @Autowired
    private ShopService shopService;

    @Test
    public void testGetShopList() {
        Shop shopCondition = new Shop();
        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(22L);
        shopCondition.setShopCategory(shopCategory);
        ShopExecution shopExecution = shopService.getShopList(shopCondition, 2, 3);
        System.out.println(shopExecution.getShopList().size());
        System.out.println(shopExecution.getCount());
    }

    @Test
    @Ignore
    public void testModifyShop() throws FileNotFoundException {
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setShopName("修改后的店铺名称");
        File shopImg = new File("D:/download/girl.jpg");
        InputStream is = new FileInputStream(shopImg);
        ShopExecution shopExecution = shopService.modifyShop(shop, is, "girl.jpg");
        System.out.println(shopExecution.getShop().getShopImg());
    }


    @Test
    @Ignore
    public void testAddShop() throws FileNotFoundException {
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
        shop.setShopName("测试一下店铺4");
        shop.setShopDesc("test啊4");
        shop.setShopAddr("店铺地址4");
        shop.setPhone("13555555554");
        shop.setPriority(1);
        shop.setAdvice("审核中");
        File shopImg = new File("D:/download/image.jpg");
        InputStream is = new FileInputStream(shopImg);
        ShopExecution shopExecution = shopService.addShop(shop, is, shopImg.getName());
//        System.out.println(shopExecution);
    }
}
