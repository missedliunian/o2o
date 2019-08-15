package com.think.o2o.dao;

import com.think.o2o.BaseTest;
import com.think.o2o.entity.Area;
import com.think.o2o.entity.PersonInfo;
import com.think.o2o.entity.Shop;
import com.think.o2o.entity.ShopCategory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class ShopDaoTest extends BaseTest {
    @Autowired
    private ShopDao shopDao;

    @Test
    @Ignore
    public void testInsertShop() {
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
        shop.setShopName("测试一下店铺");
        shop.setShopDesc("test啊");
        shop.setShopAddr("店铺地址");
        shop.setPhone("13555555555");
        shop.setShopImg("图片地址");
        shop.setPriority(1);
        shop.setCreateTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvice("审核中");
        int effectedNum = shopDao.insertShop(shop);

    }

    @Test
    public void testUpdateShop() {
        Shop shop = new Shop();
        shop.setShopId(40L);
        shop.setShopName("更新");
        shop.setShopDesc("更新");
        shop.setShopAddr("更新");
        shop.setPhone("更新");
        shop.setShopImg("更新图片");
        shop.setPriority(1);
        shop.setEnableStatus(1);
        shop.setAdvice("审核中");
        shop.setLastEditTime(new Date());
        int effectedNum = shopDao.updateShop(shop);

    }
}
