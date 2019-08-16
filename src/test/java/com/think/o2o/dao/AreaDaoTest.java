package com.think.o2o.dao;

import com.think.o2o.BaseTest;
import com.think.o2o.entity.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AreaDaoTest extends BaseTest {
    @Autowired
    private AreaDao areaDao;

    @Test
    public void test() {
        List<Area> areaList = areaDao.queryArea();
        System.out.println(areaList);
    }
}
