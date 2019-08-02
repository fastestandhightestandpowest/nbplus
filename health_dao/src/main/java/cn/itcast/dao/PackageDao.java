package cn.itcast.dao;

import cn.itcast.pojo.CheckGroup;
import cn.itcast.pojo.Package;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Author hua
 * @date 2019/07/21 19:41
 **/
public interface PackageDao {

    /**
     * 查询所有检查组
     * @return
     */
    @Select("select id,code,name,helpCode,sex,remark,attention from t_checkgroup")
    List<CheckGroup> queryAllCheckGroup();

    /**
     * 添加进t_package
     * @param pack
     */
    void addPackage(Package pack);

    /**
     * 添加进package和checkgroup的中间表
     * @param id
     * @param checkgroupId
     */
    @Insert("insert into t_package_checkgroup values (#{id},#{checkgroupId})")
    void addPackage_CheckGroup(@Param("id") int id,@Param("checkgroupId") int checkgroupId);

    /**
     * 分页查询
     * @param queryString
     * @return
     */
    Page<Package> findPage(String queryString);

    /**
     * 查询所有
     * @return
     */
    @Select("select * from t_package")
    List<Package> findAll();

    /**
     * 根据套餐id查询关于该套餐所有的检查组和检查项
     * @param id
     * @return
     */
    Package findPackag_GroupAndItem_byId(int id);

    /**
     * 根据套餐id查询套餐的信息
     * @return
     */
    @Select("select name,remark,sex,age,img from t_package where id = #{id}")
    Package getPackage_orderInfo(int id);

    /**
     * 查询所有套餐的name和预约数量
     * @return
     */
    @Select("select p.name as name,count(p.id) as value from (select p.name as name,o.id from t_package p left JOIN t_order o on p.id = o.package_id) p GROUP BY p.name")
    List<Map<String,Integer>> findEveryPackageNameAndOrderNum();

    /**
     * 查询预约数量前四的套餐的报表信息
     * @return
     */
    @Select("select p.name as name,o.count as count,o.count/n.num as proportion,p.remark as remark from \n" +
            "\t\t(SELECT count(1) count,package_id from t_order GROUP BY package_id ORDER BY count desc limit 4) o,\n" +
            "\t\tt_package p,\n" +
            "\t\t(SELECT count(1) num from t_order) n\n" +
            "where\n" +
            "\t\to.package_id = p.id")
    List<Map<String,Object>> getHotPackage();
}
