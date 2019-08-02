package cn.itcast.controller;

import cn.itcast.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author hua
 * @date 2019/07/29 21:05
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/getUsername")
    public Result getUsername(){
        String username = null;
        try {
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            username = user.getUsername();
            return new Result(true,null,username);
        } catch (Exception e) {
            return new Result(false,"获取用户名失败");
        }
    }
}
