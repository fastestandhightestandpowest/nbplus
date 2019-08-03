package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisConstant;
import cn.itcast.constant.RedisMessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Package;
import cn.itcast.service.PackageService;
import cn.itcast.utils.QiniuUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author hua
 * @date 2019/07/25 9:39
 **/
@RestController
@RequestMapping("/package")
public class PackageController {

    @Reference
    private PackageService packageService;

    /**
     * 查询所有套餐信息
     * @return
     */
    @PostMapping("/getPackage")
    public Result getPackage(){
        try {
            //在service已经把图片链接地址封装好了
            String jsonData = packageService.findAll();
            /*for (Package aPackage : list) {
                aPackage.setImg(QiniuUtils.URL +"/"+aPackage.getImg());
            }*/
//            list.forEach(pack -> {
//                pack.setImg(QiniuUtils.URL +"/"+pack.getImg());
//            });
            return new Result(true,MessageConstant.GET_SETMEAL_LIST_SUCCESS,jsonData);
        } catch (Exception e) {
            return new Result(false,MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    /**
     * 根据套餐id查询套餐和里面所有的检查组合检查项信息
     */
    @GetMapping("/getPackageDetail")
    public Result findById(int id){
        try {
            String jsonData= packageService.findById(id);
//            pack.setImg(QiniuUtils.URL +"/"+pack.getImg());
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,jsonData);
        } catch (Exception e) {
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    /**
     * 根据套餐id查询套餐的信息
     */
    @GetMapping("/getPackage_orderInfo")
    public Result getPackage_orderInfo(int id){
        try {
            Package pack = packageService.getPackage_orderInfo(id);
            pack.setImg(QiniuUtils.URL+"/"+pack.getImg());
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,pack);
        } catch (Exception e) {
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
