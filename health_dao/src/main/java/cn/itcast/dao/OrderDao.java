package cn.itcast.dao;

import cn.itcast.pojo.Member;
import cn.itcast.pojo.Order;
import cn.itcast.pojo.OrderSetting;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author hua
 * @date 2019/07/26 10:45
 **/
public interface OrderDao {

    @Select("select * from t_ordersetting where orderDate = #{orderDate}")
    OrderSetting queryByOrderDate_Ordersetting(String orderDate);

    @Select("select * from t_member where phoneNumber = #{telephone}")
    Member findByphoneNumber_t_member(String telephone);

    @Select("select * from t_order where member_id = #{memberId} and package_id = #{packId} and orderDate = #{orderDate}")
    List<Order> queryByPackageIDAndMemberId_t_order(@Param("packId") String packId,@Param("memberId") int memberId,@Param("orderDate") String orderDate);

    void add(Order order);

    Map<String,Object> getOrderSuccessInfoById(int id);

    /**
     * 指定日期到诊人数
     * @param date
     * @return
     */
    @Select("select count(1) from t_order where orderDate = #{date} and orderStatus = '已到诊'")
    Integer findVisitedCountByDate(Date date);

    /**
     * 指定日期预约人数
     * @param date
     * @return
     */
    @Select("select count(1) from t_order where orderDate = #{date}")
    Integer findOrderCountByDate(Date date);

    /**
     * 指定日期之间预约人数
     * @return
     */
    @Select("select count(1) from t_order where orderDate BETWEEN #{startDate} and #{endDate}")
    Integer findBetweenOrderDateNums(@Param("startDate") Date startDate,@Param("endDate") Date endDate);

    /**
     * 指定日期之间到诊人数
     * @return
     */
    @Select("select count(1) from t_order where orderDate BETWEEN #{startDate} and #{endDate} and orderStatus = '已到诊'")
    Integer findBeweenVisitedNums(@Param("startDate") Date startDate,@Param("endDate") Date endDate);
}
