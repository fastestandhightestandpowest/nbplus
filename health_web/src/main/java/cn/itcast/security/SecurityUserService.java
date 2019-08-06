package cn.itcast.security;

import cn.itcast.pojo.Permission;
import cn.itcast.pojo.Role;
import cn.itcast.pojo.User;
import cn.itcast.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;

/**
 * @Author hua
 * @date 2019/07/29 10:14
 **/
@Component
public class SecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.调用业务,进行用户查找
        User user = userService.findUserByUsername(username);
        if (user == null){
            return null;
        }
        //2.授权
        ArrayList<GrantedAuthority> arrayList = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                //权限授权
                arrayList.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
            //角色授权
            arrayList.add(new SimpleGrantedAuthority(role.getKeyword()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),arrayList);
    }
}
