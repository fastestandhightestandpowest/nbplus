package cn.itcast.service;

import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.pojo.CheckItem;

/**
 * @Author hua
 * @date 2019/07/19 9:12
 **/
public interface CheckItemService {

    public void add(CheckItem checkItem);

    PageResult findPage(QueryPageBean queryPageBean);

    void deleteById(String id);

    void update(CheckItem checkItem);
}
