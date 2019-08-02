package cn.itcast.dao;

import cn.itcast.pojo.CheckGroup;
import cn.itcast.pojo.CheckItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @Author hua
 * @date 2019/07/20 14:40
 **/
public interface CheckGroupDao {
    @Select("select * from t_checkitem")
    List<CheckItem> findAllCheckItems();

    void addGroup(CheckGroup checkGroup);

    @Insert("insert into t_checkgroup_checkitem values (#{groupId},#{itemId})")
    void addGroup_items(@Param("groupId") int groupId,@Param("itemId") int itemId);

    Page<CheckGroup> findAllCheckGroup(String queryString);

    @Select("select checkitem_id from t_checkgroup_checkitem where checkgroup_id = #{id}")
    int[] getCheckIds(int id);

    @Update("update t_checkgroup set code = #{code},name = #{name},helpCode = #{helpCode},sex = #{sex},remark = #{remark},attention = #{attention} where id = #{id}")
    void updateCheckGroup(CheckGroup checkGroup);

    /**
     * 根据检查组的id在t_checkgroup_checkitem表中删除所有记录
     * @param id
     */
    @Delete("delete from t_checkgroup_checkitem where checkgroup_id = #{id}")
    void deleteByCheckGroupId(Integer id);

    /**
     * 删除检查组表中的信息
     * @param id
     */
    @Delete("delete from t_checkgroup where id = #{id}")
    void deleteGroup(int id);
}
