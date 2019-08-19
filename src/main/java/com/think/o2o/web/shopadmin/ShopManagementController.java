package com.think.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.think.o2o.dto.ShopExecution;
import com.think.o2o.entity.Area;
import com.think.o2o.entity.PersonInfo;
import com.think.o2o.entity.Shop;
import com.think.o2o.entity.ShopCategory;
import com.think.o2o.enums.ShopStateEnum;
import com.think.o2o.service.AreaService;
import com.think.o2o.service.ShopCategoryService;
import com.think.o2o.service.ShopService;
import com.think.o2o.util.CodeUtil;
import com.think.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/getshopmanagementinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopManagementInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");

        if (shopId <= 0) {
            Object currentShopObj = request.getSession().getAttribute("currentShop");
            if (currentShopObj == null) {
                modelMap.put("redirect", true);
                modelMap.put("url", "/o2o/shopadmin/shoplist");

            } else {
                Shop currentShop = (Shop) currentShopObj;
                modelMap.put("redirect", false);
                modelMap.put("shopId", currentShop.getShopId());
            }
        } else {
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop", currentShop);
            modelMap.put("redirect", false);

        }
        return modelMap;

    }

    @RequestMapping(value = "/getshoplist", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        PersonInfo user = new PersonInfo();
        user.setUserId(1L);
        user.setName("test");
        request.getSession().setAttribute("user", user);
        user = (PersonInfo) request.getSession().getAttribute("user");
        try {
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution shopExecution = shopService.getShopList(shopCondition, 0, 100);
            modelMap.put("shopList", shopExecution.getShopList());
            modelMap.put("user", user);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId > -1) {
            try {
                Shop shop = shopService.getByShopId(shopId);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopInitInfo() {
        Map<String, Object> modelMap = new HashMap<>();
        List<ShopCategory> shopCategoryList;
        List<Area> areaList;
        try {
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList", shopCategoryList);
            modelMap.put("areaList", areaList);
            modelMap.put("success", true);

        } catch (Exception e) {
            modelMap.put("errMsg", e.getMessage());
            modelMap.put("success", false);
        }
        return modelMap;
    }

    @RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "错误的验证码");
            return modelMap;
        }
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        //将字符串装换为shop实体类
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (IOException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            e.printStackTrace();
            return modelMap;
        }
        //获取上传的图片
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }
        //修改店铺信息
        if (shop != null && shop.getShopId() != null) {
            ShopExecution shopExecution;
            try {
                if (shopImg == null) {
                    shopExecution = shopService.modifyShop(shop, null, null);
                } else {
                    shopExecution = shopService.modifyShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
                }

                if (shopExecution.getState() == ShopStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", shopExecution.getStateInfo());
                }
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }

            return modelMap;

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺id");
            return modelMap;
        }
    }

    @RequestMapping(value = "/registershop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> registerShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "错误的验证码");
            return modelMap;
        }
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        //将字符串装换为shop实体类
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (IOException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            e.printStackTrace();
            return modelMap;
        }
        //获取上传的图片
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }
        if (shop != null && shopImg != null) {
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
            shop.setOwner(owner);
            ShopExecution shopExecution;
            try {
                shopExecution = shopService.addShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
                if (shopExecution.getState() == ShopStateEnum.CHECK.getState()) {
                    modelMap.put("success", true);
                    //该用户可以操作的店铺列表
                    List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
                    if (shopList == null || shopList.size() == 0) {
                        shopList = new ArrayList<>();
                    }
                    shopList.add(shopExecution.getShop());
                    request.getSession().setAttribute("shopList", shopList);

                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", shopExecution.getStateInfo());
                }
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }

            return modelMap;

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }
    }

}
