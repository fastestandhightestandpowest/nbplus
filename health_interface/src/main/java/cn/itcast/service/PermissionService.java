package cn.itcast.service;

import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.pojo.Permission;
import cn.itcast.pojo.Role;

import java.util.List;

/**
 * Package: cn.itcast.service
 * Author: ZZM
 * Date: Created in 2019/8/3 10:16
 **/
public interface PermissionService {
    /**
     * 查询所有权限信息
     */
    PageResult findAll(QueryPageBean queryPageBean);

    /**
     * 回显所有角色信息
     * @return
     */
    List<Role> findRole();

    /**
     * 添加权限
     * @param permission
     * @param ids
     */
    void addPermission(Permission permission, Integer[] ids);
    /**
     * 编辑时回显权限表单信息
     */
    Permission findOnePermissionById(Integer id);
    /**
     * 编辑时回显角色列表信息
     */
    Integer[] echoIds(Integer id);

    /**
     * 修改权限信息
     * @param permission
     * @param ids
     */
    void editPermission(Permission permission, Integer[] ids);

    /**
     * 删除权限
     * @param id
     */
    void deleteById(int id);
}
