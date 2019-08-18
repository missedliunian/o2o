package com.think.o2o.dao;

import com.think.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopDao {
    int insertShop(Shop shop);

    int updateShop(Shop shop);

    int queryShopCount(@Param("shopCondition") Shop shopCondition);

    Shop queryByShopId(@Param("shopId") long shopId);

    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,
                             @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
}
