package cn.itcast.dao;

import cn.itcast.pojo.User;
import org.apache.ibatis.annotations.Select;

/**
 * @Author hua
 * @date 2019/07/29 10:29
 **/
public interface UserDao {
//    @Select("select * from t_user where username = #{username}")

    /**
     * 根据username查找到User中的所有信息
     * @param username
     * @return
     */
    User findUserByUserName(String username);
}
