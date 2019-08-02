package cn.itcast.service;

import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.pojo.CheckGroup;
import cn.itcast.pojo.CheckItem;

import java.util.List;

/**
 * @Author hua
 * @date 2019/07/20 14:32
 **/
public interface CheckGroupService {


    List<CheckItem> findAllCheckItems();

    void addGroup(CheckGroup checkGroup, Integer[] checkitemIds);


    PageResult findAllCheckGroup(QueryPageBean queryPageBean);

    int[] getCheckIds(int id);

    void updateGroup(CheckGroup checkGroup, Integer[] checkitemIds);

    void deleteById(int id);
}
