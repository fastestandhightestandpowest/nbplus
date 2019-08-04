package cn.itcast.dao;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Author hua
 * @date 2019/08/03 12:02
 **/
public interface MenuDao {

    @Select("select m.path,m.name title,m.icon,m.linkUrl,m.`level`\n" +
            "\tfrom t_user u,t_role r,t_menu m,t_user_role ur,t_role_menu rm\n" +
            "\twhere u.id = ur.user_id and r.id = ur.role_id and r.id = rm.role_id and m.id = rm.menu_id\n" +
            "\tand username = #{username}")
    List<Map<String,Object>> findMenuByUserName(String username);
}
