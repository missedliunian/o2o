package com.think.o2o.service;

import com.think.o2o.BaseTest;
import com.think.o2o.entity.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AreaServiceTest extends BaseTest {
    @Autowired
    private AreaService areaService;

    @Test
    public void testGetAreaList() {
        System.out.println(areaService.getAreaList());

    }
}
