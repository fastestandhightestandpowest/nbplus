package cn.itcast.jobs;

import cn.itcast.constant.RedisConstant;
import cn.itcast.service.OrderSettingService;
import cn.itcast.utils.DateUtils;
import cn.itcast.utils.QiniuUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Set;

/**
 * @Author hua
 * @date 2019/07/22 9:25
 **/
//已经在配置文件中加载入容器了,不需要注解
public class JobDemo {
    @Reference
    private OrderSettingService orderSettingService;
    @Autowired
    private JedisPool jedisPool;
    public void run(){
        //比较两个redis集合中的值,把差值从七牛上清除
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (sdiff != null) {
            for (String pic : sdiff) {
                //先删除七牛空间上的
                QiniuUtils.deleteFileFromQiniu(pic);
                //再删除redis集合中的RedisConstant.SETMEAL_PIC_RESOURCES
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,pic);
            }
        }
        System.out.println("清理----------------------------------------------------");
    }
//    清理下数据库中的内容

    public void clear(){
        //    获取当前月份的一号,清理该月一号之前的数据即可
        Date firstDay4ThisMonth = DateUtils.getFirstDay4ThisMonth();
        orderSettingService.clear(firstDay4ThisMonth);
        System.out.println("清理数据----------------------------------------------------");
    }

}
