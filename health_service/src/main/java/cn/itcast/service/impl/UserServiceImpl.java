package cn.itcast.service.impl;

import cn.itcast.dao.MenuDao;
import cn.itcast.dao.UserDao;
import cn.itcast.pojo.User;
import cn.itcast.service.UserService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
    @Autowired
    private MenuDao menuDao;

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

    /**
     * 根据用户名得到所拥有权限的菜单
     * @param username
     * @return
     */
    @Override
    public List<Map<String, Object>> getMenuByUsername(String username) {
        //根据用户名查询role,然后再查询出role对应的所有menu
        List<Map<String,Object>> menus = menuDao.findMenuByUserName(username);
        //根据level分成两个集合
        List<Map<String, Object>> one_level = new ArrayList<>();
        List<Map<String, Object>> two_level = new ArrayList<>();
        if (menus!=null && menus.size()>0) {
            for (Map<String, Object> menu : menus) {
                if ((Integer)menu.get("level") == 1) {
                    one_level.add(menu);
                } else {
                    two_level.add(menu);
                }
            }
        }
        for (int i = 0; i < one_level.size(); i++) {
            //移除level属性
            one_level.get(i).remove("level");
            int count = 0; //判断children是否为空
            String path = (String) one_level.get(i).get("path");
            if (path != null) {
                List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
                for (Map<String, Object> map : two_level) {
                    //移除icon属性
                    map.remove("icon");
                    String twoPath = (String) map.get("path");
                    //如:"/3-1" 取到第二个字符"3"
                    String substring = twoPath.substring(1, 2);
                    if (path.equals(substring)) {
                        count++;
                        children.add(map);
                    }
                }
                one_level.get(i).put("children",children);
            }
            //如果二级菜单为空,则移除一级菜单
            if (count == 0){
                one_level.remove(i);
            }
        }
        return one_level;
    }
}
