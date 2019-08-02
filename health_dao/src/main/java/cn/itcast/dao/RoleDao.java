package cn.itcast.dao;

import cn.itcast.pojo.Role;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * @Author hua
 * @date 2019/07/29 10:39
 **/
public interface RoleDao {
    /*@Select("select r.* from t_role r,t_user_role u where r.id = u.role_id and u.user_id = #{id}")
    Set<Role> findRoleByUser_id(Integer id);*/
}
