package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Permission;
import cn.itcast.pojo.Role;
import cn.itcast.service.PermissionService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Package: cn.itcast.controller
 * Author: ZZM
 * Date: Created in 2019/8/3 10:07
 **/

@RestController
@RequestMapping("/permission")
public class permissionController {
    @Reference
    private PermissionService permissionService;

    /**
     * 查询所有权限
     *
     * @param queryPageBean
     * @return
     */
    @PostMapping("/select")
    public Result Demo01(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = permissionService.findAll(queryPageBean);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, pageResult);
    }

    /**
     * 添加权限信息时回显角色列表
     */
    @GetMapping("/findRole")
    public Result Demo02() {
        List<Role> roles = permissionService.findRole();
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, roles);
    }

    /**
     * 添加权限
     */
    @PostMapping("/add")
    public Result Demo03(@RequestBody Permission permission,@RequestParam("checkitemIds") Integer[] ids) {
        permissionService.addPermission(permission, ids);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS);
    }

    /**
     * 编辑时回显权限表单信息
     */
    @GetMapping("/echoTable")
    public Result Demo04(Integer id) {
        Permission permission = permissionService.findOnePermissionById(id);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, permission);
    }
    /**
     * 编辑时回显角色打钩信息
     */
    @GetMapping("/echoIds")
    public Result Demo05(Integer id) {
        Integer[] ids = permissionService.echoIds(id);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,ids);
    }
    /**
     * 编辑权限信息
     */
    @PostMapping("/update")
    public Result Demo06(@RequestBody Permission permission,Integer[] ids){
        permissionService.editPermission(permission,ids);
        return  new Result(true,MessageConstant.QUERY_ORDER_SUCCESS);
    }
    /**
     * 删除权限项
     */
    @PostMapping("/deleteById")
    public Result Demo(int id){
        permissionService.deleteById(id);
        return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS);
    }
}

