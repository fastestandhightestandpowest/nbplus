package cn.itcast.service.impl;

import cn.itcast.dao.CheckItemDao;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.pojo.CheckItem;
import cn.itcast.service.CheckItemService;
import cn.itcast.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author hua
 * @date 2019/07/19 10:02
 *
 * 体检项服务
 **/
@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    @Transactional
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        if (!StringUtils.isEmity(queryPageBean.getQueryString())) {
            //条件不为空
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckItem> page = checkItemDao.findAllBlur(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        //删除之前需要先判断有没有在其他表使用
        int i = checkItemDao.findById(id);
        if (i > 0) {
            throw new RuntimeException("该项已经在使用");
        }else{
            checkItemDao.deleteById(id);
        }
    }

    @Override
    @Transactional
    public void update(CheckItem checkItem) {
        checkItemDao.updateById(checkItem);
    }
}
