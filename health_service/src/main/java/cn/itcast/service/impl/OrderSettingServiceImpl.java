package cn.itcast.service.impl;

import cn.itcast.dao.OrderSettingDao;
import cn.itcast.pojo.OrderSetting;
import cn.itcast.service.OrderSettingService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.*;

/**
 * @Author hua
 * @date 2019/07/24 10:42
 **/
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    @Transactional
    public void addExcel(ArrayList<OrderSetting> list) {
        //批量添加
        if (list != null){
            for (OrderSetting orderSetting : list) {
                //检查日期是否已经存在
                int count = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
                if (count > 0) {
                    //存在则修改
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else{
                    //不存在则添加
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    /**
     * 根据日期得到这个月
     * @param map year年 month月
     * @return date日  number可预约人数   reservations已预约人数
     */
    @Override
    public List<Map> getOrderInfo(Map<String, String> map) {
        String year = map.get("year");
        String month = map.get("month");
        //该月第一天
        String startDate = year + "-" + month + "-" + 1;
        //最后一天
        String endDate = year + "-" + month + "-" + 31;
        List<Map> list = orderSettingDao.queryMonth(startDate,endDate);
        ArrayList<Map> arrayList = new ArrayList<>();
        for (Map map1 : list) {
            HashMap<Object, Object> hashMap = new HashMap<>();
            //getdate()是获取是多少号,getday是获取第几周
            //这里是sql.Date
            Date orderDate = (Date) map1.get("orderDate");
            int date = orderDate.getDate();
            hashMap.put("date", date);
            hashMap.put("number", map1.get("number"));
            hashMap.put("reservations", map1.get("reservations"));
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    /**
     * 根据日期修改可预约人数
     * 修改的可预约人数不能低于已预约人数
     * @param orderSetting
     */
    @Override
    @Transactional
    public void editNumberByOrderDate(OrderSetting orderSetting) {
        //先查询该日期是否存在
//        数据库中的日期是sql.date,需要先转换类型   //不需要了,因为在前端已经转换了
//        Date date = new Date(orderSetting.getOrderDate().getTime());
        int count = orderSettingDao.isExistByOrderDate(orderSetting.getOrderDate());
        if (count > 0){
            //修改之前先进行判断修改的数量是否低于已预约人数
            //查询已预约人数
            int reservations = orderSettingDao.queryNumberByOrderDate(orderSetting.getOrderDate());
            if (orderSetting.getNumber() > reservations){
                orderSettingDao.updateNumberByOrderDate(orderSetting.getOrderDate(),orderSetting.getNumber());
            }else{
                throw new RuntimeException("修改的数量不能低于已预约人数");
            }
        }else{
            //插入
            orderSettingDao.add(orderSetting);
        }
    }
// 定期清理OrderSetting数据
    @Override
    @Transactional
    public void clear(java.util.Date firstDay4ThisMonth) {
        orderSettingDao.clear(firstDay4ThisMonth);
    }
}
