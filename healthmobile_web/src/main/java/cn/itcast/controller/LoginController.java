package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisMessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Member;
import cn.itcast.service.LoginService;
import cn.itcast.utils.SMSUtils;
import cn.itcast.utils.ValidateCodeUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @Author hua
 * @date 2019/07/27 9:35
 **/
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;
    private Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Reference
    private LoginService loginService;

    @PostMapping("/check")
    public Result check(@RequestBody Map<String,Object> map, HttpServletResponse response){
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (codeInRedis == null){
            //验证码超时或者没有发送
            send4Order(telephone);
            return new Result(false, MessageConstant.VALIDATECODE_sendNow);
        }
        if (validateCode == null || !codeInRedis.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //判断是否为会员
        Member member = loginService.findMemberByphoneNumber(telephone);
        if (member == null){
            member = new Member();
            member.setRegTime(new Date());
            member.setPhoneNumber(telephone);
            loginService.addMember(member);
        }
        // 用户跟踪，写手机号码到Cookie，当用户再次访问我们的网站时就会带上这个cookie，这样我们就知道是哪个用户了，方便日后做统计分析
        Cookie cookie = new Cookie("login_member_telephone",telephone);
        cookie.setPath("/");//设置项目路径
        cookie.setMaxAge(60*60*24*30);//有效时间
        response.addCookie(cookie);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }

    @PostMapping("/send4Order")
    public Result send4Order(String telephone){
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code+"");
            logger.debug("send SMS success --- ", telephone);
        } catch (ClientException e) {
            logger.debug("send SMS fail --- ", telephone);
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //验证码存入redis中
        jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN, 60*60, code.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
