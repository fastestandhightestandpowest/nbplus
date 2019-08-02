package cn.itcast.dao;

import cn.itcast.pojo.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * @Author hua
 * @date 2019/07/29 10:41
 **/
public interface PermissionDao {
    /*@Select("select * from t_permission p,t_role_permission r where p.id = r.permission_id and r.role_id = #{id}")
    Set<Permission> findPermissionByRole_id(Integer id);*/
}
