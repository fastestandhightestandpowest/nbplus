package cn.itcast.service.impl;

import cn.itcast.constant.MessageConstant;
import cn.itcast.dao.MemberDao;
import cn.itcast.dao.OrderDao;
import cn.itcast.dao.OrderSettingDao;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Member;
import cn.itcast.pojo.Order;
import cn.itcast.pojo.OrderSetting;
import cn.itcast.service.OrderService;
import cn.itcast.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author hua
 * @date 2019/07/26 10:40
 **/
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderSettingDao orderSettingDao;


    @Override
    @Transactional
    public Result order(Map map) throws Exception {
        //判断当前日期是否设置了预约,查询t_ordersetting
        String orderDate = (String) map.get("orderDate"); //这里拿到的是yyyy-MM-dd字符串,可以直接在数据库比较使用
        OrderSetting orderSetting = orderDao.queryByOrderDate_Ordersetting(orderDate);
        if (orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //判断当前日期预约是否已满
        if (orderSetting.getReservations() >= orderSetting.getNumber()) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //判断是否是会员,查询t_member
        String telephone = (String) map.get("telephone");
        Member member = orderDao.findByphoneNumber_t_member(telephone);
        if (member == null){
            //不是会员,自动注册为会员,添加进t_member
            member = new Member();
            member.setName((String) map.get("name"));
            member.setSex((String) map.get("sex"));
            member.setIdCard((String) map.get("idCard"));
            member.setPhoneNumber((String) map.get("telephone"));
            member.setRegTime(new Date());
            memberDao.add(member); //这里需要添加之后再查询id放入对象
        }else{
            //是会员,避免重复预约,t_order
            //根据套餐id和member_id查询是否有记录 // TODO 应该再加上orderDate
            String packId = (String) map.get("packageId");
            int memberId = member.getId();
            List<Order> list = orderDao.queryByPackageIDAndMemberId_t_order(packId,memberId,orderDate);
            if (list != null && list.size() != 0){
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }
        //进行预约,预约人数+1 t_order表添加一条记录,t_ordersetting已预约人数+1
        Integer packId = Integer.parseInt((String)map.get("packageId"));
        Order order = new Order((Integer) member.getId(),
                DateUtils.parseString2Date(orderDate),
                (String)map.get("orderType"),
                Order.ORDERSTATUS_NO,
                packId);
        orderDao.add(order); //添加后查询id了
        orderSettingDao.updateReservationsByOrderDate(orderDate);
        //还需要把预约Id返回前端
        return new Result(true, MessageConstant.ORDER_SUCCESS,order);
    }

    /**
     * 得到一些预约成功界面的数据
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> getOrderSuccessInfoById(int id) {
        return orderDao.getOrderSuccessInfoById(id);
    }
}
