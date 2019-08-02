package cn.itcast.jobs;

import cn.itcast.constant.RedisConstant;
import cn.itcast.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @Author hua
 * @date 2019/07/22 9:25
 **/
//已经在配置文件中加载入容器了,不需要注解
public class JobDemo {

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
}
