package cn.itcast.dao;

import cn.itcast.pojo.CheckItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Author hua
 * @date 2019/07/19 10:53
 **/
public interface CheckItemDao {
    @Select("insert into t_checkitem values (#{id},#{code},#{name},#{sex},#{age},#{price},#{type},#{attention},#{remark})")
    void add(CheckItem checkItem);

    Page<CheckItem> findAllBlur(String queryString);

    @Delete("delete from t_checkitem where id = #{id}")
    void deleteById(String id);

    @Select("select count(1) from t_checkgroup_checkitem where checkitem_id = #{id}")
    int findById(String id);

    @Update(" update t_checkitem set code= #{code},name= #{name},sex= #{sex},age= #{age},price= #{price},type= #{type},remark= #{remark},attention = #{attention} where id = #{id}")
    void updateById(CheckItem checkItem);
}
