package cn.itcast.dao;

import cn.itcast.pojo.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author hua
 * @date 2019/07/26 11:10
 **/
public interface MemberDao {

    void add(Member member);

    @Select("select * from t_member where phoneNumber = #{telephone}")
    Member findByphoneNumber(String telephone);

    @Select("select count(1) from t_member where regTime < #{s}")
    int queryMemberCountBeforeRegDate(String s);

    /**
     * 查询指定日期增加的会员
     * @param reportDate
     * @return
     */
    @Select("select count(1) from t_member where regTime = #{reportDate}")
    Integer findTodayNewMember(String reportDate);

    @Select("select count(1) from t_member")
    Integer findAllMember();

    /**
     * 查询在指定日期之间注册的会员
     * @param startDate
     * @param endDate
     * @return
     */
    @Select("select count(1) from t_member where regTime BETWEEN #{startDate} and #{endDate}")
    Integer findBetweenRegDateNums(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    List<Map<String,Object>> findSexCount();

    List<Member> getAgea();
}
