package cn.itcast.dao;

import cn.itcast.pojo.OrderSetting;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author hua
 * @date 2019/07/24 10:47
 **/
public interface OrderSettingDao {
    /**
     * 根据orderDate查询是否有记录
     * @param orderDate
     * @return
     */
    @Select("select count(1) from t_ordersetting where orderDate = #{orderDate}")
    int findByOrderDate(Date orderDate);

    /**
     * 根据日期修改number
     * @param orderSetting
     */
    @Update("update t_ordersetting set number = #{number} where orderDate = #{orderDate}")
    void editNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 添加记录
     * @param orderSetting
     */
    @Insert("insert into t_ordersetting (orderDate,number,reservations) values (#{orderDate},#{number},#{reservations})")
    void add(OrderSetting orderSetting);

    /**
     * 根据起始日期和结束日期查询
     * @param startDate
     * @param endDate
     * @return
     */
    @Select("select id,orderDate,number,reservations from t_ordersetting where orderDate between #{startDate} and  #{endDate}")
    List<Map> queryMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 根据日期查询记录条数
     * @param date
     * @return
     */
    @Select("select count(1) from t_ordersetting where orderDate = #{date}")
    int isExistByOrderDate(Date date);

    /**
     * 根据日期修改可预约人数
     * @param date
     * @param number
     */
    @Update("update t_ordersetting set number = #{number} where orderDate = #{date}")
    void updateNumberByOrderDate(@Param("date") Date date,@Param("number") int number);

    /**
     * 根据日期查询已预约人数
     * @param orderDate
     */
    @Select("select reservations from t_ordersetting where orderDate = #{orderDate}")
    int queryNumberByOrderDate(Date orderDate);

    /**
     * 根据日期让已预约人数+1
     * @param orderDate
     */
    @Update("update t_ordersetting set reservations = reservations+1 where orderDate = #{orderDate}")
    void updateReservationsByOrderDate(String orderDate);
}
