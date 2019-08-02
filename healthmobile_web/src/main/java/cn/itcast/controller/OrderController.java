package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisMessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Order;
import cn.itcast.service.OrderService;
import cn.itcast.utils.SMSUtils;
import cn.itcast.utils.ValidateCodeUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @Author hua
 * @date 2019/07/26 9:22
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;

    /**
     * 发送验证码业务
     * @param telephone
     * @return
     */
    @PostMapping("/send4Order")
    public Result send4Order(String telephone){
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code+"");
            logger.debug("send SMS success --- ", telephone);
        } catch (ClientException e) {
            logger.debug("send SMS fail ---", telephone);
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //验证码存入redis中,使用setex,设置存活时间
        jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_ORDER, 60*60, code.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    /**
     * 提交预定业务
     * @param map
     * @return
     */
    @PostMapping("/submit")
    public Result submit(@RequestBody Map map){
        //先校验验证码
        String telephone = (String) map.get("telephone");
        //从Redis中获取缓存的验证码，key为手机号+RedisConstant.SENDTYPE_ORDER
        String key = telephone+RedisMessageConstant.SENDTYPE_ORDER;
        String codeInRedis = jedisPool.getResource().get(key);
        String validateCode = (String) map.get("validateCode");
        if (codeInRedis == null || !codeInRedis.equals(validateCode)){
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
        //调用体检预约服务
        Result result = null;
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.order(map);
        } catch (Exception e) {
            //预约失败
            result = new Result(false,e.getMessage());
        }finally{
            //判断是否预约成功,成功发送短信通知 //省略
            return result;
        }
    }

    /**
     * 获取预约成功界面的信息
     * @return
     */
    @GetMapping("/findById")
    public Result getOrderSuccessInfo(@Param("id") int id){
        try {
            Map<String,Object> map = orderService.getOrderSuccessInfoById(id);
            return new Result(true, null,map);
        } catch (Exception e) {
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
