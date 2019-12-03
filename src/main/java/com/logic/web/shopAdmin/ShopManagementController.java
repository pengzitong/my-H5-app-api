package com.logic.web.shopAdmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.logic.dao.AreaDao;
import com.logic.dto.ShopExecution;
import com.logic.entity.Area;
import com.logic.entity.PersonInfo;
import com.logic.entity.Shop;
import com.logic.entity.ShopCategory;
import com.logic.enums.ShopStateEnum;
import com.logic.service.AreaService;
import com.logic.service.ShopCategoryService;
import com.logic.service.ShopService;
import com.logic.util.CodeUtil;
import com.logic.util.HttpServletRequestUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/shopAdmin")
public class ShopManagementController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    private Producer captchaProducer = null;

    @Autowired
    public void setCaptchaProducer(Producer captchaProducer) {
        this.captchaProducer = captchaProducer;
    }

    @RequestMapping(value = "/getShopInitInfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopInitInfo(){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
        List<Area> areaList = new ArrayList<Area>();

        try{
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList",shopCategoryList);
            modelMap.put("areaList",areaList);

        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errorMsg",e.getMessage());
        }


        return modelMap;
    }


    @RequestMapping(value = "/registerShop",method = RequestMethod.POST)
    @ResponseBody  //自动转换成json
    private Map<String,Object> registerShop(HttpServletRequest request){
        //客户端的请求信息都会存储在HttpServletRequest对象中
        //1.接收并转化相应的参数，包括店铺信息以及图片信息

        Map<String,Object> modelMap = new HashMap<>();
        if(!CodeUtil.checkVerifyCode(request)){  //验证码进行验证
            modelMap.put("success",false);
            modelMap.put("errorMsg","验证码不正确");
            return modelMap;
        }

        String shopStr = HttpServletRequestUtil.getString(request,"shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try{
            shop = mapper.readValue(shopStr,Shop.class);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errorMsg",e.getMessage());
            return modelMap;
        }
        //获取前端传入的文件流
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext()); //将当前会话的上下文初始化给CommonsMultipartResolver
        if(commonsMultipartResolver.isMultipart(request)){  //判断request是否有文件上传，即多部分请求
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request; //转换为多部分请求
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }else{
            modelMap.put("success",false);
            modelMap.put("errorMsg","上传图片信息不能为空！");
            return modelMap;
        }
        //2.注册店铺
        if(shop != null && shopImg != null){
//            PersonInfo owner = new PersonInfo();
//            owner.setUserId(1L);
            PersonInfo owner = (PersonInfo)request.getSession().getAttribute("user");
            shop.setOwner(owner);

//            File shopImgFile = new File(PathUtil.getImgBasePath() + ImageUtil.getRandomFileName());
//            try {
//                shopImgFile.createNewFile();
//            } catch (IOException e) {
//                modelMap.put("success",false);
//                modelMap.put("errorMsg",e.getMessage());
//                return modelMap;
//            }
//            try {
//                inputStreamToFile(shopImg.getInputStream(),shopImgFile);
//            } catch (IOException e) {
//                modelMap.put("success",false);
//                modelMap.put("errorMsg",e.getMessage());
//                return modelMap;
//            }

            try {
                ShopExecution se = shopService.addShop(shop,shopImg.getInputStream(),shopImg.getOriginalFilename());
                if (se.getState() == ShopStateEnum.CHECK.getState()){
                    modelMap.put("success",true);
                    //该用户可以操作的店铺列表
                    @SuppressWarnings("unchecked")
                    List<Shop> shopList = (List<Shop>)request.getSession().getAttribute("shopList");
                    if(shopList == null || shopList.size() == 0){
                        shopList = new ArrayList<Shop>();
                    }
                    shopList.add(se.getShop());
                    request.getSession().setAttribute("shopList",shopList);

                }else{
                    modelMap.put("success",false);
                    modelMap.put("errorMsg",se.getStateInfo());
                }
            } catch (IOException e) {
                modelMap.put("success",false);
                modelMap.put("errorMsg",e.getMessage());
            }

            return modelMap;
        }else{
            modelMap.put("success",false);
            modelMap.put("errorMsg","请输入店铺信息！");
            return modelMap;
        }

        //3.返回结果
        //return modelMap;
    }

    //inputStream 转换成 File
//    private static void inputStreamToFile(InputStream ins, File file){
//        FileOutputStream os = null;
//        try {
//            os = new FileOutputStream(file);
//            int bytesRead = 0;
//            byte[] buffer = new byte[1024];
//            while ((bytesRead = ins.read(buffer)) != -1){
//                os.write(buffer,0,bytesRead);
//            }
//        }catch (Exception e){
//            throw new RuntimeException("调用inputStreamToFile异常：" + e.getMessage());
//        }finally {
//            try {
//                if(os != null){
//                    os.close();
//                }
//                if(ins != null){
//                    ins.close();
//                }
//            } catch (IOException e) {
//                throw new RuntimeException("调用inputStreamToFile关闭io产生异常：" + e.getMessage());
//            }
//        }
//    }

    @RequestMapping(value = "/getByShopId",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> getByShopId(@RequestBody Shop shop){
        HashMap<String,Object> modelMap = new HashMap<>();

        if(shop.getShopId() > -1){
            try{
                Shop shopInfo = shopService.getByShopId(shop.getShopId());
                modelMap.put("shop",shopInfo);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("areaList",areaList);
                modelMap.put("success",true);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errorMsg",e.getMessage());
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errorMsg","empty shopId");
        }

        return modelMap;
    }

    @RequestMapping(value = "/getByShopId1",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getByShopId1(HttpServletRequest request){
        HashMap<String,Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        System.out.println(request.getParameter("shopId"));
        Shop shopInfo = shopService.getByShopId(shopId);
        modelMap.put("shop",shopInfo);
        return modelMap;
    }


    @RequestMapping(value = "/modifyShop",method = RequestMethod.POST)
    @ResponseBody  //自动转换成json
    private Map<String,Object> modifyShop(HttpServletRequest request){
        //客户端的请求信息都会存储在HttpServletRequest对象中
        //1.接收并转化相应的参数，包括店铺信息以及图片信息

        Map<String,Object> modelMap = new HashMap<>();
        if(!CodeUtil.checkVerifyCode(request)){  //验证码进行验证
            modelMap.put("success",false);
            modelMap.put("errorMsg","验证码不正确");
            return modelMap;
        }

        String shopStr = HttpServletRequestUtil.getString(request,"shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try{
            shop = mapper.readValue(shopStr,Shop.class);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errorMsg",e.getMessage());
            return modelMap;
        }
        //获取前端传入的文件流
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext()); //将当前会话的上下文初始化给CommonsMultipartResolver
        if(commonsMultipartResolver.isMultipart(request)){  //判断request是否有文件上传，即多部分请求
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request; //转换为多部分请求
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }
        //2.修改店铺信息
        if(shop != null && shop.getShopId() != null){
            ShopExecution se;
            try {
                if(shopImg == null){
                    se = shopService.modifyShop(shop,null,null);

                }else{
                    se = shopService.modifyShop(shop,shopImg.getInputStream(),shopImg.getOriginalFilename());
                }
                if (se.getState() == ShopStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errorMsg",se.getStateInfo());
                }
            } catch (IOException e) {
                modelMap.put("success",false);
                modelMap.put("errorMsg",e.getMessage());
            }

            return modelMap;
        }else{
            modelMap.put("success",false);
            modelMap.put("errorMsg","请输入店铺信息！");
            return modelMap;
        }

    }


}
