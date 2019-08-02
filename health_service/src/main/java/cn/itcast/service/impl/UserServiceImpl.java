package cn.itcast.service.impl;

import cn.itcast.dao.PermissionDao;
import cn.itcast.dao.RoleDao;
import cn.itcast.dao.UserDao;
import cn.itcast.pojo.Permission;
import cn.itcast.pojo.Role;
import cn.itcast.pojo.User;
import cn.itcast.service.UserService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * @Author hua
 * @date 2019/07/29 10:28
 **/
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    /*@Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;*/

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    @Override
    public User findUserByUsername(String username) {
        User user = userDao.findUserByUserName(username);
        /*if (user != null){
            Set<Role> roles = roleDao.findRoleByUser_id(user.getId());
            if (roles.size() > 0) {
                for (Role role : roles) {
                    Set<Permission> permissions = permissionDao.findPermissionByRole_id(role.getId());
                    role.setPermissions(permissions);
                }
            }
            user.setRoles(roles);
        }*/
        return user;
    }
}
