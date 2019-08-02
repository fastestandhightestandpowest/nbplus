package cn.itcast.service;

import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.pojo.CheckGroup;
import cn.itcast.pojo.Package;

import java.util.List;
import java.util.Map;

/**
 * @Author hua
 * @date 2019/07/21 19:35
 **/
public interface PackageService {
    List<CheckGroup> getAllCheckGroup();

    void add(Package pack, int[] checkgroupIds);

    PageResult findPage(QueryPageBean queryPageBean);

    List<Package> findAll();

    Package findById(int id);

    Package getPackage_orderInfo(int id);

    List<Map<String,Integer>> findEveryPackageOrderNum();
}
