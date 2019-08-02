package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.entity.Result;
import cn.itcast.pojo.CheckItem;
import cn.itcast.service.CheckItemService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author hua
 * @date 2019/07/19 8:52
 *
 * 体检检查项管理
 **/
@RestController
@RequestMapping("/checkItem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;

    @RequestMapping(value = "/add")
    @PreAuthorize("hasAnyAuthority('CHECKITEM_ADD')")
    public Result add(@Valid @RequestBody CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            return new Result(false,MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }


    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkItemService.findPage(queryPageBean);
        return pageResult;
    }

    @PostMapping("/deleteById")
    @PreAuthorize("hasAnyAuthority('CHECKITEM_DELETE')")
    public Result deleteById(String id){
        try {
            checkItemService.deleteById(id);
            return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            //e.printStackTrace();
            return new Result(false,e.getMessage()+MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody CheckItem checkItem){
        try {
            checkItemService.update(checkItem);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            //e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL );
        }
    }
}
