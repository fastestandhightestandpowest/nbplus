package cn.itcast.service.impl;

import cn.itcast.constant.MessageConstant;
import cn.itcast.dao.PermissionDao;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.pojo.Permission;
import cn.itcast.pojo.Role;
import cn.itcast.service.PermissionService;
import cn.itcast.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Package: cn.itcast.service.impl
 * Author: ZZM
 * Date: Created in 2019/8/3 10:18
 **/
@Service(interfaceClass = PermissionService.class)
public class PermissionServiceimpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;

    //查询所有权限
    @Override
    public PageResult findAll(QueryPageBean queryPageBean) {
        if (!StringUtils.isEmity(queryPageBean.getQueryString())) {
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Permission> per = permissionDao.findAll(queryPageBean.getQueryString());
        PageResult result = new PageResult(per.getTotal(), per.getResult());
        return result;
    }

    //回显所有角色信息
    @Override
    public List<Role> findRole() {
        List<Role> roles = permissionDao.findRoles();
        return roles;
    }

    //添加权限
    @Override
    @Transactional
    public void addPermission(Permission permission, Integer[] ids) {
        //插入数据
        permissionDao.addOnePermission(permission);
        //中间表中插入数据
        if (null != ids && ids.length > 0) {
            for (Integer id : ids) {
                permissionDao.addMiddle(permission.getId(), id);
            }
        }
    }

    //编辑时回显权限表单信息
    @Override
    public Permission findOnePermissionById(Integer id) {
        Permission permission = permissionDao.findOnePermissionById(id);
        return permission;
    }

    //编辑时回显角色列表打钩信息
    @Override
    public Integer[] echoIds(Integer id) {
        Integer[] ids = permissionDao.echoIds(id);
        return ids;
    }

    //修改权限信息
    @Override
    @Transactional
    public void editPermission(Permission permission, Integer[] ids) {
        //修改=>删除中间关系=>插入中间关系
        permissionDao.editPermission(permission);
        permissionDao.deleteMiddle(permission.getId());
        if (null != ids && ids.length > 0) {
            for (Integer id : ids) {
                permissionDao.addMiddle(permission.getId(), id);
            }
        }
    }


    //删除信息
    @Override
    @Transactional
    public void deleteById(int id) {
        //先查看中间表关系
        //无关系则删除
       int count= permissionDao.findMiddlePr(id);
        if (count > 0) {
            throw new RuntimeException(MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        permissionDao.deleteGroup(id);

    }

}
