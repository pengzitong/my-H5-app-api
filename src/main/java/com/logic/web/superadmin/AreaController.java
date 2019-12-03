package com.logic.web.superadmin;

import com.logic.entity.Area;
import com.logic.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Controller
@RequestMapping("/superAdmin")
public class AreaController {
    Logger logger = LoggerFactory.getLogger(AreaController.class);
    @Autowired
    private AreaService areaService;


    @RequestMapping(value = "/listArea",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listArea(){
        logger.info("======start======");
        long startTime = System.currentTimeMillis();

        Map<String,Object> modelMap = new HashMap<String, Object>();
        List<Area> areaList = new ArrayList<Area>();

        try{
           areaList = areaService.getAreaList();
           modelMap.put("rows",areaList);
           modelMap.put("total",areaList.size());
        }catch (Exception e){
            e.printStackTrace();
            modelMap.put("success",false);
            modelMap.put("errorMsg",e.toString());
        }

        logger.error("test error!");
        long endTime = System.currentTimeMillis();
        logger.debug("costTime:[{}ms]",endTime-startTime);
        System.out.println(System.getProperty("catalina.base"));
        logger.info("======end======");
        return modelMap;

    }


}
