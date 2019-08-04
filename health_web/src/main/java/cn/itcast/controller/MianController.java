package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author hua
 * @date 2019/08/03 10:37
 **/
@RestController
@RequestMapping("/main")
public class MianController {
    @Reference
    private UserService userService;

    @GetMapping("/getMenu")
    public Result getMenu(String username){
        try {
            List<Map<String,Object>> list =  userService.getMenuByUsername(username);
            return new Result(true, MessageConstant.GET_MENU_SUCCESS,list);
        } catch (Exception e) {
            return new Result(false,MessageConstant.GET_MENU_FAIL);
        }
    }

}
