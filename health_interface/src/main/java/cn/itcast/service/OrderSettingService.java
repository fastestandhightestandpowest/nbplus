package cn.itcast.service;

import cn.itcast.pojo.OrderSetting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author hua
 * @date 2019/07/24 10:41
 **/
public interface OrderSettingService {
    void addExcel(ArrayList<OrderSetting> list);

    List<Map> getOrderInfo(Map<String, String> map);

    void editNumberByOrderDate(OrderSetting orderSetting);

    void clear(Date firstDay4ThisMonth);
}
