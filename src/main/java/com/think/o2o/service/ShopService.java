package com.think.o2o.service;

import com.think.o2o.dto.ShopExecution;
import com.think.o2o.entity.Shop;

import java.io.File;

public interface ShopService {
    ShopExecution addShop (Shop shop, File shopImg);
}
