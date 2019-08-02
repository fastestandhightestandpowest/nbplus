package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.entity.Result;
import cn.itcast.pojo.CheckGroup;
import cn.itcast.pojo.CheckItem;
import cn.itcast.service.CheckGroupService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author hua
 * @date 2019/07/20 13:01
 **/
@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    @RequestMapping("/findAllCheckItems")
    public Result findAllCheckItems(){
        try {
            List<CheckItem> list = checkGroupService.findAllCheckItems();
            return new Result(true,null,list);
        } catch (Exception e) {
            return new Result(false,null);
        }
    }

    /**
     *添加检查组
     * @param
     * @param checkitemIds
     */
    @RequestMapping("/addGroup")
    public Result addGroup(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        try {
            checkGroupService.addGroup(checkGroup,checkitemIds);
            return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    /**
     * 更新检查组
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @RequestMapping("/updateGroup")
    public Result updateGroup(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        try {
            checkGroupService.updateGroup(checkGroup,checkitemIds);
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    /**
     * 查询所有检查组
     */
    @RequestMapping("/findAll")
    public Result findAll(@RequestBody QueryPageBean queryPageBean){
        try {
            PageResult pageResult = checkGroupService.findAllCheckGroup(queryPageBean);
            return new Result(true, null,pageResult);
        } catch (Exception e) {
            return new Result(false,"查询失败");
        }
    }

    /**
     * 查询检查组中所有勾选的检查项的id
     */
    @RequestMapping("/getCheckIds")
    public Result getCheckIds(int id){
        try {
            int[] arr = checkGroupService.getCheckIds(id);
            return new Result(true,null,arr);
        } catch (Exception e) {
            return new Result(false, e.getMessage());
        }
    }

    /**
     * 删除检查组
     */
    @RequestMapping("/deleteById")
    public Result deleteById(int id){
        try {
            checkGroupService.deleteById(id);
            return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }
}
