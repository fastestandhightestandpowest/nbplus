package cn.itcast.service.impl;

import cn.itcast.constant.RedisConstant;
import cn.itcast.dao.PackageDao;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.pojo.CheckGroup;
import cn.itcast.pojo.Package;
import cn.itcast.service.PackageService;
import cn.itcast.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author hua
 * @date 2019/07/21 19:39
 **/
@Service(interfaceClass = PackageService.class)
public class PackageServiceImpl implements PackageService {

    @Autowired
    private PackageDao packageDao;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 查询所有检查组
     * @return
     */
    @Override
    public List<CheckGroup> getAllCheckGroup() {
        return packageDao.queryAllCheckGroup();
    }

    /**
     * 添加套餐
     * @param pack
     * @param checkgroupIds
     */
    @Override
    @Transactional  //属性可以设置隔离级别
    public void add(Package pack, int[] checkgroupIds) {
        //先添加主表
        packageDao.addPackage(pack);
        //得到id
        int id = pack.getId();
        //再遍历checkGroupIds,添加从表
        addPackage_group(id, checkgroupIds);
    }


    /**
     * 查询分页数据
     * @param
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        if (!StringUtils.isEmity(queryPageBean.getQueryString())){
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Package> page = packageDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<Package> findAll() {
        List<Package> packages = packageDao.findAll();
        return packages;
    }

    /**
     * 根据套餐id查询套餐和里面所有的检查组合检查项信息
     * @return
     */
    @Override
    public Package findById(int id) {
        Package pack = packageDao.findPackag_GroupAndItem_byId(id);
        return pack;
    }

    /**
     * 根据套餐id查询套餐的图片,性别和年龄
     * @return
     */
    @Override
    public Package getPackage_orderInfo(int id) {
        return packageDao.getPackage_orderInfo(id);
    }


    /**
     * 添加进package和checkGroup中间表
     */
    public void addPackage_group(int id,int[] checkgroupIds){
        if (checkgroupIds != null){
            for (int checkgroupId : checkgroupIds) {
                packageDao.addPackage_CheckGroup(id,checkgroupId);
            }
        }
    }


    /**
     * 查询每个套餐的name和预约数量
     * @return
     */
    @Override
    public List<Map<String, Integer>> findEveryPackageOrderNum() {
        //查询所有套餐的id和name
        return packageDao.findEveryPackageNameAndOrderNum();
    }
}
