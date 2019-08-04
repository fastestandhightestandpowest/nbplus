package cn.itcast.dao;


import cn.itcast.pojo.Permission;
import cn.itcast.pojo.Role;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zzm
 * @date 2019/08/03 10:41
 **/
public interface PermissionDao {

    /**
     * 查询所有权限集合
     *
     * @return
     */
    Page<Permission> findAll(String value);

    /**
     * 回显所有角色列表信息
     *
     * @return
     */
    List<Role> findRoles();

    /**
     * 插入一条权限信息
     *
     * @param permission
     */
    void addOnePermission(Permission permission);

    /**
     * 权限_角色中间表中插入数据
     *
     * @param id
     * @param id1
     */
    void addMiddle(@Param("pid") Integer id, @Param("rid") Integer id1);

    /**
     * 编辑时回显权限表单信息
     *
     * @param id
     * @return
     */
    Permission findOnePermissionById(Integer id);

    /**
     * 编辑时回显角色列表信息
     * @param id
     * @return
     */
    Integer[] echoIds(Integer id);

    /**
     * 修改权限信息
     * @param permission
     */
    void editPermission(Permission permission);

    /**
     * 删除中间表关系
     * @param id
     */
    void deleteMiddle(int id);

    /**
     * 查看中间表关系
     * @param id
     * @return
     */
    int findMiddlePr(int id);

    /**
     * 删除权限
     * @param id
     */
    void deleteGroup(int id);
}
