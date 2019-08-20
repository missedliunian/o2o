package com.think.o2o.service;

import com.think.o2o.dto.ShopExecution;
import com.think.o2o.entity.Shop;
import com.think.o2o.exceptions.ShopOperationException;

import java.io.InputStream;

public interface ShopService {
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName);

    Shop getByShopId(long shopId);

    ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);

    ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException;


}
