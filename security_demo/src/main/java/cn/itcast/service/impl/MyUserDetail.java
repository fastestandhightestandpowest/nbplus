package cn.itcast.service.impl;


import cn.itcast.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author hua
 * @date 2019/07/28 21:09
 **/
public class MyUserDetail implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        1.根据用户名查询数据库,获得user
        User user = findUserByUname();
//        2.判断是否为null
        if (user == null){
            //返回null,表示授权失败
            return null;
        }
//        3.把用户名,数据库的密码,以及查询出来的权限访问,创建UserDetails对象返回
        //package org.springframework.security.core.userdetails;
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        list.add(new SimpleGrantedAuthority("add"));
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),list);
        return userDetails;
    }

    public User findUserByUname(){
//        模仿查询到数据
        User user = new User();
        user.setUsername("admin");
        //因为使用了BCryptPasswordEncoder密文,这个密码是模拟数据库查出的密文,
        user.setPassword("$2a$10$Y7l3mCGY4zKqyb3ZJ7sHbeGjDP66cLCYQM3YaZBttzeB3esJt67h.");
        return user;
    }
}
