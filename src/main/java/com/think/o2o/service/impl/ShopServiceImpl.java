package com.think.o2o.service.impl;

import com.think.o2o.dao.ShopDao;
import com.think.o2o.dto.ShopExecution;
import com.think.o2o.entity.Shop;
import com.think.o2o.enums.ShopStateEnum;
import com.think.o2o.exceptions.ShopOperationException;
import com.think.o2o.service.ShopService;
import com.think.o2o.util.ImageUtil;
import com.think.o2o.util.PageCalculator;
import com.think.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

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

    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculatorRowIndex(pageIndex, pageSize);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution shopExecution = new ShopExecution();
        if (shopList != null) {
            shopExecution.setShopList(shopList);
            shopExecution.setCount(count);
        } else {
            shopExecution.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return shopExecution;
    }

    @Override
    public ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException {
        if (shop == null || shop.getShopId() == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        } else {
            if (shopImgInputStream != null && fileName != null && !fileName.equals("")) {
                Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                if (tempShop.getShopImg() != null) {
                    ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                }
                addShopImg(shop, shopImgInputStream, fileName);
            }
            shop.setLastEditTime(new Date());
            int effectNum = shopDao.updateShop(shop);
            if (effectNum <= 0) {
                return new ShopExecution(ShopStateEnum.INNER_ERROR);
            } else {
                shop = shopDao.queryByShopId(shop.getShopId());
                return new ShopExecution(ShopStateEnum.SUCCESS, shop);
            }
        }
    }

    private void addShopImg(Shop shop, InputStream shopImgInputStream, String fileName) {
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(shopImgInputStream, fileName, dest);
        shop.setShopImg(shopImgAddr);
    }
}
