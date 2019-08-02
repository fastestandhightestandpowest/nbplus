package cn.itcast.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author hua
 * @date 2019/07/28 21:02
 **/
@Controller
@RequestMapping("/user")
public class UserController {

    /*public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String admin = bCryptPasswordEncoder.encode("admin");
        System.out.println(admin);
    }*/
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('add')")
    public void add(){
        System.out.println("add");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/delete")
    public void delete(){
        System.out.println("delete");
    }

}
