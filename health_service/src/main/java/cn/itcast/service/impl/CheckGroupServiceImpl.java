package cn.itcast.service.impl;

import cn.itcast.dao.CheckGroupDao;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.pojo.CheckGroup;
import cn.itcast.pojo.CheckItem;
import cn.itcast.service.CheckGroupService;
import cn.itcast.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author hua
 * @date 2019/07/20 14:39
 **/
@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    public List<CheckItem> findAllCheckItems() {
        return checkGroupDao.findAllCheckItems();
    }

    /**
     * 添加检查组
     * @param
     * @param checkitemIds
     */
    @Override
    @Transactional
    public void addGroup(CheckGroup checkGroup, Integer[] checkitemIds) {
        //先把基本信息添加进t_checkgroup
        checkGroupDao.addGroup(checkGroup);
        //然后查出该检查组的id  //在配置文件中插入的时候已经查询到了
        //遍历检查项的ids,每次都存入t_checkgroup_checkitem,存检查组的id和检查项的id
        Integer id = checkGroup.getId();
        addGroup_items(id,checkitemIds);
    }

    @Override
    public PageResult findAllCheckGroup(QueryPageBean queryPageBean) {
        if (!StringUtils.isEmity(queryPageBean.getQueryString())){
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        List<CheckGroup> list=checkGroupDao.findAllCheckGroup(queryPageBean.getQueryString());
        PageInfo pageInfo=new PageInfo(list);
        //Page<CheckGroup> page = checkGroupDao.findAllCheckGroup(queryPageBean.getQueryString());
        //return new PageResult(page.getTotal(),page.getResult());
        return new PageResult(pageInfo.getTotal(),list);
    }

    @Override
    public int[] getCheckIds(int id) {
        return checkGroupDao.getCheckIds(id);
    }

    @Override
    @Transactional
    public void updateGroup(CheckGroup checkGroup, Integer[] checkitemIds) {
        //先修改检查组的信息
        checkGroupDao.updateCheckGroup(checkGroup);
        //然后根据检查组的id在t_checkgroup_checkitem表中删除所有记录
        checkGroupDao.deleteByCheckGroupId(checkGroup.getId());
        //然后再遍历检查项的id,添加记录
        addGroup_items(checkGroup.getId(), checkitemIds);
    }

    /**
     * 删除检查组
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(int id) {

        // 先删除中间表的信息
        checkGroupDao.deleteByCheckGroupId(id);
        //再删除检查组的信息,
        checkGroupDao.deleteGroup(id);

    }


    /**
     * 遍历检查项的ids,每次都存入t_checkgroup_checkitem,存检查组的id和检查项的id
     */
    @Transactional
    public void addGroup_items(int groupId,Integer[] itemIds){
        if (itemIds != null){
            for (int itemId : itemIds) {
                checkGroupDao.addGroup_items(groupId,itemId);
            }
        }
    }

}
