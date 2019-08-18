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
import java.util.List;

public class ShopDaoTest extends BaseTest {
    @Autowired
    private ShopDao shopDao;

    @Test
    public void testQueryShopListAndCount() {
        Shop shopCondition = new Shop();
        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);
        shopCondition.setOwner(owner);
        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(22L);
        shopCondition.setShopCategory(shopCategory);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 2);
        int count = shopDao.queryShopCount(shopCondition);
        System.out.println(shopList.size() + "     " + count);
    }

    @Test
    @Ignore
    public void testQueryByShopId() {
        long shopId = 28L;
        Shop shop = shopDao.queryByShopId(shopId);
        System.out.println(shop.getArea().getAreaName());
        System.out.println(shop.getShopCategory().getShopCategoryName());
        System.out.println(shop.getOwner().getName());
    }

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
    @Ignore
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
