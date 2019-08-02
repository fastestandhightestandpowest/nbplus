package cn.itcast.service;

import cn.itcast.pojo.User;

/**
 * @Author hua
 * @date 2019/07/29 10:18
 **/
public interface UserService {
    User findUserByUsername(String username);
}
