package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.pojo.OrderSetting;
import cn.itcast.service.OrderSettingService;
import cn.itcast.utils.POIUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author hua
 * @date 2019/07/24 10:22
 **/
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     *  Excel文件上传，并解析文件内容保存到数据库
     * @param excelFile
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile){
        try {
            POIUtils.checkFile(excelFile);
            List<String[]> rows = POIUtils.readExcel(excelFile);
            ArrayList<OrderSetting> list = new ArrayList<>();
            for (String[] row : rows) {
                list.add(new OrderSetting(new Date(row[0]),Integer.parseInt(row[1])));
            }
            orderSettingService.addExcel(list);
            return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 得到预约信息
     * @param map year年 month月
     * @return date日  number可预约人数   reservations已预约人数
     */
    @RequestMapping("/getOrderInfo")
    public Result getOrderInfo(@RequestParam Map<String,String> map){ //year年 month月 days 天数
        try {
            List<Map> list = orderSettingService.getOrderInfo(map);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    /**
     * 根据日期修改可预约人数
     */
    @PostMapping("/editNumberByOrderDate")
    public Result editNumberByOrderDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByOrderDate(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ORDERSETTING_FAIL+"   "+e.getMessage());
        }
    }
}
