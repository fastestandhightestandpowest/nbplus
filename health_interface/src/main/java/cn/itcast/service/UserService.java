package cn.itcast.service;

import cn.itcast.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * @Author hua
 * @date 2019/07/29 10:18
 **/
public interface UserService {
    User findUserByUsername(String username);

    List<Map<String,Object>> getMenuByUsername(String username);
}
