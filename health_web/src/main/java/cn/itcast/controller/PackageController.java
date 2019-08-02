package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisConstant;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.entity.Result;
import cn.itcast.pojo.CheckGroup;
import cn.itcast.pojo.Package;
import cn.itcast.service.PackageService;
import cn.itcast.utils.QiniuUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @Author hua
 * @date 2019/07/21 19:33
 **/
@RestController
@RequestMapping("/package")
public class PackageController {

    @Reference
    private PackageService packageService;
    @Autowired
    private JedisPool jedisPool;

    @GetMapping("/getAllCheckGroup")
    public Result getAllCheckGroup(){
        try {
            List<CheckGroup> list = packageService.getAllCheckGroup();
            return new Result(true,MessageConstant.QUERY_SETMEALLIST_SUCCESS,list);
        } catch (Exception e) {
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }

    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
        //获取原始文件名
        String originalFilename = imgFile.getOriginalFilename();
        //获取文件后缀名
        int lastIndexOf = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(lastIndexOf - 1);
        //使用UUID随机一个,防止同名文件覆盖
        String ramNum = UUID.randomUUID().toString().replace("-", "");
        String fileName = ramNum + suffix;
        //使用qiniu工具类上传
        try {
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            //图片上传成功
            //将上传图片名称存入redis,基于redis的set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        } catch (IOException e) {
            //图片上传失败
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 添加套餐
     * @param pack
     * @param checkgroupIds
     */
    @PostMapping("/add")
    public Result add(@RequestBody Package pack, int[] checkgroupIds){
        try {
            packageService.add(pack,checkgroupIds);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pack.getImg());
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL+"---"+e.getMessage());
        }
    }

    /**
     * 分页查询
     */
    @RequestMapping("/findAll")
    public Result findAll(@RequestBody QueryPageBean queryPageBean){
        try {
            PageResult pageResult = packageService.findPage(queryPageBean);
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,pageResult);
        } catch (Exception e) {
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL );
        }
    }
}
