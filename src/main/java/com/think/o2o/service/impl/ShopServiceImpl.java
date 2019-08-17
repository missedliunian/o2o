package com.think.o2o.service.impl;

import com.think.o2o.dao.ShopDao;
import com.think.o2o.dto.ShopExecution;
import com.think.o2o.entity.Shop;
import com.think.o2o.enums.ShopStateEnum;
import com.think.o2o.exceptios.ShopOperationException;
import com.think.o2o.service.ShopService;
import com.think.o2o.util.ImageUtil;
import com.think.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;

    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName) {
        //空值判断
        if (shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try {
            //设置店铺初始值
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            //添加店铺信息
            //插入成功后shop会被自动注入shopId
            int effectNum = shopDao.insertShop(shop);
            if (effectNum <= 0) {
                //ShopOperationException错误可以使事务处理回滚
                throw new ShopOperationException("店铺创建失败");
            } else {
                if (shopImgInputStream != null) {
                    try {
                        addShopImg(shop, shopImgInputStream, fileName);
                    } catch (Exception e) {
                        throw new ShopOperationException("addShopImg error:" + e.getMessage());
                    }
                    effectNum = shopDao.updateShop(shop);
                    if (effectNum <= 0) {
                        throw new ShopOperationException("更新图片地址失败");
                    }

                }
            }
        } catch (Exception e) {
            throw new ShopOperationException("addShop error:" + e.getMessage());

        }
        return new ShopExecution(ShopStateEnum.CHECK, shop);
    }

    private void addShopImg(Shop shop, InputStream shopImgInputStream, String fileName) {
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(shopImgInputStream, fileName, dest);
        shop.setShopImg(shopImgAddr);
    }
}
